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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

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
    private int mDirection;
    private FrameLayout mToolbar;
    private boolean isFading;
    private boolean isToolbarShow;
    
    public static PreviewDialogFragment newInstance(String path, int direction) {
        Bundle args = new Bundle();
        args.putString("path", path);
        args.putInt("direction", direction);
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
            mDirection = arguments.getInt("direction", 0);
        }
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE/*| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR*/);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View inflate = inflater.inflate(R.layout.fragment_preview, container, false);
    
        mToolbar = inflate.findViewById(R.id.toolbar);
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        mToolbar.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.toolbarHeight) + statusBarHeight));
        mToolbar.setPadding(0, statusBarHeight, 0, 0);
    
        inflate.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSelf();
            }
        });
        
        PhotoView photoView = inflate.findViewById(R.id.photo_view);
    
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFading) {
                    if (isToolbarShow) {
                        fadeOutAnimation();
                    } else {
                        fadeInAnimation();
                    }
                }
            }
        });
    
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        mToolbar.startAnimation(alphaAnimation);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPath, options);
    
        // 设置一下恰当的 缩放系数
        float maxScale = 3f;
        float mediumScale = 1.5f;
        float minScale = 1f;
        float imageWidth = options.outWidth;
        float imageHeight = options.outHeight;
        float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        float screenRadio = screenWidth / screenHeight;
    
        float imageRadio = imageWidth / imageHeight;
        if (mDirection / 90 % 2 == 1) {
            imageRadio = imageHeight / imageWidth;
        }
    
        if (imageWidth < screenWidth && imageHeight < screenHeight) {
            minScale = imageWidth / screenWidth;
        }
        if (imageRadio > screenRadio + 0.05) {
            mediumScale = screenHeight / (screenWidth / imageRadio);
            if (imageHeight <= screenHeight) {
                maxScale = 1.5f * mediumScale;
            } else {
                maxScale = 2f * mediumScale;
            }
        } else if (imageRadio < screenRadio - 0.05) {
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
        
        ImageLoader imageLoader = ImagePicker.getInstance().getImageLoader();
        
        imageLoader.loadCropImage(this, mPath, photoView);
        return inflate;
    }
    
    
    private void closeSelf() {
        dismissAllowingStateLoss();
    }
    
    private void fadeOutAnimation() {
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                //mToolbar.setVisibility(View.GONE);
                isFading = false;
                isToolbarShow = false;
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
        });
        mToolbar.startAnimation(alphaAnimation);
        isFading = true;
    }
    
    private void fadeInAnimation() {
        Animation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                //mToolbar.setVisibility(View.GONE);
                isFading = false;
                isToolbarShow = true;
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
        });
        mToolbar.startAnimation(alphaAnimation);
        isFading = true;
    }
    
    @Override
    public void onDestroy() {
        mToolbar.clearAnimation();
        super.onDestroy();
    }
}
