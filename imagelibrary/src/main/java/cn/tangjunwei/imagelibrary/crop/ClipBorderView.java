package cn.tangjunwei.imagelibrary.crop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 鸿洋博客: http://blog.csdn.net/lmj623565791/article/details/39761281
 * hencoder: https://hencoder.com/ui-1-2/
 * <p>
 * Created by tangjunwei on 2018/11/9.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ClipBorderView extends View {
    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding;
    /**
     * default 2px
     */
    private int mBorderWidth = 2;
    private PorterDuffXfermode mXfermode;
    
    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
    }
    
    private Paint mPaint;
    private Paint mPaintBorder;
    
    public ClipBorderView(Context context) {
        this(context, null);
    }
    
    public ClipBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ClipBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.argb(200, 0, 0, 0));
        mPaint.setStyle(Style.FILL);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        
        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder.setColor(Color.WHITE);
        mPaintBorder.setStyle(Style.STROKE);
        mPaintBorder.setStrokeWidth(mBorderWidth);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("onDraw");
        
        // 计算矩形区域的宽度
        int width = getWidth() - 2 * mHorizontalPadding;
        // 计算距离屏幕垂直边界 的边距
        int verticalPadding = (getHeight() - width) / 2;
        int saveLayer = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        mPaint.setXfermode(mXfermode);
        canvas.drawRect(
                mHorizontalPadding,
                verticalPadding,
                getWidth() - mHorizontalPadding,
                getHeight() - verticalPadding, mPaint);
        mPaint.setXfermode(null);
        
       
        canvas.drawRect(
                mHorizontalPadding,
                verticalPadding,
                getWidth() - mHorizontalPadding,
                getHeight() - verticalPadding, mPaintBorder);
    
        canvas.restoreToCount(saveLayer);
    
    
    }
    
    public void setHorizontalPadding(int padding) {
        mHorizontalPadding = padding;
    }
    
}
