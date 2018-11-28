package cn.tangjunwei.imagelibrary.album;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.core.ImagePicker;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/15.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class PreviewDialogFragment extends DialogFragment {
    private FragmentActivity mActivity;
    private String mPath;
    
    public static PreviewDialogFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString("path", path);
        PreviewDialogFragment fragment = new PreviewDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        } else {
            throw new ClassCastException(context.toString() + "must extends FragmentActivity!");
        }
        
    }
    
    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeCropDialog);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPath = arguments.getString("path");
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
    
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View inflate = inflater.inflate(R.layout.fragment_preview, container, false);
        PhotoView photoView = inflate.findViewById(R.id.photo_view);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPath, options);
    
        {   // 设置一下恰当的 缩放系数
            float maxScale = 3f;
            float mediumScale = 1.5f;
            float minScale = 1f;
            float imageWidth = options.outWidth;
            float imageHeight = options.outHeight;
            float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            float screenRadio = screenWidth / screenHeight;
            float imageRadio = imageWidth / imageHeight;
            if (imageWidth < screenWidth && imageHeight < screenHeight) {
                minScale = imageWidth / screenWidth;
            }
            if (imageRadio > screenRadio + 0.1) {
                mediumScale = screenHeight / (screenWidth / imageRadio);
                if (imageHeight <= screenHeight) {
                    maxScale = 1.5f * mediumScale;
                } else {
                    maxScale = 2f * mediumScale;
                }
            } else if (imageRadio < screenRadio - 0.1) {
                mediumScale = screenWidth / (screenHeight * imageRadio);
                if (imageWidth <= screenWidth) {
                    maxScale = 1.5f * mediumScale;
                } else {
                    maxScale = 2f * mediumScale;
                }
            }
            photoView.setMaximumScale(maxScale);
            photoView.setMediumScale(mediumScale);
            photoView.setMinimumScale(minScale);
        }
        
        
       /* float imageRadio = 1f * options.outWidth / options.outHeight;
        float mediumScale = 1f * 1920 / 1080 * imageRadio;
        photoView.setMediumScale(mediumScale);
        photoView.setMaximumScale(mediumScale * 2);
        if (options.outWidth < 1080) {
            photoView.setMinimumScale(1f * options.outWidth / 1080);
        } else {
            photoView.setMinimumScale(1f);
        }*/
        
        ImageLoader imageLoader = ImagePicker.getInstance().getImageLoader();
        
        imageLoader.loadCropImage(this, mPath, photoView);
        return inflate;
    }
    
    
    private void closeSelf() {
        dismissAllowingStateLoss();
    }
    
}
