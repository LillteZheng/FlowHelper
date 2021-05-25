package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabConfig;
import com.zhengsr.tablib.view.action.BaseAction;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;

/**
 * @author by zhengshaorui 2021/5/23 06:51
 * describe：不处理ViewPager的情况
 */
public class TabVpFlowLayout extends AbsFlowLayout {
    private ViewPager mViewPager;
    private ViewPager2 mViewPager2;

    public TabVpFlowLayout(Context context) {
        super(context);
    }

    public TabVpFlowLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabVpFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onViewVisible() {
        super.onViewVisible();
        mAction.chooseIndex(mLastIndex,mCurrentIndex);
        if (mViewPager != null) {
            mViewPager.setCurrentItem(mCurrentIndex,false);
        }
        if (mViewPager2 != null) {
            mViewPager2.setCurrentItem(mCurrentIndex,false);
        }
        View view = getChildAt(mCurrentIndex);
        if (view != null) {
            updateScroll(view,false);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        //让tab标签回到最后一个
        if (childCount > 0 && (childCount - 1) < mCurrentIndex) {
            int tem = mCurrentIndex;
            mCurrentIndex = childCount - 1;
            mAction.chooseIndex(tem, mCurrentIndex);
            postInvalidate();
        }
    }

    @Override
    public void setCusAction(BaseAction action) {
        super.setCusAction(action);
        if (mViewPager != null && mAction.getViewPager() == null) {
            mAction.setViewPager(mViewPager);
        }
        if (mViewPager2 != null && mAction.getViewPager2() == null) {
            mAction.setViewPager(mViewPager2);
        }
    }

    @Override
    public AbsFlowLayout setTabBean(TabBean bean) {
        if (mViewPager != null && mAction.getViewPager() == null) {
            mAction.setViewPager(mViewPager);
        }
        if (mViewPager2 != null && mAction.getViewPager2() == null) {
            mAction.setViewPager(mViewPager2);
        }
        return super.setTabBean(bean);

    }

    @Override
    protected void onTabConfig(TabConfig config) {
        super.onTabConfig(config);
        if (config == null) {
            return;
        }
        if (config.getViewPager2() != null) {
            mViewPager2 = config.getViewPager2();
        }
        if (config.getViewPager() != null) {
            mViewPager = config.getViewPager();
        }
        mCurrentIndex = config.getDefaultPos();
    }


    @Override
    protected void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position,true);
        }
        if (mViewPager2 != null) {
            mViewPager2.setCurrentItem(position,true);
        }
        mLastIndex = mCurrentIndex;
        mCurrentIndex = position;
    }

    /**
     * 设置viewpager，如果有多个配置，请使用{@link #setTabConfig(TabConfig)}
     */
    public TabVpFlowLayout setViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            return this;
        }
        mViewPager = viewPager;
        if (mAction != null) {
            mAction.setViewPager(viewPager);
        }
        return this;
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
     * 设置viewpager，如果有多个配置，请使用{@link #setTabConfig(TabConfig)}
     */
    public TabVpFlowLayout setViewPager(ViewPager2 viewpager2){
        mViewPager2 = viewpager2;
        if (mAction != null){
            mAction.setViewPager(viewpager2);
        }
        return this;
    }
    /**
     * 设置默认位置
     * 请使用{@link #setTabConfig(TabConfig)}
     */
    public AbsFlowLayout setDefaultPosition(int position) {
        mCurrentIndex = position;
        return this;
    }
}
