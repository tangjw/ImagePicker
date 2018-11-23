package cn.tangjunwei.imagelibrary.core;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    private static final int REQUEST_TAKE_AVATAR = 761;
    private static final int REQUEST_TAKE_IMAGE = 762;
    private static final int REQUEST_SELECT_AVATAR = 763;
    private static final int REQUEST_PERMISSIONS_STORAGE = 764;
    private static final int REQUEST_PERMISSIONS_CAMERA_STORAGE = 765;
    private Picker.OnImageSelectListener mOnImageSelectListener;
    private int mCoreType;
    private int mMaxCount;
    private CropOption mCropOption;
    private FragmentActivity mActivity;
    private int mCurrentState;
    private String mCurrentPhotoPath;
    private boolean isConfigurationChanged;
    private boolean isReadyShowCropDialog;
    private boolean hasCaptured;
    
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
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentState = savedInstanceState.getInt("CurrentState");
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
    
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mOnImageSelectListener == null) {
            closeSelf();
        }
        if (mCurrentState != 0) return; //从状态恢复 避免重复操作
        mCurrentState = mCoreType;
        switch (mCoreType) {
            case CoreType.TAKE_PIC:
                checkCameraPermission(REQUEST_TAKE_IMAGE);
                break;
            case CoreType.TAKE_PIC_CROP:
                checkCameraPermission(REQUEST_TAKE_AVATAR);
                break;
            case CoreType.SELECT_IMAGE:
                //takePic();
                break;
            case CoreType.SELECT_AVATAR:
                selectAvatar();
                break;
            default://什么也不做直接关闭
                mCurrentState = 0;
                closeSelf();
                break;
        }
        
        ImagePicker.getInstance().setCurrentState(mCurrentState);
        
    }
    
    private void checkCameraPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            takePic(requestCode);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CAMERA_STORAGE);
        }
        
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentState", mCurrentState);
        outState.putString("CurrentPhotoPath", mCurrentPhotoPath);
        outState.putSerializable("ImageLoader", ImagePicker.getInstance().getImageLoader());
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_AVATAR:
                    if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                        hasCaptured = true;
                    } else {
                        showCropDialog();
                        closeSelf();
                    }
                    break;
                case REQUEST_TAKE_IMAGE:
                    mOnImageSelectListener.onSelectSuccess(new String[]{mCurrentPhotoPath});
                    closeSelf();
                    break;
                case REQUEST_SELECT_AVATAR:
                    mOnImageSelectListener.onSelectSuccess(data.getStringExtra("path"));
                    closeSelf();
                    break;
                default:
                    mOnImageSelectListener.onSelectFail("cancel");
                    closeSelf();
                    break;
            }
        } else {
            mOnImageSelectListener.onSelectFail("cancel");
            closeSelf();
        }
    
    }
    
    private void showCropDialog() {
        CropDialogFragment mCropDialogFragment = CropDialogFragment.newInstance(mCurrentPhotoPath, mCropOption.getWith());
        mCropDialogFragment.setOnImageSelectListener(mOnImageSelectListener);
        mCropDialogFragment.show(mActivity.getSupportFragmentManager(), CropDialogFragment.class.getSimpleName());
    }
    
    
    private void selectAvatar() {
        
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_STORAGE);
        } else {
            startAlbumActivity();
        }
        
        
    }
    
    private void startAlbumActivity() {
        startActivityForResult(new Intent(mActivity, AlbumActivity.class), REQUEST_SELECT_AVATAR);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startAlbumActivity();
                } else {
                    mOnImageSelectListener.onSelectFail(Manifest.permission.WRITE_EXTERNAL_STORAGE + " is Denied!");
                    closeSelf();
                    
                }
                break;
            case REQUEST_PERMISSIONS_CAMERA_STORAGE:
                if (grantResults.length > 0) {
                    
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            mOnImageSelectListener.onSelectFail(permissions[i] + " is Denied!");
                            closeSelf();
                            return;
                        }
                    }
                    if (mCoreType == CoreType.TAKE_PIC_CROP) {
                        mCurrentState = CoreType.TAKE_PIC_CROP;
                        takePic(REQUEST_TAKE_AVATAR);
                    } else if (mCoreType == CoreType.TAKE_PIC) {
                        mCurrentState = CoreType.TAKE_PIC;
                        takePic(REQUEST_TAKE_IMAGE);
                    }
                    
                } else {
                    mOnImageSelectListener.onSelectFail("permission denied");
                    closeSelf();
                }
                break;
            
        }
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        
        if (hasCaptured && mCurrentPhotoPath != null && newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            showCropDialog();
            closeSelf();
        }
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
        ImagePicker.getInstance().setCurrentState(0);
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
