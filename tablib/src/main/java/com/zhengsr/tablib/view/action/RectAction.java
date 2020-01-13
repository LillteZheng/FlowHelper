package com.zhengsr.tablib.view.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
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
        View child = parentView.getChildAt(0);
        if (child != null && mRect.isEmpty()) {
            float l = parentView.getPaddingLeft() + mMarginLeft;
            float t = parentView.getPaddingTop() + child.getMeasuredHeight() - mTabHeight - mMarginBottom;
            float r = parentView.getPaddingLeft() + child.getMeasuredWidth() - mMarginRight;
            float b = t + mTabHeight;
            if (mTabWidth != -1) {
                l += (child.getMeasuredWidth() - mTabWidth) / 2;
                r = mTabWidth + l;
            }
            mRect.set(l, t, r, b);
        }
        parentView.postInvalidate();
    }

    @Override
    protected void valueChange(TabValue value) {
        super.valueChange(value);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }

}
