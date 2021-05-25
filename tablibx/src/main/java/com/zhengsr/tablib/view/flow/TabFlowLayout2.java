package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.AbsFlowLayout;

/**
 * @author by zhengshaorui 2021/5/23 06:51
 * describe：不处理ViewPager的情况
 */
public class TabFlowLayout2 extends AbsFlowLayout {
    private static final String TAG = "TabFlowLayout2";
    /**
     * 滚动
     */
    private boolean isFirst = true;


    public TabFlowLayout2(Context context) {
        this(context, null);
    }

    public TabFlowLayout2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());

    }


    @Override
    protected void onViewVisible() {
        /**
         *  当横竖屏,或者异常重启之后，需要重新对位置，选中 index 等恢复到原来的状态
         */
        mAction.chooseIndex(mLastIndex,mCurrentIndex);
        updateScroll(getChildAt(mCurrentIndex),false);
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
        chooseItem(position, view);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        //让tab标签回到最后一个
        if (childCount > 0 && (childCount - 1) < mCurrentIndex) {
            int temp = mCurrentIndex;
            mCurrentIndex = childCount - 1;
            mAction.chooseIndex(temp, mCurrentIndex);
            postInvalidate();
        }
    }

    /**
     * 由外部设置位置，为不是自身点击的
     * 这个常用于 recyclerview 的联动效果
     *
     * @param position
     */
    public void chooseItem(int position) {
        if (position >= 0 && position < getChildCount()) {
            View view = getChildAt(position);
            chooseItem(position, view);
        }
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

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("instancestatus");
            mCurrentIndex = bundle.getInt("index");
            mLastIndex = bundle.getInt("lastindex");
        }
        super.onRestoreInstanceState(state);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instancestatus", super.onSaveInstanceState());

        if (mAction != null) {
            mLastIndex = mAction.getLastIndex();
        }

        bundle.putInt("index", mCurrentIndex);
        bundle.putInt("lastindex", mLastIndex);
        return bundle;
    }
}
