package com.zhengsr.tabhelper.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tabhelper.fragment.CusFragment;
import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabConfig;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.action.BaseAction;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabVpFlowLayout;
import com.zhengsr.tablib.view.flow.TabVpFlowLayout;
import com.zhengsr.tablib.view.flow.base.AbsFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabActivity extends BaseActivity {
    private static final String TAG = "TabActivity";
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitle = new ArrayList<>(Arrays.asList("Life is like an ocean Only strong willed people can reach the other side".split(" ")));

    private ViewPager2 mViewPager;
    private CusAdapter2 mViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        mViewPager = findViewById(R.id.viewpager);
        for (String s : mTitle) {
            mFragments.add(CusFragment.newInStance(s));
        }
        mViewPager.setAdapter(mViewAdapter = new CusAdapter2(this));
        mViewPager.setOffscreenPageLimit(3);
        rectFlow();
        triFlow();
        roundFlow();
        resFlow();
        colorFlow();
        cusFlow();


    }

    boolean isDetele = false;
    private void rectFlow(){
        final TabVpFlowLayout flowLayout = findViewById(R.id.rectflow);
       // flowLayout.setViewPager(mViewPager,R.id.item_text,getResources().getColor(R.color.unselect),Color.WHITE);
        TabConfig config = new TabConfig.Builder()
                .setViewpager(mViewPager)
                .setTextId(R.id.item_text)
                .setSelectedColor(Color.WHITE)
                .setUnSelectColor(getResources().getColor(R.color.unselect))
                .build();
        flowLayout.setAdapter(config,new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mTitle.set(position,data+position);
            }
        });


        TabVpFlowLayout flowLayout2 = findViewById(R.id.rectflow2);
        flowLayout2.setAdapter(config,new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data);
                if (position == 0){
                    setVisible(view, R.id.item_msg,true);
                }
            }

        });

    }

    private void triFlow(){
        TabVpFlowLayout flowLayout = findViewById(R.id.triflow);
        flowLayout.setViewPager(mViewPager);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data);
            }

        });
    }
    private void roundFlow(){
        TabVpFlowLayout flowLayout = findViewById(R.id.roundflow);
        TabConfig config = new TabConfig.Builder()
                .setViewpager(mViewPager)
                .setTextId(R.id.item_text)
                .setSelectedColor(Color.WHITE)
                .setUnSelectColor(getResources().getColor(R.color.unselect))
                .build();
        flowLayout.setTabConfig(config);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
    }
    private void resFlow(){
        final TabVpFlowLayout flowLayout = findViewById(R.id.resflow);

        /**
         * 配置自定义属性
         */

        TabBean bean = new TabBean();
        bean.tabType = FlowConstants.RES;
        bean.tabItemRes = R.drawable.shape_round;
        bean.tabClickAnimTime = 300;
        bean.tabMarginLeft = 30;
        bean.tabMarginTop = 12;
        bean.tabMarginRight = 5;
        bean.tabMarginBottom = 10;
        bean.autoScale = true;
        bean.scaleFactor = 1.2f;
        flowLayout.setTabBean(bean);

        flowLayout.setViewPager(mViewPager);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
    }

    private void colorFlow(){
        TabVpFlowLayout flowLayout = findViewById(R.id.colorflow);
        TabConfig config = new TabConfig.Builder()
                .setViewpager(mViewPager)
                .setTextId(R.id.item_text)
                .build();

        flowLayout.setAdapter(config,new TabFlowAdapter<String>(R.layout.item_color_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
    }

    private void cusFlow(){
        TabVpFlowLayout flowLayout = findViewById(R.id.cusflow);
        flowLayout.setCusAction(new CircleAction());
        flowLayout.setViewPager(mViewPager);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data)
                        .setTextColor(view, R.id.item_text, Color.WHITE);
                if (position == 2){
                    setVisible(view, R.id.item_msg,true);
                }
            }
            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                mViewPager.setCurrentItem(position);
            }
        });
    }

    /**
     * 绘制一个圆的指示器
     */
    class CircleAction extends BaseAction {
        @Override
        public void config(AbsFlowLayout parentView) {
            super.config(parentView);
            View child = parentView.getChildAt(0);
            if (child != null) {
                float l = parentView.getPaddingLeft() + child.getMeasuredWidth()/2;
                float t = parentView.getPaddingTop() +  child.getMeasuredHeight() - mTabBean.tabHeight/2 -mTabBean.tabMarginBottom;
                float r = mTabBean.tabWidth + l;
                float b = child.getMeasuredHeight() - mTabBean.tabMarginBottom;
                mTabRect.set(l,t,r,b);
            }
        }


        @Override
        protected void valueChange(TabValue value) {
            super.valueChange(value);
            //由于自定义的，都是从left 开始算起的，所以这里还需要加上圆的半径
            mTabRect.left = value.left + mTabBean.tabWidth/2;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mTabRect.left, mTabRect.top,mTabBean.tabWidth/2,mPaint);
        }
    }

    class CusAdapter2 extends FragmentStateAdapter{

        public CusAdapter2(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return CusFragment.newInStance(mTitle.get(position));
        }


        @Override
        public int getItemCount() {
            return mFragments.size();
        }
    }
}
