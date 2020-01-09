package com.zhengsr.tablib.view.action;

import android.graphics.Canvas;
import android.graphics.Path;
import android.view.View;

import com.zhengsr.tablib.bean.TabTypeValue;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class TriAction extends BaseAction {
    private static final String TAG = "TriAction";
    private Path mPath;

    @Override
    public void config(TabFlowLayout parentView) {
        super.config(parentView);
        mPath = new Path();
        View child = parentView.getChildAt(0);
        if (child != null) {
            float l = parentView.getPaddingLeft() + mMarginLeft;
            float t = parentView.getPaddingTop() + child.getMeasuredHeight() - mTabHeight - mMarginBottom;
            float r = parentView.getPaddingLeft() + child.getMeasuredWidth() - mMarginRight;
            float b = t + mTabHeight;

            if (mTabWidth != -1) {
                l += (child.getMeasuredWidth() - mTabWidth) / 2;
                r = mTabWidth + l;
            }
            mRect.set(l, t, r, b);
            mPath.moveTo(mRect.width() / 2 + mRect.left, t);
            mPath.lineTo(mRect.left, b);
            mPath.lineTo(mRect.right, b);
        }
    }

    @Override
    protected void valueChange(TabTypeValue value) {
        super.valueChange(value);
        mPath.reset();
        mPath.moveTo(mRect.width() / 2 + mRect.left, mRect.top);
        mPath.lineTo(mRect.left, mRect.bottom);
        mPath.lineTo(mRect.right, mRect.bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }
}
