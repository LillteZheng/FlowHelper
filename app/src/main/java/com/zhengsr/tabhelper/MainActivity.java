package com.zhengsr.tabhelper;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.zhengsr.tabhelper.activity.LabelActivity;
import com.zhengsr.tabhelper.activity.LabelShowMoreActivity;
import com.zhengsr.tabhelper.activity.NetTestActivity;
import com.zhengsr.tabhelper.activity.TabActivity;
import com.zhengsr.tabhelper.activity.TabNoViewPagerActivity;
import com.zhengsr.tabhelper.activity.VerticalTabActivity;
import com.zhengsr.tabhelper.fragment.TestFragment;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> mFragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // startActivity(new Intent(this, LabelActivity.class));

        ViewPager viewPager = findViewById(R.id.viewpager);
        mFragments.add(TestFragment.newInStance());
        mFragments.add(TestFragment.newInStance());
        viewPager.setAdapter(new CusAdapter(getSupportFragmentManager()));

    }


    public void tablayout(View view) {
        startActivity(new Intent(this, TabActivity.class));
    }

    public void tablayout2(View view) {
        startActivity(new Intent(this, TabNoViewPagerActivity.class));
    }

    public void labelflow(View view) {
        startActivity(new Intent(this, LabelActivity.class));
    }

    public void tablayout3(View view) {
        startActivity(new Intent(this, NetTestActivity.class));
    }

    public void tablayout4(View view) {
        startActivity(new Intent(this,VerticalTabActivity.class));
    }

    public void labelflowshomore(View view) {
        startActivity(new Intent(this, LabelShowMoreActivity.class));
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
