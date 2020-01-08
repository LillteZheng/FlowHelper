package com.zhengsr.tablib.view.cus;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.view.View;

import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bena.TabTypeValue;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class RoundAction extends BaseAction {
    private static final String TAG = "RoundAction";
    private float mRound;
    @Override
    public void configAttrs(TypedArray ta) {
        super.configAttrs(ta);
        mRound = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_round_size,10);
    }

    @Override
    public void config(TabFlowLayout parentView) {
        super.config(parentView);
        View child = parentView.getChildAt(0);
        if (child != null) {
            float l = parentView.getPaddingLeft() + mMarginLeft;
            float t = parentView.getPaddingTop() + mMarginTop;
            float r = child.getMeasuredWidth() - mMarginRight;
            float b = child.getMeasuredHeight() - mMarginBottom;
            mRect.set(l,t,r,b);

        }
        parentView.postInvalidate();
    }


    @Override
    protected void valueChange(TabTypeValue value) {
        //super.valueChange(value);
        mRect.left = value.left + mMarginLeft;
        mRect.right = value.right - mMarginRight;

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRect,mRound,mRound,mPaint);
    }
}
