package cn.tangjunwei.imagelibrary.album.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.crop.ClipBorderView;
import cn.tangjunwei.imagelibrary.crop.ClipImageLayout;
import cn.tangjunwei.imagelibrary.crop.ClipZoomImageView;

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
    
    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }
    
    public static CropDialogFragment newInstance(String path) {
        
        Bundle args = new Bundle();
        args.putString("path", path);
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
        
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPath = arguments.getString("path");
        }
    }
    
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //mActivity.getWindow().
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
        
        mImageLoader.loadImage(this, mPath, mZoomImageView);
        
        
        return inflate;
    }
    
    @Override
    public void onDismiss(DialogInterface dialog) {
        System.out.println("onDetach");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onDismiss(dialog);
    }
    
    
}
