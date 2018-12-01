package cn.tangjunwei.imagelibrary.album.adapter;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;

/**
 * 预览 viewpager adapter
 * <p>
 * Created by tangjunwei on 2018/11/30.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class PreviewPagerAdapter extends PagerAdapter {
    
    private final float mScreenWidth;
    private final float mScreenHeight;
    private final float mScreenRatio;
    private final BitmapFactory.Options mOptions;
    private int mMaxCount;
    private List<ImageBean> mList;
    private ImageLoader mImageLoader;
    private SparseArray<ImageBean> mSparseArray;
    private OnImageCheckedChangeListener mListener;
    private OnPhotoViewClickListener mPhotoViewClickListener;
    
    public PreviewPagerAdapter(int maxCount, List<ImageBean> list, ImageLoader imageLoader, SparseArray<ImageBean> sparseArray) {
        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        mScreenRatio = mScreenWidth / mScreenHeight;
        
        mOptions = new BitmapFactory.Options();
        mOptions.inJustDecodeBounds = true;
        
        mMaxCount = maxCount;
        
        mList = list;
        
        mImageLoader = imageLoader;
        mSparseArray = sparseArray;
    }
    
    public void setPhotoViewClickListener(OnPhotoViewClickListener photoViewClickListener) {
        mPhotoViewClickListener = photoViewClickListener;
    }
    
    public void replaceData(List<ImageBean> list, SparseArray<ImageBean> sparseArray) {
        mList = list;
        mSparseArray = sparseArray;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mList.size();
    }
    
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final ImageBean imageBean = mList.get(position);
        final PhotoView photoView = new PhotoView(container.getContext());
        BitmapFactory.decodeFile(imageBean.getPath(), mOptions);
        
        // 设置一下恰当的 缩放系数
        float maxScale = 3f;
        float mediumScale = 1.5f;
        float minScale = 1f;
        float imageWidth = mOptions.outWidth;
        float imageHeight = mOptions.outHeight;
        float mScreenRadio = mScreenWidth / mScreenHeight;
        
        float imageRadio = imageWidth / imageHeight;
        if (imageBean.getDirection() / 90 % 2 == 1) {
            imageRadio = imageHeight / imageWidth;
        }
        
        if (imageWidth < mScreenWidth && imageHeight < mScreenHeight) {
            minScale = imageWidth / mScreenWidth;
        }
        if (imageRadio > mScreenRadio + 0.05) {
            mediumScale = mScreenHeight / (mScreenWidth / imageRadio);
            if (imageHeight <= mScreenHeight) {
                maxScale = 1.5f * mediumScale;
            } else {
                maxScale = 2f * mediumScale;
            }
        } else if (imageRadio < mScreenRadio - 0.05) {
            mediumScale = mScreenWidth / (mScreenHeight * imageRadio);
            if (imageWidth <= mScreenWidth) {
                maxScale = 1.5f * mediumScale;
            } else {
                maxScale = 2f * mediumScale;
            }
        }
        photoView.setMaximumScale(maxScale);
        photoView.setMediumScale(mediumScale);
        photoView.setMinimumScale(minScale);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoViewClickListener != null) {
                    mPhotoViewClickListener.OnPhotoViewClick(photoView);
                }
            }
        });
        mImageLoader.loadPreviewImage(container.getContext(), imageBean.getPath(), photoView, mOptions.outMimeType, (int) imageWidth, (int) imageHeight);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    
    public void setListener(OnImageCheckedChangeListener listener) {
        mListener = listener;
    }
    
    public interface OnImageCheckedChangeListener {
        void onCheckedImageChange(SparseArray<ImageBean> sparseArray);
    }
    
    public interface OnPhotoViewClickListener {
        void OnPhotoViewClick(PhotoView photoView);
    }
    
}
