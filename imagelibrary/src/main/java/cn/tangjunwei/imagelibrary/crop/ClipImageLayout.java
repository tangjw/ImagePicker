package cn.tangjunwei.imagelibrary.crop;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.tangjunwei.imagelibrary.R;


/**
 * 原址鸿洋博客 http://blog.csdn.net/lmj623565791/article/details/39761281
 */
public class ClipImageLayout extends RelativeLayout {
    
    private ClipZoomImageView mZoomImageView;
    private ClipBorderView mClipBorderView;
    /**
     * default 20px
     */
    private int mHorizontalPadding = 20;
    
    public ClipImageLayout(Context context) {
        this(context, null);
    }
    
    public ClipImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ClipImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    public ClipZoomImageView getZoomImageView() {
        return mZoomImageView;
    }
    
    public ClipBorderView getClipBorderView() {
        return mClipBorderView;
    }
    
    public Bitmap clip(int outImageX, int outImageY) {
        return mZoomImageView.clip(outImageX, outImageY);
    }
    
    private void init(Context context, AttributeSet attrs) {
        
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClipImageLayout);
        mHorizontalPadding = typedArray.getDimensionPixelOffset(R.styleable.ClipImageLayout_padding_horizontal, mHorizontalPadding);
        int borderWidth = typedArray.getDimensionPixelOffset(R.styleable.ClipImageLayout_border_width, 1);
        typedArray.recycle();
        mZoomImageView = new ClipZoomImageView(context);
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipBorderView = new ClipBorderView(context);
        mClipBorderView.setHorizontalPadding(mHorizontalPadding);
        mClipBorderView.setBorderWidth(borderWidth);
        addView(mZoomImageView,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mClipBorderView,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        
    }
    
}
