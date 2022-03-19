package com.zhengsr.tablib.view.action;

import android.graphics.Canvas;
import android.view.View;

import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.flow.base.AbsFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class RoundAction extends BaseVpAction {
    private static final String TAG = "RoundAction";
    private float mRound;


    @Override
    public void configAttrs(TabBean bean) {
        super.configAttrs(bean);
        if (bean.tabRoundSize !=-1) {
            mRound = bean.tabRoundSize;
        }
    }



    @Override
    public void config(AbsFlowLayout parentView) {
        super.config(parentView);
        View child = parentView.getChildAt(0);
        if (child != null) {
            float l =  mTabBean.tabMarginLeft + child.getLeft();
            float t = mTabBean.tabMarginTop + child.getTop();
            float r =  child.getRight() - mTabBean.tabMarginRight;
            float b =  child.getBottom() - mTabBean.tabMarginBottom;
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
