package cn.tangjunwei.imagelibrary.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
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
import cn.tangjunwei.imagelibrary.core.ImagePicker;
import cn.tangjunwei.imagelibrary.core.Picker;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/15.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class CropDialogFragment extends DialogFragment {
    
    private String mPath;
    private int mCropSize;
    
    private ClipImageLayout mClipImageLayout;
    
    
    public static CropDialogFragment newInstance(String path, int cropSize) {
        Bundle args = new Bundle();
        args.putString("path", path);
        args.putInt("cropSize", cropSize);
        CropDialogFragment fragment = new CropDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    private FragmentActivity mActivity;
    
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
            Window window = getDialog().getWindow();
            if (window == null) return;
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    
    private Picker.OnImageSelectListener mOnImageSelectListener;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mOnImageSelectListener == null) {
            //dismiss();
        }
        View inflate = inflater.inflate(R.layout.fragment_crop, container, false);
        mClipImageLayout = inflate.findViewById(R.id.clipimagelayout);
        ClipBorderView clipImageView = mClipImageLayout.getClipBorderView();
        ClipZoomImageView zoomImageView = mClipImageLayout.getZoomImageView();
        final ImageLoader imageLoader = ImagePicker.getInstance().getImageLoader();
        if (imageLoader != null) {
            imageLoader.loadCropImage(this, mPath, zoomImageView);
        } else {
            mOnImageSelectListener.onSelectFail("ImageLoader = null");
            dismiss();
        }
    
        inflate.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnImageSelectListener != null) {
                    mOnImageSelectListener.onSelectFail("cancel");
                }
                dismiss();
            }
        });
    
        inflate.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap avatar = mClipImageLayout.clip(mCropSize, mCropSize);
                if (mOnImageSelectListener != null) {
                    mOnImageSelectListener.onSelectSuccess(writeToFile(avatar));
                }
                dismiss();
            }
        });
        
        return inflate;
    }
    
    private String writeToFile(@NonNull Bitmap bitmap) {
        File avatarDir = mActivity.getExternalFilesDir("avatar");
        if (avatarDir != null) {
            deleteAvatarFile(avatarDir);
        }
        File file = new File(avatarDir, "avatar_" + System.currentTimeMillis() + ".jpg");
        
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
    
    private void deleteAvatarFile(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            //遍历删除文件夹下的所有文件
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith("avatar_")) {
                    file.delete();
                }
            }
        }
    }
    
    public void setOnImageSelectListener(Picker.OnImageSelectListener onImageSelectListener) {
        mOnImageSelectListener = onImageSelectListener;
    }
    
    
}
