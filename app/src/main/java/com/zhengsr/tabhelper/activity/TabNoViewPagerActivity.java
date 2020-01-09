package com.zhengsr.tabhelper.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tablib.view.adapter.TabAdapter;
import com.zhengsr.tablib.bean.TabTypeValue;
import com.zhengsr.tablib.view.flow.TabFlowLayout;
import com.zhengsr.tablib.view.action.BaseAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabNoViewPagerActivity extends AppCompatActivity {
    private List<String> mTitle = Arrays.asList("Java","Android","Kotlin");
    //Life is like an ocean. Only strong willed people can reach the other side
    private ArrayList<String> mTitle2 = new ArrayList<>(Arrays.asList("Life is like an ocean".split(" ")));
    private ArrayList<String> mTitle3 = new ArrayList<>(Arrays.asList("Life is like an ocean. Only strong willed people can reach the other side".split(" ")));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_no_view_pager);
        rectFlow();
        triFlow();
        roundFlow();
        resFlow();
        cusFlow();

    }

    private void rectFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.rectflow);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
                if (position == 0){
                    setVisible(view,R.id.item_msg,true);
                }
            }
        });

        TabFlowLayout flowLayout2 = findViewById(R.id.rectflow2);
        flowLayout2.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }
        });
    }

    private void triFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.triflow);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle2) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data)
                        .setTextColor(view,R.id.item_text,Color.BLACK);
                if (position == 0){
                    setTextColor(view,R.id.item_text,Color.WHITE);
                }
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                resetAllColor(R.id.item_text,Color.BLACK);
                setTextColor(view,R.id.item_text,Color.WHITE);
            }
        });
    }

    private void roundFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.roundflow);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle3) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data)
                        .setTextColor(view,R.id.item_text, Color.WHITE);
            }
        });
    }

    private void resFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.resflow);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle3) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data)
                        .setTextColor(view,R.id.item_text, Color.WHITE);
            }
        });
    }



    private void cusFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.cusflow);
        flowLayout.setCusAction(new CircleAction());
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle2) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data)
                        .setTextColor(view,R.id.item_text, Color.WHITE);
                if (position == 2){
                    setVisible(view,R.id.item_msg,true);
                }
            }
        });
    }

    /**
     * 绘制一个圆的指示器
     */
    class CircleAction extends BaseAction{
        private static final String TAG = "CircleAction";
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

}
