package com.zhengsr.tabhelper.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tabhelper.bean.BaseResponse;
import com.zhengsr.tabhelper.bean.NaviChildrenBean;
import com.zhengsr.tabhelper.bean.SystematicBean;
import com.zhengsr.tabhelper.fragment.RecyclerFragment;
import com.zhengsr.tabhelper.rx.HttpCreate;
import com.zhengsr.tablib.bean.TabConfig;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabVpFlowLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportActivity;

public class NetTestActivity extends BaseActivity {
    private static final String TAG = "NetTestActivity";
    private ViewPager mViewPager;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_test);

        final TabVpFlowLayout flowLayout = findViewById(R.id.tabflow);
        mViewPager = findViewById(R.id.viewpager);

        mViewPager.setOffscreenPageLimit(3);
        final List<String> titles = new ArrayList<>();
        final List<Fragment> fragments = new ArrayList<>();


        final TabFlowAdapter adapter ;


        TabConfig config = new TabConfig.Builder()
                .setViewPager(mViewPager)
                .setTextId(R.id.item_text)
                .setDefaultPos(2)
                .setVisibleCount(4)
                .build();

        flowLayout.setAdapter(config,adapter = new TabFlowAdapter<String>(R.layout.item_tab,titles) {

            @Override
            public void bindView(View view, String data, int position) {
                setDefaultText(view,data);
            }
        });


        HttpCreate.getServer().getTreeKnowledge()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceObserver<BaseResponse<List<SystematicBean>>>() {
                    @Override
                    public void onNext(BaseResponse<List<SystematicBean>> baseResponse) {
                        List<SystematicBean> data = baseResponse.getData();
                        // 可以通过 page 获取不同的参数
                        int page = 1;
                        for (NaviChildrenBean child : data.get(page).getChildren()) {
                            String title = child.getName().replaceAll("&amp;","和");
                            titles.add(title);
                            fragments.add(RecyclerFragment.newInstance(child));
                        }
                        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragments));
                        //刷新数据
                    //    adapter.notifyDataChanged();
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

    public void test(View view) {
        recreate();
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
