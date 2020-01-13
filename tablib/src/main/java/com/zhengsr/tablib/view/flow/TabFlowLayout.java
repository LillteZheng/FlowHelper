package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.view.action.ColorAction;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.callback.FlowListenerAdapter;
import com.zhengsr.tablib.utils.ViewPagerHelperUtils;
import com.zhengsr.tablib.view.action.BaseAction;
import com.zhengsr.tablib.view.action.RectAction;
import com.zhengsr.tablib.view.action.ResAction;
import com.zhengsr.tablib.view.action.RoundAction;
import com.zhengsr.tablib.view.action.TriAction;

import java.lang.reflect.Field;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 实现数据封装与一些重绘工作
 */
public class TabFlowLayout extends ScrollFlowLayout {
    private static final String TAG = "TabFlowLayout";
    private TabFlowAdapter mAdapter;
    private BaseAction mAction;

    private boolean isFirst = true;
    private TypedArray mTypeArray;
    private TabBean mTabBean;

    /**
     * 滚动
     */
    private Scroller mScroller;
    private int mLastScrollX = 0;
    private int mLastIndex = 0;
    private int mCurrentIndex = 0;

    /**
     * viewpager 相关
     *
     */
    private ViewPager mViewPager;
    private int mTextId = -1;
    private int mSelectedColor = -1;
    private int mUnselectedColor = -1;

    public TabFlowLayout(Context context) {
        this(context, null);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.TabFlowLayout);
        int tabStyle = mTypeArray.getInteger(R.styleable.TabFlowLayout_tab_type, -1);
        mScroller = new Scroller(getContext());
        chooseTabTpye(tabStyle);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                reAdjustLayoutParams();

                /**
                 *  当横竖屏之后，需要重新对位置，选中 index 等恢复到原来的状态
                 */
                if (isFirst) {
                    isFirst = false;
                    if (mViewPager != null) {
                        if (mAction != null) {
                            mAction.chooseSelectedPosition(mCurrentIndex);
                            if (mCurrentIndex != mLastIndex) {
                                mAction.doAnim(mLastIndex, mCurrentIndex);
                            }
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

    /**
     * 如果超过了屏幕大小，且父布局是 LinearLayout ，gravity 或 自身的 layout_gravity 不是 left；
     * 则需要自身去重新设置，不然初始位置是在中间开始去layout的。
     * 如果是 ConstraintLayout ，width 又是 wrap_content 的，只需要改成0即可
     */
    private void reAdjustLayoutParams() {
        if (getWidth() > mWidth) {
            ViewGroup parent = (ViewGroup) getParent();
            if (parent instanceof LinearLayout) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
                params.gravity = Gravity.START;
                setLayoutParams(params);
            }else if (parent instanceof ConstraintLayout){
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) getLayoutParams();
                boolean isWrapContent = params.width == ConstraintLayout.LayoutParams.WRAP_CONTENT;
                if (isWrapContent && isCanMove()){
                    params.width = 0;
                    setLayoutParams(params);
                }
            }
        }
    }


    /**
     * 选中不同的 action
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
        if (mAction != null && !isTypeArrayRecycler()) {
            mAction.configAttrs(mTypeArray);
            mTypeArray.recycle();
        }

    }

    /**
     * 判断typeArray 是否被回收
     * @return
     */
    private boolean isTypeArrayRecycler(){
        if (mTypeArray != null) {
            try {
                Class<?> typeArrayClass = mTypeArray.getClass();
                Field mRecycled  = typeArrayClass.getDeclaredField("mRecycled");
                mRecycled.setAccessible(true);
                return mRecycled.getBoolean(mTypeArray);

            } catch (Exception e) {
                //e.printStackTrace();
                return false;

            }
        }
        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mAction != null) {
            mAction.draw(canvas);
        }
        super.dispatchDraw(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mAction != null) {
            mAction.config(TabFlowLayout.this);
        }
    }

    /**
     * 添加adapter，
     *
     * @param adapter
     */
    public void setAdapter(TabFlowAdapter adapter) {
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

        if (mAction != null){
            if (mViewPager != null && mAction.getViewPager() == null){
                mAction.setViewPager(mViewPager,mTextId,mUnselectedColor,mSelectedColor);
            }
        }
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


    public void setViewPager(ViewPager viewPager, int textId) {
        setViewPager(viewPager, textId, 0, -1, -1);
    }
    public void setViewPager(ViewPager viewPager, int textId, int unselectedColor, int selectedColor) {
        setViewPager(viewPager, textId, 0, unselectedColor, selectedColor);
    }


    /**
     * 配置viewpager
     * @param viewPager
     * @param textId  view 中 textview 的id
     * @param selectedIndex 默认选中的item，初始值为0，也可以从第二页或者其他 位置
     * @param unselectedColor 没有选中的颜色 ColorTextView 中失效
     * @param selectedColor 选中的颜色 ColorTextView 中失效
     */
    public void setViewPager(ViewPager viewPager, int textId, int selectedIndex, int unselectedColor, int selectedColor) {
        if (viewPager != null) {
            mViewPager = viewPager;
            ViewPagerHelperUtils.initSwitchTime(getContext(), viewPager, 600);
            if (mAction != null) {
                mAction.setViewPager(viewPager, textId, unselectedColor, selectedColor);
            }
        }
        mSelectedColor = selectedColor;
        mUnselectedColor = unselectedColor;
        mTextId = textId;
        mCurrentIndex = selectedIndex;

        if (mAction != null && !isTypeArrayRecycler()) {
            if (mTabBean != null) {
                mAction.configAttrs(mTypeArray);
                mTypeArray.recycle();
                mAction.setBean(mTabBean);
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

        //如果此时 width 为0，则是加载完布局，但是数据还没有导入，则需要重新适配一下；
        if (mWidth == 0 && getWidth() == 0){
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    reAdjustLayoutParams();
                }
            },5);
        }
    }


    /**
     * 配置 点击和长按事件
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
                if (mAdapter != null) {
                    mAdapter.onItemClick(view, mAdapter.getDatas().get(i), i);
                }
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

        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mAdapter != null) {
                    return mAdapter.onItemLongClick(view,i);
                }
                return false;
            }
        });
    }

    /**
     * 更新滚动
     * @param view
     */
    private void updateScroll(View view) {
        if (isCanMove()) {
            //超过中间了，让父控件也跟着移动
            int scrollX = view.getLeft();
            int dx;
            if (scrollX > mWidth / 2 - getPaddingLeft()) {
                scrollX -= mWidth / 2 - getPaddingLeft();
                //有边界提醒
                if (scrollX < mRightBound - mWidth) {
                     dx = scrollX - mLastScrollX;
                    mScroller.startScroll(getScrollX(), 0, dx, 0);
                    mLastScrollX = scrollX;
                    Log.d(TAG, "zsr - updateScroll r: "+dx+" "+mLastScrollX+" "+scrollX);
                } else {
                    dx = mRightBound - mWidth - getScrollX();
                    if (getScrollX() >= mRightBound - mWidth) {
                        dx = 0;
                    }
                    mScroller.startScroll(getScrollX(), 0, dx, 0);
                    mLastScrollX = mRightBound - mWidth - dx;
                }
            } else {
                dx = - scrollX;
                mScroller.startScroll(getScrollX(),0,dx,0);
                mLastScrollX = 0;
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            //有边界
            int dx = mScroller.getCurrX();
            if (dx >= mRightBound - mWidth){
                dx = mRightBound - mWidth;
            }
            if (dx <= 0){
                dx = 0;
            }
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

    /**
     * 自定义属性的配置
     */

    public TabFlowLayout setTabBean(TabBean bean){
        mTabBean = bean;
        if (bean == null) {
            return this;
        }
        if (bean.tabType != -1) {
            chooseTabTpye(bean.tabType);
        }

        if (mAction != null) {
            if (mTabBean != null) {
                mAction.setBean(mTabBean);
                if (mViewPager != null && mAction.getViewPager() == null){
                    mAction.setViewPager(mViewPager,mTextId,mUnselectedColor,mSelectedColor);
                }
            }
        }

        return this;
    }

    public TabFlowAdapter getAdapter() {
        return mAdapter;
    }
}
