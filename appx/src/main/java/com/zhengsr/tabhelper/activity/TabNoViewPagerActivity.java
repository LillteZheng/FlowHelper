package com.zhengsr.tabhelper.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.action.BaseAction;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

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
        flowLayout.setDefaultPosition(2);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void onItemSelectState(View view, boolean isSelected) {
                super.onItemSelectState(view, isSelected);
                if (isSelected){
                    setTextColor(view, R.id.item_text,Color.WHITE);
                }else{
                    setTextColor(view, R.id.item_text,getResources().getColor(R.color.unselect));
                }
            }

            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data)
                        .setTextColor(view, R.id.item_text,getResources().getColor(R.color.unselect));
                if (position == 0){
                    setVisible(view, R.id.item_msg,true);
                }


            }
        });

        TabFlowLayout flowLayout2 = findViewById(R.id.rectflow2);
        flowLayout2.setAdapter(new TabFlowAdapter<String>(R.layout.item_test,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data);
            }
        });
    }

    private void triFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.triflow);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle2) {
            /**
             * 绑定数据，可以使用 setText(..) 等快捷方式，也可以视同 view.findViewById()
             * 同时，当你的子控件需要点击事件时，可以通过  addChildrenClick() 注册事件，
             * 然后重写 onItemChildClick(..) 即可拿到事件，否则就自己写。
             */
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data)
                        .setTextColor(view, R.id.item_text,Color.BLACK);
                if (position == 0){
                    setTextColor(view, R.id.item_text,Color.WHITE);
                }
                // 注册子控件的点击事件
                //addChildrenClick(view,R.id.item_text,position);
                //注册子控件的长按事件
                //addChildrenLongClick(view,R.id.item_text,position);
            }

            @Override
            public void onItemSelectState(View view, boolean isSelected) {
                super.onItemSelectState(view, isSelected);
                if (isSelected){
                    setTextColor(view, R.id.item_text,Color.WHITE);
                }else{
                    setTextColor(view, R.id.item_text,getResources().getColor(R.color.black));
                }
            }

        });
    }

    private void roundFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.roundflow);
        TabBean bean = new TabBean();
        bean.tabType = FlowConstants.ROUND;
        bean.tabColor = Color.parseColor("#b01a1a1a");
        bean.tabMarginLeft = 5;
        bean.tabMarginTop = 12;
        bean.tabMarginRight = 5;
        bean.tabMarginBottom = 10;
        bean.tabRoundSize = 10;
        flowLayout.setTabBean(bean);

        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle3) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data)
                        .setTextColor(view, R.id.item_text, Color.WHITE);
            }
        });
    }

    private void resFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.resflow);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle3) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data)
                        .setTextColor(view, R.id.item_text, Color.WHITE);
            }
        });
    }



    private void cusFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.cusflow);
        flowLayout.setCusAction(new CircleAction());
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle2) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data)
                        .setTextColor(view, R.id.item_text, Color.WHITE);
                if (position == 2){
                    setVisible(view, R.id.item_msg,true);
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
                float t = parentView.getPaddingTop() +  child.getMeasuredHeight() - mTabBean.tabHeight/2 -mTabBean.tabMarginBottom;
                float r = mTabBean.tabWidth + l;
                float b = child.getMeasuredHeight() - mTabBean.tabMarginBottom;
                mTabRect.set(l,t,r,b);
            }
        }


        @Override
        protected void valueChange(TabValue value) {
            super.valueChange(value);

            /**
             * value 子控件在滚动时的 left 和 right，可以理解为偏移量
             * Rect 为这个偏移量的局域。
             */
            //由于自定义的，都是从left 开始算起的，所以这里还需要加上圆的半径
            mTabRect.left = value.left + mTabBean.tabWidth/2;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mTabRect.left, mTabRect.top,mTabBean.tabWidth/2,mPaint);
        }
    }

}
