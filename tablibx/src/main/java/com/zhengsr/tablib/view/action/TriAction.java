package com.zhengsr.tablib.view.action;

import android.graphics.Canvas;
import android.graphics.Path;
import android.view.View;

import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.flow.AbsFlowLayout;
import com.zhengsr.tablib.view.flow.TabFlowLayoutremove;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class TriAction extends BaseAction {
    private static final String TAG = "TriAction";
    private Path mPath;

    @Override
    public void config(AbsFlowLayout parentView) {
        super.config(parentView);
        mPath = new Path();
        View child = parentView.getChildAt(0);
        if (child != null) {
            float l ;
            float t;
            float r;
            float b;
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
                    l += (child.getMeasuredWidth() - mTabBean.tabWidth) / 2;
                    r = mTabBean.tabWidth + l;
                }
            }


            mTabRect.set(l, t, r, b);

            if (isVertical()){
                mPath.moveTo(r,t + mTabBean.tabHeight/2);
                mPath.lineTo(l,t);
                mPath.lineTo(l,b);
            }else{

                mPath.moveTo(l+mTabBean.tabWidth/ 2 , t);
                mPath.lineTo(l, b);
                mPath.lineTo(r, b);
            }
        }
    }

    @Override
    protected void valueChange(TabValue value) {
        super.valueChange(value);
        mPath.reset();

        if (isVertical()){
            mTabRect.set(value.valueToRect());
            float l = mTabRect.left;
            float t = mTabRect.top;
            float r = mTabRect.right;
            float b = mTabRect.bottom;
            if (isRightAction()){
                l = r;
                r = l - mTabBean.tabWidth;
            }
            mPath.moveTo(r,t + mTabBean.tabHeight/2);
            mPath.lineTo(l,t);
            mPath.lineTo(l,b);
        }else {
            mPath.moveTo(mTabRect.width() / 2 + mTabRect.left, mTabRect.top);
            mPath.lineTo(mTabRect.left, mTabRect.bottom);
            mPath.lineTo(mTabRect.right, mTabRect.bottom);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
      //  canvas.drawRect(mTabRect,mPaint);
    }
}
