package cn.tangjunwei.imagelibrary.album;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
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
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.adapter.PreviewPagerAdapter;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;
import cn.tangjunwei.imagelibrary.core.ImagePicker;
import cn.tangjunwei.imagelibrary.widget.HackyViewPager;
import cn.tangjunwei.imagelibrary.widget.MyCheckableView;
import cn.tangjunwei.imagelibrary.widget.ZoomOutPageTransformer;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/15.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class PreviewDialogFragment extends DialogFragment {
    private AlbumActivity mActivity;
    private int mPreviewType;
    private FrameLayout mToolbar;
    private boolean isFading;
    private boolean isToolbarShow = true;
    
    private int mCurrentPageIndex;
    private MyCheckableView mCheckableView;
    private List<ImageBean> mList;
    private PreviewPagerAdapter mAdapter;
    private SparseArray<ImageBean> mSelectedImageArray;
    
    public static PreviewDialogFragment newInstance(int type, int position) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("position", position);
        PreviewDialogFragment fragment = new PreviewDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (AlbumActivity) context;
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
            mPreviewType = arguments.getInt("type", 0);
            mCurrentPageIndex = arguments.getInt("position", 0);
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
        mSelectedImageArray = mActivity.mSelectedImageArray;
        
        View inflate = inflater.inflate(R.layout.fragment_preview, container, false);
    
        mToolbar = inflate.findViewById(R.id.toolbar);
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        mToolbar.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.toolbarHeight) + statusBarHeight));
        mToolbar.setPadding(0, statusBarHeight, 0, 0);
    
        mCheckableView = inflate.findViewById(R.id.cv_index);
    
        mCheckableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageBean currentImageBean = mList.get(mCurrentPageIndex);
                if (currentImageBean.getIndex() > 0) {    // 已选中
                    mCheckableView.setChecked(0);   // 取消选中
                    mSelectedImageArray.remove(currentImageBean.getId());    // 在SparseArray中移除取消选中的
                    for (int i = 0; i < mSelectedImageArray.size(); i++) { // 更新SparseArray中的（索引-1）
                        ImageBean imageBean = mSelectedImageArray.valueAt(i);
                        if (imageBean.getIndex() > currentImageBean.getIndex()) {
                            imageBean.setIndex(imageBean.getIndex() - 1);
                        }
                    }
                    currentImageBean.setIndex(0);   // 在list中设置取消选中
                } else {    // 未选中
                    if (mSelectedImageArray.size() < 9) {
    
                        mCheckableView.setIndex(mSelectedImageArray.size() + 1);
                        mCheckableView.switchState();
                        currentImageBean.setIndex(mSelectedImageArray.size() + 1);
                        mSelectedImageArray.append(currentImageBean.getId(), currentImageBean);
                    } else {
                        Toast.makeText(mActivity.getApplicationContext(), "最多选9张", Toast.LENGTH_SHORT).show();
                    }
                }
                /*{   // 检测数据
                    for (int i = 0; i < mSelectedImageArray.size(); i++) {
                        System.out.println("spa: " + mSelectedImageArray.valueAt(i).getIndex());
                    }
                    for (ImageBean imageBean : mList) {
                        System.out.println("mList after: " + imageBean.getIndex());
                    }
                }*/
            }
        });
    
    
        HackyViewPager viewPager = inflate.findViewById(R.id.view_pager);
    
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPageIndex = position;
                setIndicator(mCurrentPageIndex);
            }
        });
    
        ImageLoader imageLoader = ImagePicker.getInstance().getImageLoader();
        if (mPreviewType == 0) {
            ImageBean[] arrays = new ImageBean[mSelectedImageArray.size()];
            for (int i = 0; i < mSelectedImageArray.size(); i++) {
                ImageBean imageBean = mSelectedImageArray.valueAt(i);
                arrays[imageBean.getIndex() - 1] = imageBean;
            }
            mList = Arrays.asList(arrays);
        }
    
        if (mPreviewType == 1) {
            mList = mActivity.mCurrentAlbumImageList;
        }
        
        
        mAdapter = new PreviewPagerAdapter(0, mList, imageLoader, mSelectedImageArray);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(mAdapter);
    
        if (mPreviewType == 1) {
            viewPager.setCurrentItem(mCurrentPageIndex, false);
        }
        
        setIndicator(mCurrentPageIndex);
    
        mAdapter.setPhotoViewClickListener(new PreviewPagerAdapter.OnPhotoViewClickListener() {
            @Override
            public void OnPhotoViewClick(PhotoView photoView) {
                if (!isFading) {
                    if (isToolbarShow) {
                        fadeOutAnimation();
                    } else {
                        fadeInAnimation();
                    }
                }
            }
        });
    
        inflate.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSelf();
            }
        });
        
        return inflate;
    }
    
    private void setIndicator(int currentPageIndex) {
        mCheckableView.setChecked(mList.get(currentPageIndex).getIndex());
    }
    
    private void closeSelf() {
        mActivity.refreshCheckedImage();
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
        mActivity.refreshCheckedImage();
        mToolbar.clearAnimation();
        super.onDestroy();
    }
    
}
