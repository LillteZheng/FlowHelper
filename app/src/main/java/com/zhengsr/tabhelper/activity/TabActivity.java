package com.zhengsr.tabhelper.activity;

import android.graphics.Color;
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
    private List<String> mTitle = new ArrayList<>(Arrays.asList("Life is like an ocean Only strong willed people can reach the other side".split(" ")));

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        mViewPager = findViewById(R.id.viewpager);
        for (String s : mTitle) {
            mFragments.add(CusFragment.newInStance(s));
        }
        mViewPager.setAdapter(new CusAdapter(getSupportFragmentManager()));
        rectFlow();
       // triFlow();
       // roundFlow();
        //resFlow();

    }

    private void rectFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.rectflow);
        flowLayout.setViewPager(mViewPager,R.id.item_text,getResources().getColor(R.color.unselect),Color.WHITE);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });

        TabFlowLayout flowLayout2 = findViewById(R.id.rectflow2);
        flowLayout2.setViewPager(mViewPager,R.id.item_text,getResources().getColor(R.color.unselect),Color.WHITE);
        flowLayout2.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
                if (position == 0){
                    setVisiable(view,R.id.item_msg,true);
                }
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);

            }
        });

    }

    private void triFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.triflow);
        flowLayout.setViewPager(mViewPager,R.id.item_text,getResources().getColor(R.color.unselect),Color.WHITE);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
    }
    private void roundFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.roundflow);
        flowLayout.setViewPager(mViewPager,R.id.item_text,getResources().getColor(R.color.unselect),Color.WHITE);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
    }
    private void resFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.resflow);
        flowLayout.setViewPager(mViewPager);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
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
