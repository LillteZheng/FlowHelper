package com.zhengsr.tablib.view;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zhengsr.tablib.Constants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.TabAdapter;
import com.zhengsr.tablib.callback.AdapterListener;
import com.zhengsr.tablib.utils.ViewPagerHelperUtils;
import com.zhengsr.tablib.view.ScrollFlowLayout;
import com.zhengsr.tablib.view.cus.BaseAction;
import com.zhengsr.tablib.view.cus.RectAction;
import com.zhengsr.tablib.view.cus.RoundAction;
import com.zhengsr.tablib.view.cus.TriAction;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 实现数据封装与一些重绘工作
 */
public class TabFlowLayout extends ScrollFlowLayout implements AdapterListener {
    private static final String TAG = "TabFlowLayout";
    private TabAdapter mAdapter;
    private int mLastIndex = 0;
    private BaseAction mAction;

    public TabFlowLayout(Context context) {
        this(context, null);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabFlowLayout);
        int tabStyle = ta.getInteger(R.styleable.TabFlowLayout_tab_style, -1);


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
                default:
                    break;
            }
        }

        //配置自定义属性给 action
        if (mAction != null) {
            mAction.configAttrs(ta);
        }

        ta.recycle();

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

        View child = getChildAt(0);
        if (child != null) {
            //拿到第一个数据
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
        }
    }

    /**
     * 添加adapter，
     *
     * @param adapter
     */
    public void setAdapter(TabAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setListener(this);
        //实现数据更新
        notifyDataChanged();
    }


    public void setViewPager(ViewPager viewPager) {
        // viewPager.addOnPageChangeListener(null);
        // viewPager.addOnPageChangeListener(new ViewPagerListener());
        ViewPagerHelperUtils.initSwitchTime(getContext(), viewPager, 600);

        if (mAction != null) {
            mAction.setViewPager(viewPager);
        }
        //
    }

    @Override
    public void notifyDataChanged() {
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
                mAdapter.onItemClick(view, mAdapter.getDatas().get(i), i);
                if (mAction != null) {
                    mAction.onItemClick(mLastIndex, i);
                }
                mLastIndex = i;
            }
        });
    }


}
