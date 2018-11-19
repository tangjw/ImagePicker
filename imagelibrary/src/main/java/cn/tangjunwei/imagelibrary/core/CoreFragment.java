package cn.tangjunwei.imagelibrary.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.album.AlbumActivity;
import cn.tangjunwei.imagelibrary.crop.CropDialogFragment;

/**
 * 处理接口回调等等的桥梁
 * <p>
 * Created by tangjunwei on 2018/11/16.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class CoreFragment extends Fragment {
    private static final int REQUEST_TAKE_AVATAR = 101;
    private static final int REQUEST_TAKE_IMAGE = 102;
    private static final int REQUEST_SELECT_AVATAR = 103;
    private Picker.OnImageSelectListener mOnImageSelectListener;
    private int mCoreType;
    private int mMaxCount;
    private CropOption mCropOption;
    private FragmentActivity mActivity;
    private String mCurrentState;
    private String mCurrentPhotoPath;
    
    static CoreFragment newInstance(int type, CropOption cropOption, int maxCount) {
        
        Bundle args = new Bundle();
        args.putInt("CoreType", type);
        args.putInt("MaxCount", maxCount);
        args.putSerializable("CropOption", cropOption);
        CoreFragment fragment = new CoreFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    void setOnImageSelectListener(Picker.OnImageSelectListener onImageSelectListener) {
        mOnImageSelectListener = onImageSelectListener;
    }
    
    @Override
    public void onAttach(Context context) {
        System.out.println("CoreFragment onAttach");
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("CoreFragment onCreate");
        if (savedInstanceState != null) {
            mCurrentState = savedInstanceState.getString("CurrentState");
            mCurrentPhotoPath = savedInstanceState.getString("CurrentPhotoPath");
            ImageLoader imageLoader = (ImageLoader) savedInstanceState.getSerializable("ImageLoader");
            if (imageLoader != null) {
                ImagePicker.getInstance().initImageLoader(imageLoader);
            } else {
                closeSelf();
            }
            
            ImagePicker.getInstance().setCurrentState(mCurrentState);
        }
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCoreType = args.getInt("CoreType");
            mMaxCount = args.getInt("MaxCount");
            mCropOption = (CropOption) args.getSerializable("CropOption");
            if (mCropOption == null) {
                mCropOption = new CropOption();
            }
        }
        
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("CoreFragment onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mOnImageSelectListener == null) {
            closeSelf();
        }
        if (mCurrentState != null) return; //从状态恢复 避免重复操作
        switch (mCoreType) {
            case CoreType.TAKE_PIC:
                takePic(REQUEST_TAKE_IMAGE);
                mCurrentState = "TAKE_PIC";
                break;
            case CoreType.TAKE_PIC_CROP:
                mCurrentState = "TAKE_PIC_CROP";
                takePic(REQUEST_TAKE_AVATAR);
                break;
            case CoreType.SELECT_IMAGE:
                mCurrentState = "SELECT_IMAGE";
                //takePic();
                break;
            case CoreType.SELECT_AVATAR:
                mCurrentState = "SELECT_AVATAR";
                selectAvatar();
                break;
            default://什么也不做直接关闭
                mCurrentState = null;
                closeSelf();
                break;
        }
        
        ImagePicker.getInstance().setCurrentState(mCurrentState);
        
    }
    
    @Override
    public void onStart() {
        System.out.println("CoreFragment onStart");
        super.onStart();
    }
    
    @Override
    public void onResume() {
        System.out.println("CoreFragment onResume");
        super.onResume();
    }
    
    @Override
    public void onPause() {
        System.out.println("CoreFragment onPause");
        super.onPause();
    }
    
    @Override
    public void onStop() {
        System.out.println("CoreFragment onStop");
        super.onStop();
    }
    
    @Override
    public void onDestroyView() {
        System.out.println("CoreFragment onDestroyView");
        super.onDestroyView();
    }
    
    @Override
    public void onDetach() {
        System.out.println("CoreFragment onDetach");
        super.onDetach();
    }
    
    @Override
    
    public void onSaveInstanceState(@NonNull Bundle outState) {
        System.out.println("CoreFragment onSaveInstanceState");
        outState.putString("CurrentState", mCurrentState);
        outState.putString("CurrentPhotoPath", mCurrentPhotoPath);
        outState.putSerializable("ImageLoader", ImagePicker.getInstance().getImageLoader());
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult");
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_AVATAR:
                    CropDialogFragment mCropDialogFragment = CropDialogFragment.newInstance(mCurrentPhotoPath, mCropOption.getWith());
                    mCropDialogFragment.setOnImageSelectListener(mOnImageSelectListener);
                    mCropDialogFragment.show(mActivity.getSupportFragmentManager(), CropDialogFragment.class.getSimpleName());
                    break;
                case REQUEST_TAKE_IMAGE:
                    mOnImageSelectListener.onSelectSuccess(new String[]{mCurrentPhotoPath});
                    break;
                case REQUEST_SELECT_AVATAR:
                    mOnImageSelectListener.onSelectSuccess(data.getStringExtra("path"));
                    break;
                default:
                    mOnImageSelectListener.onSelectFail("cancel");
                    break;
            }
        } else {
            mOnImageSelectListener.onSelectFail("resultCode != RESULT_OK");
        }
        closeSelf();
    }
    
    
    private void selectAvatar() {
        startActivityForResult(new Intent(mActivity, AlbumActivity.class), REQUEST_SELECT_AVATAR);
    }
    
    
    /**
     * 拍照
     */
    private void takePic(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                String authority = mActivity.getApplicationInfo().processName + ".imagelibrary.fileprovider";
                Uri photoURI = FileProvider.getUriForFile(mActivity, authority, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
            // this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            
        } else {
            mOnImageSelectListener.onSelectFail("相机不可用");
        }
    }
    
    private void closeSelf() {
        ImagePicker.getInstance().setCurrentState(null);
        mActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
    
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "avatar_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir("avatar");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir      /* directory */
        );
        
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
