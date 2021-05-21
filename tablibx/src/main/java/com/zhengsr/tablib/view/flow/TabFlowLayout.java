package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
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

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabConfig;
import com.zhengsr.tablib.callback.FlowListenerAdapter;
import com.zhengsr.tablib.utils.AttrsUtils;
import com.zhengsr.tablib.view.action.BaseAction;
import com.zhengsr.tablib.view.action.ColorAction;
import com.zhengsr.tablib.view.action.RectAction;
import com.zhengsr.tablib.view.action.ResAction;
import com.zhengsr.tablib.view.action.RoundAction;
import com.zhengsr.tablib.view.action.TriAction;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;


/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 实现数据封装与一些重绘工作
 * 好想重构，好绝望，写的啥啊
 */
public class TabFlowLayout extends ScrollFlowLayout {
    private static final String TAG = "TabFlowLayout";
    private TabFlowAdapter mAdapter;
    private BaseAction mAction;

    private boolean isFirst = true;

    /**
     * 滚动
     */
    private Scroller mScroller;
    private int mLastScrollPos = 0;
    private int mLastIndex = 0;
    private int mCurrentIndex = 0;

    /**
     * viewpager 相关
     */
    private ViewPager mViewPager;
    private ViewPager2 mViewPager2;
    private TabBean mTabBean;
    private TabConfig mTabConfig;

    public TabFlowLayout(Context context) {
        this(context, null);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        TypedArray ta  = context.obtainStyledAttributes(attrs, R.styleable.TabFlowLayout);
        mTabBean = AttrsUtils.getTabBean(ta);
        mScroller = new Scroller(getContext());
        setVisibleCount(mTabBean.visualCount);
        setTabOrientation(mTabBean.tabOrientation);
        chooseTabTpye(mTabBean.tabType);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                reAdjustLayoutParams();

                /**
                 *  当横竖屏,或者异常重启之后，需要重新对位置，选中 index 等恢复到原来的状态
                 */
                if (isFirst) {
                    isFirst = false;

                    if (mAction == null) {
                        return;
                    }
                    mAction.config(TabFlowLayout.this);


                    mAction.chooseIndex(mLastIndex, mCurrentIndex);

                    //让它滚动到对应的位置
                    final View view = getChildAt(mCurrentIndex);
                    if (view != null) {
                        if (mViewPager == null) {
                            updateScroll(view, false);
                        }
                    }
                }
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    /**
     * 如果超过了屏幕大小，且父布局是 LinearLayout ，gravity 或 自身的 layout_gravity 不是 bottom；
     * 则需要自身去重新设置，不然初始位置是在中间开始去layout的。
     * 如果是 ConstraintLayout ，width 又是 wrap_content 的，只需要改成0即可
     */
    private void reAdjustLayoutParams() {
        if (!isVertical()) {
            if (getWidth() > mWidth) {
                ViewGroup parent = (ViewGroup) getParent();
                if (parent instanceof LinearLayout) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
                    params.gravity = Gravity.START;
                    setLayoutParams(params);
                } else if (parent instanceof ConstraintLayout) {
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) getLayoutParams();
                    boolean isWrapContent = params.width == ConstraintLayout.LayoutParams.WRAP_CONTENT;
                    if (isWrapContent && isCanMove()) {
                        params.width = 0;
                        setLayoutParams(params);
                    }
                }
            }
        }
    }


    /**
     * 选中不同的 action
     *
     * @param tabStyle
     */
    private void chooseTabTpye(int tabStyle) {
        if (tabStyle != -1) {
            switch (tabStyle) {
                case FlowConstants.RECT:
                    mAction = new RectAction();
                    break;
                case FlowConstants.TRI:
                    mAction = new TriAction();
                    break;
                case FlowConstants.ROUND:
                    mAction = new RoundAction();
                    break;
                case FlowConstants.RES:
                    mAction = new ResAction();
                    break;
                case FlowConstants.COLOR:
                    mAction = new ColorAction();
                    break;
                default:
                    break;
            }
        }
        //配置自定义属性给 action
        if (mAction != null) {
            mAction.setContext(this.getContext());
            mAction.configAttrs(mTabBean);
        }

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mAction != null) {
            mAction.draw(canvas);
        }
        super.dispatchDraw(canvas);

    }

    /**
     * 添加adapter，
     * @param adapter
     */
    public void setAdapter(TabFlowAdapter adapter) {
        setAdapter(null,adapter);
    }

    public void setAdapter(TabConfig tabConfig,TabFlowAdapter adapter) {
        setTabConfig(tabConfig);
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
        mAction.configAttrs(mTabBean);


        if (mAction != null) {
            if (mViewPager != null && mAction.getViewPager() == null) {
                mAction.setViewPager(mViewPager);
            }
            if (mViewPager2 != null && mAction.getViewPager2() == null) {
                mAction.setViewPager(mViewPager2);
            }
            mAction.setTabConfig(mTabConfig);

        }
    }


    /**
     * 监听adapter 的一些操作
     */
    class FlowListener extends FlowListenerAdapter {
        @Override
        public void notifyDataChanged() {
            super.notifyDataChanged();
            TabFlowAdapter adapter = mAdapter;
            int childCount = getChildCount();
            if (childCount != adapter.getDatas().size()){
                throw new RuntimeException("you need use notifyInsertOrRemoveChange() ");
            }
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                adapter.bindView(view,adapter.getDatas().get(i),i);

            }
        }

        @Override
        public void notifyInsertOrRemoveChange() {
            super.notifyInsertOrRemoveChange();
            //todo 后续重构后再修复
            /*if (mViewPager != null || mViewPager2 != null){
                throw new RuntimeException("notifyInsertOrRemoveChange not support this method when Viewpager is not empty");
            }*/
            if (mVisibleCount != -1 && getChildCount() > 0){
                throw new RuntimeException("notifyInsertOrRemoveChange not support when visibleCount is not -1");
            }
            notifyChanged();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mCurrentIndex > getChildCount()-1){
                        mCurrentIndex --;
                    }
                    mAction.chooseIndex(mLastIndex, mCurrentIndex);
                    mLastIndex = mCurrentIndex;
                    postInvalidate();
                }
            },100);
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



    /**
     * 数据变化
     */
    private void notifyChanged() {
        removeAllViews();
        TabFlowAdapter adapter = mAdapter;
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            View view = LayoutInflater.from(getContext()).inflate(adapter.getLayoutId(), this, false);
            adapter.bindView(view, adapter.getDatas().get(i), i);
            configClick(view, i);
            addView(view);
        }
        //如果此时 width 为 0，则是加载完布局，但是数据还没有导入，则需要重新适配一下；
        if (mWidth == 0 && getWidth() == 0 || mVisibleCount > 0) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getChildCount() > 0) {
                        reAdjustLayoutParams();
                        if (mAction != null) {
                            mAction.config(TabFlowLayout.this);
                            if (mViewPager != null) {
                                mViewPager.setCurrentItem(mCurrentIndex, false);
                            }
                            if (mViewPager2 != null) {
                                mViewPager2.setCurrentItem(mCurrentIndex, false);
                            }
                            mAction.chooseIndex(mLastIndex, mCurrentIndex);
                            updateScroll(getChildAt(mCurrentIndex), false);
                        }
                    }

                }
            }, 5);
        }
    }


    /**
     * 由外部设置位置，为不是自身点击的
     * 这个常用于 recyclerview 的联动效果
     *
     * @param position
     */
    public void setItemClickByOutSet(int position) {
        isItemClick = false;
        if (position >= 0 && position < getChildCount()) {
            View view = getChildAt(position);
            chooseItem(position, view);
        }
    }

    /**
     * 配置 点击和长按事件
     *
     * @param view
     * @param i
     */
    private void configClick(final View view, final int i) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isItemClick = true;
                if (mViewPager2 == null && mViewPager == null){
                    chooseItem(i,view);
                }else {
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(i);
                    }
                    if (mViewPager2 != null) {
                        mViewPager2.setCurrentItem(i);
                    }
                    if (mAdapter != null) {
                        mAdapter.onItemClick(view, mAdapter.getDatas().get(i),i);
                    }
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mAdapter != null) {
                    return mAdapter.onItemLongClick(view, i);
                }
                return false;
            }
        });
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
        if ((mViewPager != null||mViewPager2 != null) && mAction != null) {
            mLastIndex = mAction.getCurrentIndex();
        }
        if (mAction != null) {
            mAction.onItemClick(mLastIndex, position);
        }
        /**
         * 如果没有 viewpager，则需要使用 scroller 平滑过渡
         */
        if (mViewPager == null && mViewPager2 == null) {
            updateScroll(view, true);
            invalidate();
        }

        if (mAdapter != null) {
            mAdapter.onItemClick(view, mAdapter.getDatas().get(position), position);
        }
    }

    /**
     * 更新滚动
     *
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

        if ((mViewPager == null && mViewPager2 == null)&& mScroller.computeScrollOffset()) {
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
            mLastIndex = 0;
        }else if (mViewPager2 != null) {
            mCurrentIndex = mViewPager2.getCurrentItem();
            mLastIndex = 0;
        } else {
            if (mAction != null) {
                mLastIndex = mAction.getLastIndex();
            }
        }
        bundle.putInt("index", mCurrentIndex);
        bundle.putInt("lastindex", mLastIndex);
        return bundle;
    }


    /**
     * 自定义属性的配置,设置该属性会覆盖xml的属性
     */

    public TabFlowLayout setTabBean(TabBean bean) {
        if (bean == null) {
            return this;
        }
        mTabBean = AttrsUtils.diffTabBean(mTabBean,bean);
        if (bean.tabType != -1) {
            chooseTabTpye(bean.tabType);
        }

        if (mAction != null) {
            if (mTabBean != null) {
                mAction.configAttrs(mTabBean);
                if (mViewPager != null && mAction.getViewPager() == null) {
                    mAction.setViewPager(mViewPager);
                }
                if (mViewPager2 != null && mAction.getViewPager2() == null) {
                    mAction.setViewPager(mViewPager2);
                }
                mAction.setTabConfig(mTabConfig);
            }
        }


        setTabOrientation(bean.tabOrientation);

        if (bean.visualCount != -1) {
            setVisibleCount(bean.visualCount);
        }
        return this;
    }

    public TabFlowAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 设置某个item动画，不执行其他的
     *
     * @param position
     */
    public void setItemAnim(int position) {
        mLastIndex = mCurrentIndex;
        mCurrentIndex = position;
        if (mAction != null) {
            mAction.autoScaleView();
            mAction.doAnim(mLastIndex, mCurrentIndex, mTabBean.tabClickAnimTime);
        }
    }

    private boolean isItemClick;

    /**
     * 是否由item的点击事件引起的，长用于列表联动
     *
     * @return
     */
    public boolean isItemClick() {
        return isItemClick;
    }

    /**
     * 也可以用于自己去改变 itemclick 这个状态
     *
     * @param isClick
     */
    public void setItemClickStatus(boolean isClick) {
        isItemClick = isClick;
    }

    @Override
    public boolean isTabAutoScroll() {
        return mTabBean.isAutoScroll;
    }

    @Override
    public boolean isLabelFlow() {
        return false;
    }

    /**
     * 设置默认位置
     * 请使用{@link #setTabConfig(TabConfig)}
     */
    public TabFlowLayout setDefaultPosition(int position) {
        mCurrentIndex = position;
        return this;
    }

    public void setTabConfig(TabConfig config){
        if (config != null) {
            mTabConfig = config;
            if (config.getViewPager() != null) {
                mViewPager = config.getViewPager();
            }
            if (config.getViewPager2() != null) {
                mViewPager2 = config.getViewPager2();
            }
            if (config.getDefaultPos() != 0) {
                mCurrentIndex = config.getDefaultPos();
            }
            if (config.getVisibleCount() != -1) {
                setVisibleCount(config.getVisibleCount());
            }
            Log.d(TAG, "setTabConfig() called with: config = [" + config.toString() + "]");
        }else{
            mTabConfig = new TabConfig.Builder()
                    .setViewPager(mViewPager)
                    .setViewpager(mViewPager2)
                    .setDefaultPos(mCurrentIndex)
                    .build();

        }
        if (mAction != null) {
            mAction.setTabConfig(mTabConfig);
        }


    }

    /**
     * 设置viewpager，如果有多个配置，请使用{@link #setTabConfig(TabConfig)}
     */
    public TabFlowLayout setViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            return this;
        }
        mViewPager = viewPager;
        if (mAction != null) {
            mAction.setViewPager(viewPager);
        }
        return this;
    }


    /**
     * 设置viewpager，如果有多个配置，请使用{@link #setTabConfig(TabConfig)}
     */
    public TabFlowLayout setViewPager(ViewPager2 viewpager2){
        mViewPager2 = viewpager2;
        if (mAction != null){
            mAction.setViewPager(viewpager2);
        }
        return this;
    }

}
