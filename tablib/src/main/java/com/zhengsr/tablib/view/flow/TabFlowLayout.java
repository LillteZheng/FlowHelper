package com.zhengsr.tablib.view.flow;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.zhengsr.tablib.Constants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.view.ColorTextView;
import com.zhengsr.tablib.view.adapter.TabAdapter;
import com.zhengsr.tablib.callback.FlowListenerAdapter;
import com.zhengsr.tablib.utils.ViewPagerHelperUtils;
import com.zhengsr.tablib.view.action.BaseAction;
import com.zhengsr.tablib.view.action.RectAction;
import com.zhengsr.tablib.view.action.ResAction;
import com.zhengsr.tablib.view.action.RoundAction;
import com.zhengsr.tablib.view.action.TriAction;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 实现数据封装与一些重绘工作
 */
public class TabFlowLayout extends ScrollFlowLayout {
    private static final String TAG = "TabFlowLayout";
    private TabAdapter mAdapter;
    private int mLastIndex = 0;
    private BaseAction mAction;
    private ViewPager mViewPager;
    private Scroller mScroller;
    private int mLastScrollX = 0;
    private boolean isFirst = true;
    private TypedArray mTypeArray;
    private int mCurrentIndex = 0;

    public TabFlowLayout(Context context) {
        this(context, null);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.TabFlowLayout);
        int tabStyle = mTypeArray.getInteger(R.styleable.TabFlowLayout_tab_style, -1);
        mScroller = new Scroller(getContext());
        if (tabStyle != -1) {
            switch (tabStyle) {
                case Constants.RECT:
                    mAction = new RectAction();
                    break;
                case Constants.TRI:
                    mAction = new TriAction();
                    break;
                case Constants.ROUND:
                    mAction = new RoundAction();
                    break;
                case Constants.RES:
                    mAction = new ResAction();
                    break;
                default:
                    break;
            }
        }

        //配置自定义属性给 action
        if (mAction != null) {
            mAction.configAttrs(mTypeArray);
        }
        if (mAction != null) {
            mTypeArray.recycle();
        }


        /**
         * 如果超过了屏幕大小，且父布局是 LinearLayout ，gravity 或 自身的 layout_gravity 不是 left；
         * 则需要自身去重新设置，不然初始位置是在中间开始去layout的
         */
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getWidth() > mScreenWidth) {
                    ViewGroup parent = (ViewGroup) getParent();
                    if (parent instanceof LinearLayout) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
                        params.gravity = Gravity.START;
                        setLayoutParams(params);
                    }
                }

                /**
                 *  当横竖屏之后，需要重新对位置，选中 index 等恢复到原来的状态
                 */
                if (isFirst) {
                    isFirst = false;
                    if (mViewPager != null) {
                        if (mAction != null) {
                            mAction.chooseSelectedPosition(mCurrentIndex);
                            mAction.doAnim(mLastIndex, mCurrentIndex);
                        }

                    } else {
                        if (mAction != null && mCurrentIndex > 0) {
                            mAction.onItemClick(mLastIndex, mCurrentIndex);
                        }
                    }
                    View view = getChildAt(mCurrentIndex);
                    if (view != null) {
                        updateScroll(view);
                    }
                }
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mAction != null) {
            mAction.draw(canvas);
        }
        super.dispatchDraw(canvas);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mAction != null) {
            mAction.config(this);
        }
    }

    /**
     * 添加adapter，
     *
     * @param adapter
     */
    public void setAdapter(TabAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setListener(new FlowListener());
        //实现数据更新
        notifyChanged();
    }

    /**
     * 自定义的action
     *
     * @param action
     */
    public void setCusAction(BaseAction action) {
        mAction = action;
        mAction.configAttrs(mTypeArray);
        mTypeArray.recycle();
    }


    class FlowListener extends FlowListenerAdapter {
        @Override
        public void notifyDataChanged() {
            super.notifyDataChanged();
            notifyChanged();
        }

        @Override
        public void resetAllTextColor(int viewId, int color) {
            super.resetAllTextColor(viewId, color);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                TextView textView = view.findViewById(viewId);
                if (textView != null) {
                    textView.setTextColor(color);
                }
            }
        }

    }

    public void setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, -1, 0, 0, 0);
    }


    public void setViewPager(ViewPager viewPager, int textId, int unselectedColor, int selectedColor) {
        setViewPager(viewPager, textId, 0, unselectedColor, selectedColor);
    }

    public void setViewPager(ViewPager viewPager, int textId, int selectedIndex, int unselectedColor, int selectedColor) {
        if (viewPager != null) {
            mViewPager = viewPager;
            ViewPagerHelperUtils.initSwitchTime(getContext(), viewPager, 600);
            if (mAction != null) {
                mAction.setViewPager(viewPager, textId, unselectedColor, selectedColor);
            }
        }

        mCurrentIndex = selectedIndex;

    }

    /**
     * 数据变化
     */
    private void notifyChanged() {
        removeAllViews();
        TabAdapter adapter = mAdapter;
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            View view = LayoutInflater.from(getContext()).inflate(adapter.getLayoutId(), this, false);
            adapter.bindView(view, adapter.getDatas().get(i), i);
            configClick(view, i);
            addView(view);
        }
    }


    /**
     * 配置 点击事件
     *
     * @param view
     * @param i
     */

    private void configClick(final View view, final int i) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = i;
                if (mViewPager != null && mAction != null) {
                    mLastIndex = mAction.getCurrentIndex();
                }
                mAdapter.onItemClick(view, mAdapter.getDatas().get(i), i);
                if (mAction != null) {
                    mAction.onItemClick(mLastIndex, i);
                }
                mLastIndex = mCurrentIndex;
                /**
                 * 如果没有 viewpager，则需要使用 scroller 平滑过渡
                 */
                if (mViewPager == null) {
                    updateScroll(view);
                }
            }
        });
    }


    private void updateScroll(View view) {
        //超过中间了，让父控件也跟着移动
        int scrollX = view.getLeft();
        if (scrollX > mScreenWidth / 2 - getPaddingLeft()) {
            scrollX -= mScreenWidth / 2 - getPaddingLeft();
            //有边界提醒
            if (scrollX < mRightBound - mScreenWidth) {
                int dx = scrollX - mLastScrollX;
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                mLastScrollX = scrollX;
            } else {
                int dx = mRightBound - mScreenWidth - getScrollX();
                if (getScrollX() >= mRightBound - mScreenWidth) {
                    dx = 0;
                }
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                mLastScrollX = mRightBound - mScreenWidth - dx;
            }
        } else {
            scrollTo(0, 0);
            mLastScrollX = 0;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int dx = mScroller.getCurrX();
            //有边界
            scrollTo(dx, 0);
        }
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
        if (mViewPager != null) {
            mCurrentIndex = mViewPager.getCurrentItem();
        }
        bundle.putInt("index", mCurrentIndex);
        bundle.putInt("lastindex", mLastIndex);
        return bundle;
    }

    @Override
    public boolean isVertical() {
        return false;
    }
}
