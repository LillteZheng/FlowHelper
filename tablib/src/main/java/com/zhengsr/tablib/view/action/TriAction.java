package com.zhengsr.tablib.view.action;

import android.graphics.Canvas;
import android.graphics.Path;
import android.view.View;

import com.zhengsr.tablib.bean.TabValue;
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
            float l ;
            float t;
            float r;
            float b;
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
                    l += (child.getMeasuredWidth() - mTabWidth) / 2;
                    r = mTabWidth + l;
                }
            }


            mTabRect.set(l, t, r, b);

            if (isVertical()){
                mPath.moveTo(r,t + mTabHeight/2);
                mPath.lineTo(l,t);
                mPath.lineTo(l,b);
            }else{

                mPath.moveTo(l+mTabWidth/ 2 , t);
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
                r = l - mTabWidth;
            }
            mPath.moveTo(r,t + mTabHeight/2);
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
