package com.zhengsr.tablib.view.action;

import android.util.Log;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @author by zhengshaorui 2020/9/13 10:26
 * describe：
 */
public class BViewPager {
    private static final String TAG = "BViewPager";
    protected ViewPager2 mViewPager2;
    protected ViewPager mViewPager;
    private Pager2Listener mPager2Listener;
    public BViewPager setViewPager(ViewPager2 viewPager){
        if (viewPager != null) {
            mViewPager2 = viewPager;
            if (mPager2Listener != null) {
                mViewPager2.unregisterOnPageChangeCallback(mPager2Listener);
            }
            mViewPager2.registerOnPageChangeCallback(mPager2Listener = new Pager2Listener());
        }

        return this;
    }

    public BViewPager setViewPager(ViewPager viewPager){
        if (viewPager != null) {
            mViewPager = viewPager;
            mViewPager.addOnPageChangeListener(null);
            mViewPager.addOnPageChangeListener(new PagerListener());
        }
        return this;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    private class PagerListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            BViewPager.this.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            BViewPager.this.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            BViewPager.this.onPageScrollStateChanged(state);
        }
    }

    private class Pager2Listener extends ViewPager2.OnPageChangeCallback{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            BViewPager.this.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            BViewPager.this.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            BViewPager.this.onPageScrollStateChanged(state);
        }
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    public void onPageSelected(int position) {

    }

    public void onPageScrollStateChanged(int state) {

    }
}
