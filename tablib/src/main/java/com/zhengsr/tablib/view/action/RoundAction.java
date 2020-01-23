package com.zhengsr.tablib.view.action;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabValue;
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
        mRound = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_round_size, 10);
    }

    @Override
    public void setBean(TabBean bean) {
        super.setBean(bean);

        if (bean.tabRoundSize !=-1) {
            mRound = bean.tabRoundSize;
        }
    }

    @Override
    public void config(TabFlowLayout parentView) {
        super.config(parentView);
        View child = parentView.getChildAt(0);
        if (child != null) {
            float l =  mMarginLeft + child.getLeft();
            float t = mMarginTop + child.getTop();
            float r =  child.getRight() - mMarginRight;
            float b =  child.getBottom() - mMarginBottom;
            mTabRect.set(l, t, r, b);
        }
        parentView.postInvalidate();
    }


    @Override
    protected void valueChange(TabValue value) {
        //super.valueChange(value);
        if (isVertical()){
            mTabRect.top = value.top ;
            mTabRect.bottom = value.bottom ;
        }
        mTabRect.left = value.left ;
        mTabRect.right = value.right ;

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mTabRect, mRound, mRound, mPaint);
    }
}
