package cn.tangjunwei.imagelibrary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import androidx.annotation.Nullable;
import cn.tangjunwei.imagelibrary.R;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/25.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class MyCheckableView extends View implements View.OnClickListener, Checkable {
    
    private static final String KEY_INSTANCE_STATE = "InstanceState";
    private Paint mPaintChecked;
    private Paint mPaintUnchecked;
    private Path mTickPath;
    private Point mCenterPoint;
    private Point[] mTickPoints;
    
    private boolean mChecked;
    
    private float mAnimatedValue = 1;
    private Paint mPaintText;
    private int mTextSize;
    private String mStrText = "1";
    private int mIndex = 1;
    private int mBorderWidth;
    private float mTextDx;
    private float mTextDy;
    
    public MyCheckableView(Context context) {
        this(context, null);
    }
    
    public MyCheckableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public MyCheckableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        init(context, attrs);
    }
    
    
    public int getIndex() {
        return mIndex;
    }
    
    public void setIndex(int index) {
        mIndex = index;
    }
    
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCheckableView);
        int colorChecked = typedArray.getColor(R.styleable.MyCheckableView_color_checked, Color.BLUE);
        int colorUnChecked = typedArray.getColor(R.styleable.MyCheckableView_color_unchecked, Color.WHITE);
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.MyCheckableView_circle_width, 3);
        typedArray.recycle();
    
        mPaintChecked = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintChecked.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintChecked.setColor(colorChecked);
        
        
        mPaintUnchecked = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintUnchecked.setStyle(Paint.Style.STROKE);
        mPaintUnchecked.setColor(colorUnChecked);
        mPaintUnchecked.setStrokeCap(Paint.Cap.ROUND);
        mPaintUnchecked.setStrokeWidth(mBorderWidth);
    
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.WHITE);
        
        
        mTickPath = new Path();
        mCenterPoint = new Point();
        mTickPoints = new Point[]{new Point(), new Point(), new Point()};
    
        //setOnClickListener(this);
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(KEY_INSTANCE_STATE, isChecked());
        return bundle;
    }
    
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            boolean isChecked = bundle.getBoolean(KEY_INSTANCE_STATE);
            setChecked(isChecked);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getWidth();
        int height = getHeight();
        mCenterPoint.x = width / 2;
        mCenterPoint.y = height / 2;
        int w = width - getPaddingStart() - getPaddingEnd();
        
        int h = height - getPaddingTop() - getPaddingBottom();
        mTickPoints[0].x = Math.round((float) w / 12 * 3) + getPaddingStart();
        mTickPoints[0].y = Math.round((float) h / 12 * 6) + getPaddingTop();
        mTickPoints[1].x = Math.round((float) w / 12 * 5) + getPaddingStart();
        mTickPoints[1].y = Math.round((float) h / 12 * 8) + getPaddingTop();
        mTickPoints[2].x = Math.round((float) w / 12 * 9) + getPaddingStart();
        mTickPoints[2].y = Math.round((float) h / 12 * 4) + getPaddingTop();
    
        mTextSize = w * 3 / 5;
        mPaintText.setTextSize(mTextSize);
        mTextDx = mPaintText.measureText(String.valueOf(mIndex)) / 2;
        Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
        mTextDy = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (!isChecked()) {
            drawUnchecked(canvas);
        } else {
            drawChecked(canvas);
        }
    }
    
    private void drawChecked(Canvas canvas) {
        float radius = (mCenterPoint.x - getPaddingStart()) * mAnimatedValue;
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaintChecked);
        mPaintText.setTextSize(mTextSize * mAnimatedValue);
        canvas.drawText(mIndex + "", mCenterPoint.x - mTextDx, mCenterPoint.y + mTextDy, mPaintText);
    }
    
    /**
     * 绘制未选各种时的状态
     */
    private void drawUnchecked(Canvas canvas) {
        float radius = mCenterPoint.x - getPaddingStart() - mBorderWidth;
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaintUnchecked);
        mTickPath.reset();
        mTickPath.moveTo(mTickPoints[0].x, mTickPoints[0].y);
        mTickPath.lineTo(mTickPoints[1].x, mTickPoints[1].y);
        mTickPath.moveTo(mTickPoints[1].x, mTickPoints[1].y);
        mTickPath.lineTo(mTickPoints[2].x, mTickPoints[2].y);
        canvas.drawPath(mTickPath, mPaintUnchecked);
    }
    
    @Override
    public void onClick(View v) {
        switchState();
    }
    
    public void switchState() {
        toggle();
        if (mChecked) {
            mAnimatedValue = 0;
            startCheckedAnimation();
        }
    }
    
    private void startCheckedAnimation() {
        ValueAnimator floorAnimator = ValueAnimator.ofFloat(0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.05f, 1.0f, 0.95f, 0.9f, 0.94f, 0.97f, 0.99f, 1.0f);
        floorAnimator.setDuration(400);
        floorAnimator.setInterpolator(new LinearInterpolator());
        floorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        floorAnimator.start();
    }
    
    @Override
    public boolean isChecked() {
        return mChecked;
    }
    
    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        mAnimatedValue = 1f;
        invalidate();
        if (mListener != null) {
            mListener.onCheckedChanged(this, mChecked);
        }
    }
    
    public void setChecked(int index) {
        mIndex = index;
        setChecked(index > 0);
    }
    
    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
    
    private OnCheckedChangeListener mListener;
    
    public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
        this.mListener = l;
    }
    
    public interface OnCheckedChangeListener {
        void onCheckedChanged(MyCheckableView checkableView, boolean isChecked);
    }
}
