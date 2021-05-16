package com.zhengsr.tablib.view.action;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class ResAction extends BaseAction {
    private static final String TAG = "ResAction";
    private Bitmap mBitmap;
    private Rect mSrcRect;
    private Drawable mDrawable;
    private int mRes = -1;

    @Override
    public void configAttrs(TabBean bean) {
        super.configAttrs(bean);
        mRes = bean.tabItemRes;

    }



    @Override
    public void config(TabFlowLayout parentView) {
        super.config(parentView);
        if (mRes != -1) {
            mDrawable = mContext.getResources().getDrawable(mRes);
        }
        View child = parentView.getChildAt(0);
        if (child != null) {
            if (mDrawable != null) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mBitmap);

                float l = mTabBean.tabMarginLeft + child.getLeft();
                float t = mTabBean.tabMarginTop + child.getTop();
                float r = child.getRight() - mTabBean.tabMarginRight;
                float b = child.getBottom() - mTabBean.tabMarginBottom;
                mDrawable.setBounds(0, 0, width, height);
                mDrawable.draw(canvas);
                if (mTabBean.tabWidth != -1) {
                    l += (child.getMeasuredWidth() - mTabBean.tabWidth) / 2 ;
                    r = mTabBean.tabWidth + l;
                }
                mTabRect.set(l, t, r, b);
                mSrcRect = new Rect(0, 0, width, height);

            }

        }


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
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mSrcRect, mTabRect, mPaint);

        }
    }
}
