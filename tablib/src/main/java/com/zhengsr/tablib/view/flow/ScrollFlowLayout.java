package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.zhengsr.tablib.view.flow.FlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 滚动类，用来移动
 */
class ScrollFlowLayout extends FlowLayout {
    private static final String TAG = "ScrollFlowLayout";
    private int mTouchSlop;
    private float mLastX;
    private float mMoveX;
    protected int mRightBound;
    private boolean isCanMove;
    private int mScreenWidth;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private int mCurScrollX;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    protected boolean isMove;
    protected int mWidth;

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
        mScroller = new Scroller(context);
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

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

        //说明控件没有满屏或者固定宽度
        if (mViewWidth < mScreenWidth){
            if (mRightBound > mViewWidth){
                isCanMove = true;
            }else{
                isCanMove = false;
            }
            mWidth = mViewWidth;
        }else{
            //再确认一遍
            if (mRightBound > mScreenWidth){
                isCanMove = true;
            }
            mWidth = mScreenWidth;
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

                if (mScroller != null && !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

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
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
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
                if (scrollX + dx >= mRightBound - mWidth) {
                    scrollTo(mRightBound - mWidth, 0);
                    return true;
                }

                scrollBy(dx, 0);
                isMove = true;
                mMoveX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                mVelocityTracker.computeCurrentVelocity(1000,mMaximumVelocity);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                if (Math.abs(velocityX) >= mMinimumVelocity) {
                    mCurScrollX = getScrollX();
                    mScroller.fling(mCurScrollX, 0, velocityX, 0, 0, getWidth(), 0, 0);
                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                }
                break;
            default:
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            int dx = mCurScrollX - mScroller.getCurrX();
            // 超出右边界，进行修正
            if (getScrollX() + dx >= mRightBound - mWidth) {
                dx = mRightBound - mWidth - getScrollX();
            }

            // 超出左边界，进行修正
            if (getScrollX() + dx <= 0) {
                dx = -getScrollX();
            }
            scrollBy(dx,0);
            postInvalidate();
        }
    }

    public int getViewWidth(){
        return mWidth;
    }

    public boolean isCanMove() {
        return isCanMove;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(boolean move) {
        isMove = move;
    }
}
