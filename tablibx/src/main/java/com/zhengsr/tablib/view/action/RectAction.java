package com.zhengsr.tablib.view.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.flow.base.AbsFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 绘制矩形
 */
public class RectAction extends BaseAction {
    private static final String TAG = "RectAction";

    @Override
    public void config(AbsFlowLayout parentView) {
        super.config(parentView);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        View child = parentView.getChildAt(mCurrentIndex);
        if (child != null && mTabRect.isEmpty()) {

            float l =0;
            float t=0;
            float r=0;
            float b=0;
            if (isLeftAction()){
                l = child.getLeft() + mTabBean.tabMarginLeft;
                t = child.getTop() + mTabBean.tabMarginTop;
                r = l + mTabBean.tabWidth ;
                b = t + child.getBottom() - mTabBean.tabMarginBottom;
                if (mTabBean.tabHeight != -1){
                    t += (child.getMeasuredHeight() - mTabBean.tabHeight)/2;
                    b = t + mTabBean.tabHeight;
                }
            }else if (isRightAction()){
                l = child.getRight() - mTabBean.tabMarginRight;
                t = child.getTop() - mTabBean.tabMarginTop;
                r = l - mTabBean.tabWidth;
                b = t + mTabBean.tabHeight;
                if (mTabBean.tabHeight != -1){
                    t += (child.getMeasuredHeight() - mTabBean.tabHeight)/2;
                    b = t + mTabBean.tabHeight;
                }
            }else{
                l =  mTabBean.tabMarginLeft + child.getLeft();
                t = mTabBean.tabMarginTop + child.getBottom() - mTabBean.tabHeight - mTabBean.tabMarginBottom;
                r =  child.getRight() - mTabBean.tabMarginRight;
                b = t + mTabBean.tabHeight;
                if (mTabBean.tabWidth != -1) {
                    l += (child.getMeasuredWidth() - mTabBean.tabWidth) / 2 ;
                    r = mTabBean.tabWidth + l;
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
                mTabRect.right = mTabBean.tabWidth + mTabRect.left;
            }else{
                mTabRect.left = value.right;
                mTabRect.right = mTabRect.left - mTabBean.tabWidth;
            }

        }else{
            super.valueChange(value);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mTabBean.tabRoundSize != -1) {
            canvas.drawRoundRect(mTabRect, mTabBean.tabRoundSize, mTabBean.tabRoundSize, mPaint);
        } else {
            canvas.drawRect(mTabRect, mPaint);
        }
    }

}
