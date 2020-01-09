package com.zhengsr.tablib.view.action;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabTypeValue;
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
    public void configAttrs(TypedArray ta) {
        super.configAttrs(ta);
        mDrawable = ta.getDrawable(R.styleable.TabFlowLayout_tab_item_res);
    }

    @Override
    public void setBean(TabBean bean) {
        super.setBean(bean);
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

                int l = (int) (parentView.getPaddingLeft() + mMarginLeft);
                int t = (int) (parentView.getPaddingTop() + mMarginTop);
                int r = (int) (parentView.getPaddingLeft() + width - mMarginRight);
                int b = (int) (parentView.getPaddingTop() + height - mMarginBottom);
                mDrawable.setBounds(0, 0, width, height);
                mDrawable.draw(canvas);
                mRect.set(l, t, r, b);
                mSrcRect = new Rect(0, 0, width, height);

                Log.d(TAG, "zsr config: "+mSrcRect.toShortString()+" "+mRect);
            }

        }


    }

    @Override
    protected void valueChange(TabTypeValue value) {
        //super.valueChange(value);
        mRect.left = value.left + mMarginLeft;
        mRect.right = value.right - mMarginRight;

    }

    @Override
    public void draw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mSrcRect, mRect, mPaint);
           // canvas.drawBitmap(mBitmap,0,0,null);

        }
    }
}
