package com.zhengsr.tablib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 滚动类，用来移动
 */
public class ScrollFlowLayout extends FlowLayout {
    private static final String TAG = "ScrollFlowLayout";
    private int mTouchSlop;
    private float mLastX;
    private float mMoveX;
    private int mRightBound;
    private boolean isCanMove;
    private int mScreenWidth;

    public ScrollFlowLayout(Context context) {
        this(context, null);
    }

    public ScrollFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        //拿到有边界
        if (count > 0) {
            View child = getChildAt(count - 1);
            mRightBound = child.getRight() + getPaddingRight();
        }

        //判断是否可移动
        if (getWidth() > mScreenWidth){
            isCanMove = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isCanMove){
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getX();
                //拿到上次的down坐标
                mMoveX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - mLastX;
                if (Math.abs(dx) >= mTouchSlop) {
                    //由父控件接管触摸事件
                    return true;
                }
                mLastX = ev.getX();
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //scroller 向右为负，向左为正
                int dx = (int) (mMoveX - event.getX());
                /**
                 * 判断左右边界
                 */
                int scrollX = getScrollX();
                if (scrollX + dx <= 0) {
                    scrollTo(0, 0);
                    return true;
                }
                if (scrollX + dx >= mRightBound - mScreenWidth) {
                    scrollTo(mRightBound - mScreenWidth, 0);
                    return true;
                }

                scrollBy(dx, 0);
                mMoveX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;

        }
        return super.onTouchEvent(event);
    }
}
