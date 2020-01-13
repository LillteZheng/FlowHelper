package com.zhengsr.tabhelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tabhelper.bean.BaseResponse;
import com.zhengsr.tabhelper.bean.NaviBean;
import com.zhengsr.tabhelper.bean.NaviChildrenBean;
import com.zhengsr.tabhelper.fragment.RecyclerFragment;
import com.zhengsr.tabhelper.rx.HttpCreate;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

public class NetTestActivity extends AppCompatActivity {
    private static final String TAG = "NetTestActivity";
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_test);

        TabFlowLayout flowLayout = findViewById(R.id.tabflow);
        final ViewPager viewPager  = findViewById(R.id.viewpager);


        final List<String> titles = new ArrayList<>();
        final List<Fragment> fragments = new ArrayList<>();


        final TabFlowAdapter adapter ;
        flowLayout.setViewPager(viewPager,R.id.item_text, getResources().getColor(R.color.unselect),Color.WHITE);
        flowLayout.setAdapter(adapter = new TabFlowAdapter<String>(R.layout.item_tab,titles) {

            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                viewPager.setCurrentItem(position);
            }
        });


        HttpCreate.getServer().getTreeKnowledge()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceObserver<BaseResponse<List<NaviBean>>>() {
                    @Override
                    public void onNext(BaseResponse<List<NaviBean>> baseResponse) {
                        List<NaviBean> data = baseResponse.getData();

                        for (NaviChildrenBean child : data.get(1).getChildren()) {
                            String title = child.getName().replaceAll("&amp;","和");
                            titles.add(title);
                            fragments.add(RecyclerFragment.newInstance(child));
                        }
                        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragments));
                            adapter.notifyDataChanged();


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "zsr - onError: "+e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });




    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;
        public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
