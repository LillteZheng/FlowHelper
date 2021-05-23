package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

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
 * @author by zhengshaorui 2021/5/23 06:40
 * describe：用来获取通用的自定义属性，和一些常用的配置
 */
public abstract class AbsFlowLayout extends ScrollFlowLayout {
    private static final String TAG = "AttrFlowLayout";


    /**
     * attrs
     */
    protected TabBean mTabBean;

    /**
     * logic
     */
    protected BaseAction mAction;
    private TabConfig mTabConfig;
    private TabFlowAdapter mAdapter;

    public AbsFlowLayout(Context context) {
        this(context, null);
    }

    public AbsFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AbsFlowLayout);
        mTabBean = AttrsUtils.getTabBean(ta);
        setVisibleCount(mTabBean.visualCount);
        setTabOrientation(mTabBean.tabOrientation);
        chooseTabTpye(mTabBean.tabType);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                reAdjustLayoutParams();
                onViewVisible();
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
        //绘制tab标签
        if (mAction != null) {
            mAction.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }

    /**
     * 添加adapter，
     *
     * @param adapter
     */
    public void setAdapter(TabFlowAdapter adapter) {
        setAdapter(null, adapter);
    }

    public void setAdapter(TabConfig tabConfig, TabFlowAdapter adapter) {
        mAdapter = adapter;
        setTabConfig(tabConfig);
        // mAdapter = adapter;
        adapter.setListener(new FlowListener());
        //实现数据更新
        notifyChanged(adapter);
    }

    private void notifyChanged(final TabFlowAdapter adapter) {
        if (adapter == null) {
            return;
        }
        removeAllViews();
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            View view = LayoutInflater.from(getContext()).inflate(adapter.getLayoutId(), this, false);
            if (mVisibleCount != -1) {
                MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
                params.width = (int) (mViewWidth * 1.0f / mVisibleCount);
                view.setLayoutParams(params);
            }

            adapter.bindView(view, adapter.getDatas().get(i), i);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(view, finalI);
                    adapter.onItemClick(view, adapter.getDatas().get(finalI), finalI);
                }
            });
            addView(view);
        }
    }


    class FlowListener extends FlowListenerAdapter {
        @Override
        public void notifyDataChanged() {
            super.notifyDataChanged();
            final TabFlowAdapter adapter = mAdapter;
            //一开始没有数据，直接刷新即可
            int childCount = getChildCount();
            int dataSize = adapter.getDatas().size();
            if (childCount != 0 && childCount == dataSize) {
                //数据变化
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    adapter.bindView(view, adapter.getDatas().get(i), i);
                }
            } else {
                notifyChanged(mAdapter);
            }
        }
    }


    public void setTabConfig(TabConfig config) {
        mTabConfig = config;
        if (config != null) {
            if (config.getVisibleCount() != -1) {
                setVisibleCount(config.getVisibleCount());
            }
            Log.d(TAG, "setTabConfig() called with: config = [" + config.toString() + "]");
        }
        onTabConfig(mTabConfig);
        if (mAction != null) {
            mAction.setTabConfig(mTabConfig);
        }
    }

    public TabFlowAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public boolean isLabelFlow() {
        return false;
    }

    /**
     * 此时View可用
     */
    protected void onViewVisible() {
    }

    protected void onTabConfig(TabConfig config) {
    }

    protected void onItemClick(View view, int position) {
    }

}
