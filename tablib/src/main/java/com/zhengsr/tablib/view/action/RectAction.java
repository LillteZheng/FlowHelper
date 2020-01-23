package com.zhengsr.tablib.view.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 绘制矩形
 */
public class RectAction extends BaseAction {
    private static final String TAG = "RectAction";

    @Override
    public void config(TabFlowLayout parentView) {
        super.config(parentView);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        View child = parentView.getChildAt(mCurrentIndex);
        if (child != null && mTabRect.isEmpty()) {

            float l =0;
            float t=0;
            float r=0;
            float b=0;
            if (isLeftAction()){
                l = child.getLeft() + mMarginLeft;
                t = child.getTop() + mMarginTop;
                r = l + mTabWidth ;
                b = t + child.getBottom() - mMarginBottom;
                if (mTabHeight != -1){
                    t += (child.getMeasuredHeight() - mTabHeight)/2;
                    b = t + mTabHeight;
                }
            }else if (isRightAction()){
                l = child.getRight() - mMarginRight;
                t = child.getTop() - mMarginTop;
                r = l - mTabWidth;
                b = t + mTabHeight;
                if (mTabHeight != -1){
                    t += (child.getMeasuredHeight() - mTabHeight)/2;
                    b = t + mTabHeight;
                }
            }else{
                l =  mMarginLeft + child.getLeft();
                t = mMarginTop + child.getBottom() - mTabHeight - mMarginBottom;
                r =  child.getRight() - mMarginRight;
                b = t + mTabHeight;
                if (mTabWidth != -1) {
                    l += (child.getMeasuredWidth() - mTabWidth) / 2 ;
                    r = mTabWidth + l;
                }
            }

            mTabRect.set(l, t, r, b);
        }
        parentView.postInvalidate();
    }

    @Override
    protected void valueChange(TabValue value) {
       // super.valueChange(value);
        if (isVertical()){
            mTabRect.top = value.top ;
            mTabRect.bottom = value.bottom ;
            if (isLeftAction()) {
                mTabRect.left = value.left;
                mTabRect.right = mTabWidth + mTabRect.left;
            }else{
                mTabRect.left = value.right;
                mTabRect.right = mTabRect.left - mTabWidth;
            }

        }else{
            super.valueChange(value);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(mTabRect, mPaint);
    }

}
