package cn.tangjunwei.imagelibrary.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/15.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class CropDialogFragment extends DialogFragment {
    
    private String mPath;
    private ClipImageLayout mClipImageLayout;
    private ClipBorderView mClipImageView;
    private ClipZoomImageView mZoomImageView;
    
    private ImageLoader mImageLoader;
    private int mCropSize;
    private String mAvatarPath;
    
    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }
    
    public static CropDialogFragment newInstance(String path, int cropSize) {
        
        Bundle args = new Bundle();
        args.putString("path", path);
        args.putInt("cropSize", cropSize);
        CropDialogFragment fragment = new CropDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    protected FragmentActivity mActivity;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        } else {
            throw new RuntimeException(context.toString() + "must extends FragmentActivity!");
        }
        
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BrowsePicTheme);
        //getDialog().getWindow().
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPath = arguments.getString("path");
            mCropSize = arguments.getInt("cropSize");
        }
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //mActivity.getWindow().
            Window window = getDialog().getWindow();
            if (window == null) return;
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_crop, container, false);
        
        mClipImageLayout = inflate.findViewById(R.id.clipimagelayout);
        
        mClipImageView = mClipImageLayout.getClipBorderView();
        
        mZoomImageView = mClipImageLayout.getZoomImageView();
        
        inflate.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        inflate.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap avatar = mClipImageLayout.clip(mCropSize, mCropSize);
                if (avatar != null) {
                    if (mOnCropListener != null) {
                        mOnCropListener.onCropSuccess(writeToFile(avatar));
                    }
                }
                dismiss();
            }
        });
        
        if (mImageLoader != null) {
            mImageLoader.loadCropImage(this, mPath, mZoomImageView);
        }
        
        return inflate;
    }
    
    private String writeToFile(@NonNull Bitmap bitmap) {
        String diskCachePath = getDiskCachePath(mActivity);
        deleteAvatarFile(diskCachePath);
        File file = new File(diskCachePath, "avatar_" + System.currentTimeMillis() + ".jpg");
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            bos.flush();
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) try {
                fos.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }
    
    private String getDiskCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()
                && context.getExternalCacheDir() != null) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                return context.getExternalCacheDir().getPath();
            }
            return context.getCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }
    
    private void deleteAvatarFile(String path) {
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        if (dirFile.exists() && dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            //遍历删除文件夹下的所有文件
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith("avatar_")) {
                    file.delete();
                }
            }
            
        }
    }
    
    private OnCropListener mOnCropListener;
    
    public void setOnCropListener(OnCropListener onCropListener) {
        mOnCropListener = onCropListener;
    }
    
    public interface OnCropListener {
        void onCropSuccess(String avatarPath);
    }
    
}
