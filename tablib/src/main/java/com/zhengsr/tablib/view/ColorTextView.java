package com.zhengsr.tablib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.zhengsr.tablib.R;




/**
 * @author by  zhengshaorui on 2019/10/8
 * csdn: http://blog.csdn.net/u011418943
 * Describe: 颜色渐变类
 */

public class ColorTextView extends AppCompatTextView {
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
    public ColorTextView(Context context) {
        this(context,null);
    }

    public ColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);



    }

    public ColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorTextView);
        mDefaultColor = ta.getColor(R.styleable.ColorTextView_colortext_default_color, Color.GRAY);
        mChangeColor = ta.getColor(R.styleable.ColorTextView_colortext_change_color,Color.WHITE);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mPaint.setTextSize(getTextSize());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

    }

    /**
     * 公布出去，可以手动设置颜色和字体大小
     * @param defaultColor
     * @param changeColor
     * @param textsize
     */
    public void setCusTextColor(int defaultColor, int changeColor, int textsize){
        mDefaultColor = defaultColor;
        mChangeColor = changeColor;
        mPaint.setTextSize(textsize);
        invalidate();
    }
    private float mProgress = 0;
    public void setprogress(float progress,int decection) {
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
            drawText(canvas,0,mWidth,getCurrentTextColor());
            isUseUserColor = false;
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
        canvas.clipRect(start,0,end,getHeight());
        String text = getText().toString();

        //绘制颜色居中
        float textWidth = mPaint.measureText(text);
        float x = (mWidth - textWidth)/2;
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float dy = (metrics.descent+metrics.ascent)/2;
        float ty = mHeight/2 - dy;

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
