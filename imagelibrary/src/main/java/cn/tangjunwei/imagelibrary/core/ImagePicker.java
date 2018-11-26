package cn.tangjunwei.imagelibrary.core;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    
    private int mCurrentState;
    
    private ImagePicker() {
    }
    
    public static ImagePicker getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    public void setCurrentState(int currentState) {
        mCurrentState = currentState;
    }
    
    @Override
    public void initListener(@NonNull FragmentActivity activity, @NonNull OnImageSelectListener onImageSelectListener) {
        if (mCurrentState == 0) return;
        switch (mCurrentState) {
            case CoreType.TAKE_PIC:
                takePicture(activity, onImageSelectListener);
                break;
            case CoreType.TAKE_PIC_CROP:
                takeAvatar(activity, null, onImageSelectListener);
                break;
            case CoreType.SELECT_IMAGE:
                break;
            case CoreType.SELECT_AVATAR:
                selectAvatar(activity, null, onImageSelectListener);
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
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        CoreFragment coreFragment = (CoreFragment) fm.findFragmentByTag(CoreFragment.class.getSimpleName());
        if (coreFragment == null) {
            coreFragment = CoreFragment.newInstance(CoreType.TAKE_PIC, null, 1);
            fm.beginTransaction().add(coreFragment, CoreFragment.class.getSimpleName()).commitAllowingStateLoss();
        }
        coreFragment.setOnImageSelectListener(onImageSelectListener);
    }
    
    @Override
    public void selectPicture(@NonNull FragmentActivity fragmentActivity, @IntRange(from = 1, to = 9) int maxCount, @NonNull OnImageSelectListener onImageSelectListener) {
        if (checkImageLoader(onImageSelectListener)) {
            tryClearOldFragment(fragmentActivity);
            return;
        }
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        CoreFragment coreFragment = (CoreFragment) fm.findFragmentByTag(CoreFragment.class.getSimpleName());
        if (coreFragment == null) {
            coreFragment = CoreFragment.newInstance(CoreType.SELECT_IMAGE, null, maxCount);
            fm.beginTransaction().add(coreFragment, CoreFragment.class.getSimpleName()).commitAllowingStateLoss();
        }
        coreFragment.setOnImageSelectListener(onImageSelectListener);
    }
    
    @Override
    public void takeAvatar(@NonNull FragmentActivity fragmentActivity, @Nullable CropOption cropOption, @NonNull OnImageSelectListener onImageSelectListener) {
    
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        CropDialogFragment cropFragment = (CropDialogFragment) fm.findFragmentByTag(CropDialogFragment.class.getSimpleName());
        if (cropFragment != null) {
            cropFragment.setOnImageSelectListener(onImageSelectListener);
        }
        CoreFragment coreFragment = (CoreFragment) fm.findFragmentByTag(CoreFragment.class.getSimpleName());
        if (coreFragment == null) {
            coreFragment = CoreFragment.newInstance(CoreType.TAKE_PIC_CROP, cropOption, 1);
            fm.beginTransaction().add(coreFragment, CoreFragment.class.getSimpleName()).commitAllowingStateLoss();
        }
        coreFragment.setOnImageSelectListener(onImageSelectListener);
    }
    
    @Override
    public void selectAvatar(@NonNull FragmentActivity fragmentActivity, @Nullable CropOption cropOption, @NonNull OnImageSelectListener onImageSelectListener) {
        if (checkImageLoader(onImageSelectListener)) {
            tryClearOldFragment(fragmentActivity);
            return;
        }
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        CoreFragment coreFragment = (CoreFragment) fm.findFragmentByTag(CoreFragment.class.getSimpleName());
        if (coreFragment == null) {
            coreFragment = CoreFragment.newInstance(CoreType.SELECT_AVATAR, cropOption, 1);
            fm.beginTransaction().add(coreFragment, CoreFragment.class.getSimpleName()).commitAllowingStateLoss();
        }
        coreFragment.setOnImageSelectListener(onImageSelectListener);
        
    }
    
    private boolean checkImageLoader(@NonNull OnImageSelectListener onImageSelectListener) {
        if (mImageLoader == null) {
            onImageSelectListener.onSelectFail("please init ImageLoader first!");
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
