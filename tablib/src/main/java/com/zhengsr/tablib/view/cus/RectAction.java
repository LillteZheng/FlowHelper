package com.zhengsr.tablib.view.cus;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.zhengsr.tablib.R;
import com.zhengsr.tablib.view.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 绘制矩形
 */
public class RectAction extends BaseAction {
    private static final String TAG = "RectAction";
    private float mValue = 0;
    private float mOffset;
    @Override
    public void config(TabFlowLayout parentView) {
        super.config(parentView);
        View child = parentView.getChildAt(0);
        if (child != null) {
            float l = parentView.getPaddingLeft();
            float t = parentView.getPaddingTop() + child.getMeasuredHeight() - mTabHeight;
            float r = child.getMeasuredWidth();
            float b = child.getMeasuredHeight();
            if (mTabWidth != -1){
                l += (child.getMeasuredWidth() - mTabWidth)/2;
                r = mTabWidth+l ;
            }
            mRect.set(l,t,r,b);

        }
        parentView.postInvalidate();
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(mRect,mPaint);
    }

}
