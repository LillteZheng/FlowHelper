package com.zhengsr.tablib.view.cus;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.zhengsr.tablib.R;
import com.zhengsr.tablib.view.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class ResAction extends BaseAction {
    private static final String TAG = "ResAction";
    private Bitmap mBitmap;
    private int mResourceId;
    private Rect mDstRect;
    private Drawable mDrawable;

    @Override
    public void configAttrs(TypedArray ta) {
        super.configAttrs(ta);
        mResourceId = ta.getResourceId(R.styleable.TabFlowLayout_tab_item_res, -1);
        mDrawable = ta.getDrawable(R.styleable.TabFlowLayout_tab_item_res);
    }
    @Override
    public void config(TabFlowLayout parentView) {
        super.config(parentView);

        View child = parentView.getChildAt(0);
        if (child != null){
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBitmap);

            int l = parentView.getPaddingLeft();
            int t = parentView.getPaddingTop();
            int r = (int) (width - mMarginRight);
            int b = (int) (height - mMarginBottom);
            mDrawable.setBounds((int) (l+mMarginLeft), (int) (t+mMarginTop),r,b);
            mDrawable.draw(canvas);
            mRect.set(l,t,r,b);
            mDstRect = new Rect(l,t,r,b);
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
            canvas.drawBitmap(mBitmap,mDstRect,mRect,mPaint);
        }
    }
}
