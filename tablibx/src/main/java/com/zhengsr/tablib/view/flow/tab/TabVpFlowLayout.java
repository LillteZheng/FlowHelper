package com.zhengsr.tablib.view.flow.tab;

import android.content.Context;
import android.view.View;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.zhengsr.tablib.bean.TabConfig;
import com.zhengsr.tablib.view.flow.AbsFlowLayout;

/**
 * @author by zhengshaorui 2021/5/23 06:51
 * describe：不处理ViewPager的情况
 */
public class TabVpFlowLayout extends AbsFlowLayout {
    private ViewPager mViewPager;
    private ViewPager2 mViewPager2;
    private int mCurrentIndex = 0;
    public TabVpFlowLayout(Context context) {
        super(context);
    }

    @Override
    protected void onViewVisible() {
        super.onViewVisible();
        if (mCurrentIndex != 0) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(mCurrentIndex,false);
            }
            if (mViewPager2 != null) {
                mViewPager2.setCurrentItem(mCurrentIndex,false);
            }
        }
    }

    @Override
    protected void onTabConfig(TabConfig config) {
        super.onTabConfig(config);
        if (config.getViewPager2() != null) {
            mViewPager2 = config.getViewPager2();
        }
        if (config.getViewPager() != null) {
            mViewPager = config.getViewPager();
        }
    }

    @Override
    protected void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }
        if (mViewPager2 != null) {
            mViewPager2.setCurrentItem(position);
        }
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
