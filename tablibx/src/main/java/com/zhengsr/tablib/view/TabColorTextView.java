package com.zhengsr.tablib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.zhengsr.tablib.R;


/**
 * @author by  zhengshaorui on 2019/10/8
 * csdn: http://blog.csdn.net/u011418943
 * Describe: 颜色渐变类
 */

public class TabColorTextView extends AppCompatTextView {
    private static final String TAG = "ColorTextView";
    /**
     * const
     */
    public static final int DEC_LEFT = 1;
    public static final int DEC_RIGHT = 2;

    private Paint mPaint;
    /**
     * attrs
     */
    private int mWidth,mHeight;
    private int mDefaultColor ;
    private int mChangeColor ;
    private int mDecection = DEC_LEFT;

    private boolean isUseUserColor = false;
    public TabColorTextView(Context context) {
        this(context,null);
    }

    public TabColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

        setIncludeFontPadding(false);

    }

    public TabColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabColorTextView);
        mDefaultColor = ta.getColor(R.styleable.TabColorTextView_colortext_default_color, Color.GRAY);
        mChangeColor = ta.getColor(R.styleable.TabColorTextView_colortext_change_color,Color.WHITE);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(getTextSize());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
      //  mWidth = w;
      //  mHeight = h;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /**
     * 公布出去，可以手动设置颜色和字体大小
     * @param defaultColor
     * @param changeColor
     */
    public void setCusTextColor(int defaultColor, int changeColor){
        mDefaultColor = defaultColor;
        mChangeColor = changeColor;
        isUseUserColor = false;
        invalidate();
    }
    private float mProgress = 0;
    public void setprogress(float progress,int decection) {
        isUseUserColor = false;
        mDecection = decection;
        mProgress = progress;
        invalidate();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        isUseUserColor = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isUseUserColor){
          super.onDraw(canvas);
        }else {
            if (mDecection == DEC_RIGHT) {
                //绘制一遍黑色
                drawText(canvas, 0, mWidth, mDefaultColor);
                // 再绘制一遍其他颜色
                drawText(canvas, (int) ((1 - mProgress) * mWidth), mWidth, mChangeColor);
            } else {
                //绘制一遍黑色
                drawText(canvas, 0, mWidth, mDefaultColor);
                // 再绘制一遍其他颜色
                drawText(canvas, 0, (int) (mProgress * mWidth), mChangeColor);
            }
        }
    }
    private void drawText(Canvas canvas,int start,int end,int color){
        mPaint.setColor(color);
        canvas.save();
        canvas.clipRect(start,0,end,mHeight);
        String text = getText().toString();

        int t = getPaddingTop();
        int b = getPaddingBottom();
        float x;
        float ty;
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float dy = (metrics.descent + metrics.ascent) / 2;
        float textWidth = mPaint.measureText(text);
        x = (mWidth - textWidth) / 2;
        //根据padding来
        if (getGravity() == Gravity.CENTER_VERTICAL || getGravity() == Gravity.CENTER){
            t = 0;
            b = 0;
        }
        ty = (mHeight+ t - b )*1.0f / 2 - dy;


        canvas.drawText(text,x,ty,mPaint);
        canvas.restore();
    }

    public int getChangeColor() {
        return mChangeColor;
    }

    public int getDefaultColor() {
        return mDefaultColor;
    }
}
