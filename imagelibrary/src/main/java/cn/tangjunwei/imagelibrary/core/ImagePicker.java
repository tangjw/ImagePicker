package cn.tangjunwei.imagelibrary.core;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.crop.CropDialogFragment;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/16.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImagePicker implements Picker {
    
    private ImageLoader mImageLoader;
    
    private String mCurrentState;
    
    private ImagePicker() {
    }
    
    public static ImagePicker getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    public void setCurrentState(String currentState) {
        mCurrentState = currentState;
    }
    
    @Override
    public void initListener(@NonNull FragmentActivity activity, @NonNull OnImageSelectListener onImageSelectListener) {
        System.out.println("initListener CurrentState: " + mCurrentState);
        if (mCurrentState == null) return;
        switch (mCurrentState) {
            case "TAKE_PIC":
                takePicture(activity, onImageSelectListener);
                break;
            case "TAKE_PIC_CROP":
                takeAvatar(activity, null, onImageSelectListener);
                break;
            case "SELECT_IMAGE":
                break;
            case "SELECT_AVATAR":
                break;
            default:
                break;
        }
        
    }
    
    @Override
    public void initImageLoader(@NonNull ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }
    
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
    
    @Override
    public void takePicture(@NonNull FragmentActivity fragmentActivity, @NonNull OnImageSelectListener onImageSelectListener) {
        if (checkPermission(fragmentActivity, Manifest.permission.CAMERA, onImageSelectListener)
                || checkPermission(fragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, onImageSelectListener)) {
            return;
        }
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        CoreFragment coreFragment = (CoreFragment) fm.findFragmentByTag(CoreFragment.class.getSimpleName());
        if (coreFragment == null) {
            coreFragment = CoreFragment.newInstance(CoreType.TAKE_PIC, null, 1);
            fm.beginTransaction().add(coreFragment, CoreFragment.class.getSimpleName()).commit();
        }
        coreFragment.setOnImageSelectListener(onImageSelectListener);
        
        
    }
    
    @Override
    public void selectPicture(@NonNull FragmentActivity fragmentActivity, int maxCount, @NonNull OnImageSelectListener onImageSelectListener) {
        if (checkImageLoader(onImageSelectListener)) {
            tryClearOldFragment(fragmentActivity);
            return;
        }
    }
    
    @Override
    public void takeAvatar(@NonNull FragmentActivity fragmentActivity, @Nullable CropOption cropOption, @NonNull OnImageSelectListener onImageSelectListener) {
        if (checkPermission(fragmentActivity, Manifest.permission.CAMERA, onImageSelectListener)) {
            System.out.println("111111111111111111111");
            tryClearOldFragment(fragmentActivity);
            return;
        }
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        CropDialogFragment cropFragment = (CropDialogFragment) fm.findFragmentByTag(CropDialogFragment.class.getSimpleName());
        if (cropFragment != null) {
            System.out.println("22222222222222222222");
            cropFragment.setOnImageSelectListener(onImageSelectListener);
        }
        CoreFragment coreFragment = (CoreFragment) fm.findFragmentByTag(CoreFragment.class.getSimpleName());
        if (coreFragment == null) {
            coreFragment = CoreFragment.newInstance(CoreType.TAKE_PIC_CROP, null, 1);
            fm.beginTransaction().add(coreFragment, CoreFragment.class.getSimpleName()).commit();
        }
        coreFragment.setOnImageSelectListener(onImageSelectListener);
    }
    
    @Override
    public void selectAvatar(@NonNull FragmentActivity fragmentActivity, @Nullable CropOption cropOption, @NonNull OnImageSelectListener onImageSelectListener) {
        if (checkImageLoader(onImageSelectListener)) {
            tryClearOldFragment(fragmentActivity);
            return;
        }
    }
    
    private boolean checkImageLoader(@NonNull OnImageSelectListener onImageSelectListener) {
        if (mImageLoader == null) {
            onImageSelectListener.onSelectFail("please init ImageLoader first!");
            return true;
        }
        return false;
    }
    
    /**
     * 检查权限
     *
     * @param fragmentActivity
     * @param permission
     * @param onImageSelectListener
     * @return
     */
    private boolean checkPermission(@NonNull FragmentActivity fragmentActivity, @NonNull String permission, @NonNull OnImageSelectListener onImageSelectListener) {
        if (ContextCompat.checkSelfPermission(fragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            onImageSelectListener.onSelectFail(permission + " is Denied!");
            tryClearOldFragment(fragmentActivity);
            return true;
        }
        return false;
    }
    
    private void tryClearOldFragment(@NonNull FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        CropDialogFragment fragment = (CropDialogFragment) fm.findFragmentByTag(CropDialogFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.dismiss();
        }
        CoreFragment fragment1 = (CoreFragment) fm.findFragmentByTag(CoreFragment.class.getSimpleName());
        if (fragment1 != null) {
            fm.beginTransaction().remove(fragment1).commit();
        }
    }
    
    private static class SingletonHolder {
        private static final ImagePicker INSTANCE = new ImagePicker();
    }
    
    
}
