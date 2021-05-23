package com.zhengsr.tablib.view.flow.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.zhengsr.tablib.view.flow.AbsFlowLayout;

/**
 * @author by zhengshaorui 2021/5/23 06:51
 * describe：不处理ViewPager的情况
 */
public class TabFlowLayout2 extends AbsFlowLayout {
    private static final String TAG = "TabFlowLayout2";
    private boolean isFirst = true;
    protected int mLastScrollPos = 0;
    protected int mLastIndex = 0;
    protected int mCurrentIndex = 0;

    public TabFlowLayout2(Context context) {
        this(context,null);
    }

    public TabFlowLayout2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabFlowLayout2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public void onViewVisible() {
        Log.d(TAG, "zsr onViewVisible: "+getViewWidth());
        /**
         *  当横竖屏,或者异常重启之后，需要重新对位置，选中 index 等恢复到原来的状态
         */
        if (isFirst) {
            isFirst = false;
            if (mAction == null) {
                return;
            }
            mAction.config(this);
            mAction.chooseIndex(mLastIndex, mCurrentIndex);
            //让它滚动到对应的位置
            final View view = getChildAt(mCurrentIndex);
            if (view != null) {
                updateScroll(view, false);
            }
        }
    }

    /**
     * 更新滚动
     * @param view
     */
    private void updateScroll(View view, boolean smoothScroll) {
        if (isCanMove() && view != null) {
            //超过中间了，让父控件也跟着移动
            int scrollPos;
            if (isVertical()) {
                scrollPos = view.getTop();
            } else {
                scrollPos = view.getLeft();
            }
            int offset;
            if (scrollPos != mLastScrollPos) {
                if (isVertical()) {
                    if (scrollPos > mHeight / 2) {
                        scrollPos -= mHeight / 2;
                        //下边界
                        if (scrollPos < mBottomRound - mHeight) {
                            offset = scrollPos - mLastScrollPos;
                            if (smoothScroll) {
                                mScroller.startScroll(0, getScrollY(), 0, offset);
                            } else {
                                scrollTo(0, offset);
                            }
                            mLastScrollPos = scrollPos;
                        } else {
                            offset = mBottomRound - mHeight - getScrollY();
                            if (getScrollY() >= mBottomRound - mHeight) {
                                offset = 0;
                            }
                            if (smoothScroll) {
                                mScroller.startScroll(0, getScrollY(), 0, offset);
                            } else {
                                scrollTo(0, mBottomRound - mHeight);
                            }
                            mLastScrollPos = mBottomRound - mHeight - offset;
                        }
                    } else {
                        offset = -scrollPos;
                        if (smoothScroll) {
                            mScroller.startScroll(0, getScrollY(), 0, offset);
                        } else {
                            scrollTo(0, 0);
                        }
                        mLastScrollPos = 0;
                    }
                } else {
                    if (scrollPos > mWidth / 2) {
                        scrollPos -= mWidth / 2;
                        //有边界提醒
                        if (scrollPos < mRightBound - mWidth) {
                            offset = scrollPos - mLastScrollPos;
                            if (smoothScroll) {
                                mScroller.startScroll(getScrollX(), 0, offset, 0);
                            } else {
                                scrollTo(offset, 0);
                            }
                            mLastScrollPos = scrollPos;
                        } else {
                            offset = mRightBound - mWidth - getScrollX();
                            if (getScrollX() >= mRightBound - mWidth) {
                                offset = 0;
                            }
                            if (smoothScroll) {
                                mScroller.startScroll(getScrollX(), 0, offset, 0);
                            } else {
                                scrollTo(mRightBound - mWidth, 0);
                            }
                            mLastScrollPos = mRightBound - mWidth - offset;
                        }
                    } else {
                        offset = -scrollPos;
                        if (smoothScroll) {
                            mScroller.startScroll(getScrollX(), 0, offset, 0);
                        } else {
                            scrollTo(0, 0);
                        }
                        mLastScrollPos = 0;
                    }
                }
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {
            //有边界
            int offset;
            if (isVertical()) {
                offset = mScroller.getCurrY();
                if (offset >= mBottomRound - mHeight) {
                    offset = mBottomRound - mHeight;
                }
            } else {
                offset = mScroller.getCurrX();
                if (offset >= mRightBound - mWidth) {
                    offset = mRightBound - mWidth;
                }
            }

            if (offset <= 0) {
                offset = 0;
            }
            if (isVertical()) {
                scrollTo(0, offset);
            } else {
                scrollTo(offset, 0);
            }
            postInvalidate();
        }
    }


    @Override
    protected void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        chooseItem(position,view);
    }
    /**
     * 选中某个tab
     *
     * @param position
     * @param view
     */
    private void chooseItem(int position, View view) {
        mLastIndex = mCurrentIndex;
        mCurrentIndex = position;

        if (mAction != null) {
            mAction.onItemClick(mLastIndex, position);
        }
        /**
         * 如果没有 viewpager，则需要使用 scroller 平滑过渡
         */
        updateScroll(view, true);
        invalidate();


    }
}
