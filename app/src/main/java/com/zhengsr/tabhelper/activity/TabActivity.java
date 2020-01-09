package com.zhengsr.tabhelper.activity;

import android.graphics.Canvas;
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
import com.zhengsr.tablib.view.adapter.TabAdapter;
import com.zhengsr.tablib.bena.TabTypeValue;
import com.zhengsr.tablib.view.flow.TabFlowLayout;
import com.zhengsr.tablib.view.action.BaseAction;

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
        //rectFlow();
        //triFlow();
        //roundFlow();
        //resFlow();
        colorFlow();
        //cusFlow();

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
                    setVisible(view,R.id.item_msg,true);
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

    private void colorFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.colorflow);
        flowLayout.setViewPager(mViewPager,R.id.item_text,getResources().getColor(R.color.unselect),Color.RED);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_color_msg,mTitle) {
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

    private void cusFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.cusflow);
        flowLayout.setCusAction(new CircleAction());
        //设置viewpager 要再 action之后
        flowLayout.setViewPager(mViewPager);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data)
                        .setTextColor(view,R.id.item_text, Color.WHITE);
                if (position == 2){
                    setVisible(view,R.id.item_msg,true);
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
        public void config(TabFlowLayout parentView) {
            super.config(parentView);
            View child = parentView.getChildAt(0);
            if (child != null) {
                float l = parentView.getPaddingLeft() + child.getMeasuredWidth()/2;
                float t = parentView.getPaddingTop() +  child.getMeasuredHeight() - mTabHeight/2 -mMarginBottom;
                float r = mTabWidth + l;
                float b = child.getMeasuredHeight() - mMarginBottom;
                mRect.set(l,t,r,b);
            }
        }


        @Override
        protected void valueChange(TabTypeValue value) {
            super.valueChange(value);
            //由于自定义的，都是从left 开始算起的，所以这里还需要加上圆的半径
            mRect.left = value.left + mTabWidth/2;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mRect.left,mRect.top,mTabWidth/2,mPaint);
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
