package com.zhengsr.tablib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 瀑布流布局,这个类只用来测量子控件，不做其他操作
 */
class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        int width = 0;
        int height = 0;
        /**
         * 计算宽高
         */
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE){
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

            //拿到 子控件宽高 + margin
            int cw = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int ch = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            width += cw;
            //拿到 子控件高度，拿到最大的那个高度
            height = Math.max(height, ch);


        }

        //具体大小，padding不受影响
        if (MeasureSpec.EXACTLY == heightMode) {
            height = heightSize;
        }else{
            height = height + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int cl = left + params.leftMargin;
            int ct = top + params.topMargin;
            int cr = cl + child.getMeasuredWidth() ;
            int cb = ct + child.getMeasuredHeight() ;
            //下个控件的起始位置
            left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            child.layout(cl, ct, cr, cb);
        }
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }
}
