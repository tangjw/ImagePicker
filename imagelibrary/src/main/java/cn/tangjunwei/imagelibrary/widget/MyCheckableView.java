package cn.tangjunwei.imagelibrary.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import java.util.Arrays;

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
    
    
    private Paint mPaintchecked;
    private Paint mPaintUnchecked;
    private Path mTickPath;
    private Point mCenterPoint;
    private Point[] mTickPoints;
    private float mTickLeftLength;
    private float mTickRightLength;
    private int mBorderWidth;
    private int mTickWidth;
    private int mTickColor;
    
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
    
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCheckableView);
        int colorChecked = typedArray.getColor(R.styleable.MyCheckableView_color_checked, Color.BLUE);
        int colorUnChecked = typedArray.getColor(R.styleable.MyCheckableView_color_unchecked, Color.WHITE);
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.MyCheckableView_circle_width, dp2px(1));
        System.out.println(mBorderWidth);
        mTickWidth = typedArray.getDimensionPixelSize(R.styleable.MyCheckableView_tick_width, dp2px(1));
        mTickColor = typedArray.getDimensionPixelSize(R.styleable.MyCheckableView_tick_color, Color.WHITE);
        typedArray.recycle();
        
        mPaintchecked = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintchecked.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintchecked.setStrokeCap(Paint.Cap.ROUND);
        mPaintchecked.setColor(colorChecked);
        
        mPaintUnchecked = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintUnchecked.setStyle(Paint.Style.STROKE);
        mPaintUnchecked.setColor(colorUnChecked);
        mPaintUnchecked.setStrokeCap(Paint.Cap.ROUND);
        mPaintUnchecked.setStrokeWidth(mBorderWidth);
        
        mTickPath = new Path();
        mCenterPoint = new Point();
        mTickPoints = new Point[]{new Point(), new Point(), new Point()};
        
        setOnClickListener(this);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getWidth();
        int height = getHeight();
        mCenterPoint.x = width / 2;
        mCenterPoint.y = height / 2;
        int w = width - getPaddingLeft() - getPaddingRight();
        int h = height - getPaddingTop() - getPaddingBottom();
        mTickPoints[0].x = Math.round((float) w / 12 * 3) + getPaddingLeft();
        mTickPoints[0].y = Math.round((float) h / 12 * 6) + getPaddingTop();
        mTickPoints[1].x = Math.round((float) w / 12 * 5) + getPaddingLeft();
        mTickPoints[1].y = Math.round((float) h / 12 * 8) + getPaddingTop();
        mTickPoints[2].x = Math.round((float) w / 12 * 9) + getPaddingLeft();
        mTickPoints[2].y = Math.round((float) h / 12 * 4) + getPaddingTop();
        System.out.println(Arrays.toString(mTickPoints));
        //计算对号'✓ '左右两边的距离
        mTickLeftLength = (float) Math.sqrt(Math.pow(mTickPoints[1].x - mTickPoints[0].x, 2) +
                Math.pow(mTickPoints[1].y - mTickPoints[0].y, 2));
        mTickRightLength = (float) Math.sqrt(Math.pow(mTickPoints[2].x - mTickPoints[1].x, 2) +
                Math.pow(mTickPoints[2].y - mTickPoints[1].y, 2));
        // mTickPaint.setStrokeWidth(mStrokeWidth);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        drawUnchecked(canvas);
    }
    
    /**
     * 绘制未选各种时的状态
     */
    private void drawUnchecked(Canvas canvas) {
        mPaintUnchecked.setStrokeWidth(mBorderWidth);
        int radius = mCenterPoint.x - getPaddingLeft();
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaintUnchecked);
        
        mPaintUnchecked.setStrokeWidth(mTickWidth);
        mTickPath.reset();
        mTickPath.moveTo(mTickPoints[0].x, mTickPoints[0].y);
        mTickPath.lineTo(mTickPoints[1].x, mTickPoints[1].y);
        mTickPath.moveTo(mTickPoints[1].x, mTickPoints[1].y);
        mTickPath.lineTo(mTickPoints[2].x, mTickPoints[2].y);
        canvas.drawPath(mTickPath, mPaintUnchecked);
    }
    
    @Override
    public void onClick(View v) {
        
    }
    
    @Override
    public boolean isChecked() {
        return false;
    }
    
    @Override
    public void setChecked(boolean checked) {
        
    }
    
    @Override
    public void toggle() {
        
    }
    
    private int dp2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
