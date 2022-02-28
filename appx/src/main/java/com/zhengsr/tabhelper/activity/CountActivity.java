package com.zhengsr.tabhelper.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tabhelper.fragment.CusFragment;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabVpFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author by zhengshaorui 2021/4/22 16:31
 * describe：
 */
public class CountActivity extends BaseActivity {
    private List<String> mTitle = new ArrayList<>(Arrays.asList("test1","test2"));
    private List<Fragment> mFragments = new ArrayList<>();
    private ViewPager2 mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        mViewPager = findViewById(R.id.viewpager2);
        for (String s : mTitle) {
            mFragments.add(CusFragment.newInStance(s));
        }
        mViewPager.setAdapter(new CusAdapter2(this));
        rectFlow();
    }

    private void rectFlow(){
        final TabVpFlowLayout flowLayout = findViewById(R.id.rectflow);
        // flowLayout.setViewPager(mViewPager,R.id.item_text,getResources().getColor(R.color.unselect),Color.WHITE);
        flowLayout.setViewPager(mViewPager);
        flowLayout.setAdapter(new TabFlowAdapter<String>(mTitle));




    }

    class CusAdapter2 extends FragmentStateAdapter {

        public CusAdapter2(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragments.size();
        }
    }
}
