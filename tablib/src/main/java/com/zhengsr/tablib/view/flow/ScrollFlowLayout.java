package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.Scroller;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 滚动类，用来移动
 */
class ScrollFlowLayout extends FlowLayout {
    private static final String TAG = "ScrollFlowLayout";
    private int mTouchSlop;
    private float mLastPos;
    private float mMovePos;
    protected int mRightBound;
    protected int mBottomRound;
    private boolean isCanMove;
    private int mScreenWidth;
    private int mScreenHeight;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private int mCurScrollPos;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    protected boolean isMove;
    protected int mWidth;
    protected int mHeight;

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
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
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
            mBottomRound = child.getBottom() + getPaddingBottom();
        }


        /**
         * TabFlowLayout 或者 LabelFlowLayout 的竖向滚动
         */
        //todo 这是什么魔性 if else 找机会改一下
        if (isVerticalMove()){
            if (mViewHeight < mScreenHeight){
                if (mBottomRound > mViewHeight){
                    isCanMove = true;
                }else{
                    isCanMove = false;
                }
                mHeight = mViewHeight;

            }else{
                //再确认一遍
                if (mBottomRound > mScreenHeight){
                    isCanMove = true;
                }
                mHeight = mScreenHeight;
                //需要减去actionbar 和 状态栏的高度
                mHeight = mHeight - getActionBarHeight(getContext()) - getStatusBarHeight();
            }
            if (!isLabelAutoScroll() || !isTabAutoScroll()){
                isCanMove = false;
            }

        }else{
            //TabFlowLayout 横向布局
            if (!isVertical()) {
                //如果是固定宽度
                if (mVisualCount != -1){
                    if (getChildCount() > mVisualCount){
                        isCanMove = true;
                    }else{
                        isCanMove = false;
                    }
                    mWidth = mViewWidth;
                }else {
                    //说明控件没有满屏或者固定宽度
                    if (mViewWidth < mScreenWidth) {
                        if (mRightBound > mViewWidth) {
                            isCanMove = true;
                        } else {
                            isCanMove = false;
                        }
                        mWidth = mViewWidth;
                    } else {
                        //再确认一遍
                        if (mRightBound > mScreenWidth) {
                            isCanMove = true;
                        }
                        mWidth = mScreenWidth;
                    }
                }
                if (!isTabAutoScroll()) {
                    isCanMove = false;
                }
            }
        }




    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isCanMove){
            return super.onInterceptTouchEvent(ev);
        }
        final ViewParent parent = getParent();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isVertical()){
                    mLastPos = ev.getY();
                }else {
                    mLastPos = ev.getX();
                }
                //拿到上次的down坐标
                if (isVertical()){
                    mMovePos = ev.getY();
                }else {
                    mMovePos = ev.getX();
                }

                if (mScroller != null && !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                mVelocityTracker = VelocityTracker.obtain();

                mVelocityTracker.addMovement(ev);
                //如果能滚动，应该要屏蔽父控件的触摸事件
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }

                break;

            case MotionEvent.ACTION_MOVE:
                float offset;
                if (isVerticalMove()){
                    offset = ev.getY() - mLastPos;
                }else{
                    offset = ev.getX() - mLastPos;
                }
                if (Math.abs(offset) >= mTouchSlop) {
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }

                    if (mVelocityTracker == null){
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    mVelocityTracker.addMovement(ev);
                    //由父控件接管触摸事件
                    return true;
                }
                if (isVerticalMove()){
                    mLastPos = ev.getY();
                }else {
                    mLastPos = ev.getX();
                }
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
        final ViewParent parent = getParent();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                
                break;
            case MotionEvent.ACTION_MOVE:
                //scroller 向右为负，向左为正
                int offset;
                if (isVerticalMove()){
                    offset = (int) (mMovePos - event.getY());
                }else {
                    offset = (int) (mMovePos - event.getX());
                }
                if (Math.abs(offset) > mTouchSlop){
                  
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                int scrollPos ;
                if (isVerticalMove()){
                    scrollPos = getScrollY();
                }else{
                    scrollPos = getScrollX();
                }
                /**
                 * 判断边界
                 */
                if (scrollPos + offset <= 0) {
                    scrollTo(0, 0);
                    return true;
                }
                if (isVerticalMove()){
                    if (scrollPos +offset >= mBottomRound - mHeight){
                        scrollTo(0,mBottomRound - mHeight);
                        return true;
                    }
                    scrollBy(0, offset);
                }else{
                    if (scrollPos +offset >= mRightBound - mWidth) {
                        scrollTo(mRightBound - mWidth, 0);
                        return true;
                    }
                    scrollBy(offset, 0);
                }



                isMove = true;
                if (isVerticalMove()){
                    mMovePos = event.getY();
                }else {
                    mMovePos = event.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000,mMaximumVelocity);
                int velocityPos ;
                if (isVerticalMove()){
                    velocityPos = (int) mVelocityTracker.getYVelocity();
                }else{
                    velocityPos = (int) mVelocityTracker.getXVelocity();
                }
                if (Math.abs(velocityPos) >= mMinimumVelocity) {
                    if (isVerticalMove()){
                        mCurScrollPos = getScrollY();
                        mScroller.fling(0,mCurScrollPos,0,velocityPos,
                                0,0,0,getHeight());
                    }else {
                        mCurScrollPos = getScrollX();
                        mScroller.fling(mCurScrollPos, 0, velocityPos,
                                0, 0, getWidth(), 0, 0);
                    }

                    if (mVelocityTracker != null) {
                        mVelocityTracker.clear();
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
            int offset;
            if (isVerticalMove()){
                offset = mCurScrollPos - mScroller.getCurrY();
            }else {
                offset = mCurScrollPos - mScroller.getCurrX();
            }
            // 超出边界，进行修正
            int scrollPos;
            if (isVerticalMove()){
                if (getScrollY() + offset >= mBottomRound - mHeight) {
                    offset =  mBottomRound - mHeight - getScrollY();
                }

                // 超出边界，进行修正
                if (getScrollY() + offset <= 0) {
                    offset = -getScrollY();
                }
                scrollBy(0,offset);
            }else{
                if (getScrollX() + offset >= mRightBound - mWidth) {
                    offset = mRightBound - mWidth - getScrollX();
                }

                // 超出边界，进行修正
                if (getScrollX() + offset <= 0) {
                    offset = -getScrollX();
                }
                scrollBy(offset,0);

            }







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

    /**
     * 获取状态栏的高度
     * @return
     */
    private int getStatusBarHeight(){
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 拿到 actionbar 的高度
     * @param context
     * @return
     */
    private int getActionBarHeight(Context context){
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize,tv,true)){
            return  TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public boolean isLabelAutoScroll(){
        return true;
    }

    public boolean isTabAutoScroll(){
        return true;
    }
}
