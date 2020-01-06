package com.zhengsr.tabhelper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tabhelper.fragment.CusFragment;
import com.zhengsr.tablib.TabAdapter;
import com.zhengsr.tablib.view.TabFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabActivity extends AppCompatActivity {
    private static final String TAG = "TabActivity";
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitle = new ArrayList<>(Arrays.asList("An Android TabLayout Lib has 3 kinds of TabLayout at present.".split(" ")));
    private TabFlowAdapter mAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        for (String string : mTitle) {
            CusFragment fragment = CusFragment.newInStance(string);
            mFragments.add(fragment);
        }
        //viewpager
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(new CusAdapter(getSupportFragmentManager()));

        // tabflowlayout
        TabFlowLayout tabFlowLayout = findViewById(R.id.tabflow);
        mAdapter = new TabFlowAdapter(R.layout.item_tab_text, mTitle);
        tabFlowLayout.setViewPager(mViewPager);
        tabFlowLayout.setAdapter(mAdapter);
    }

    public void update(View view) {
        mTitle.clear();
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("Life is like an ocean. Only strong willed people can reach the other side".split(" ")));
        mTitle.addAll(strings);
        mAdapter.notifyDataChanged();
    }

    /**
     * tabadapter
     */
    class TabFlowAdapter extends TabAdapter<String>{

        public TabFlowAdapter(int layoutId, List<String> data) {
            super(layoutId, data);
        }

        @Override
        public void bindView(View view, String data, int position) {
            setText(view,R.id.item_text,data);
        }

        @Override
        public void onItemClick(View view,String data,int position) {
            super.onItemClick(view,data,position);
            mViewPager.setCurrentItem(position);
        }
    }

    /**
     * viewpager adapter
     */
    class CusAdapter extends FragmentPagerAdapter {

        public CusAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
