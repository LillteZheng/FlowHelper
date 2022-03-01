package com.zhengsr.tabhelper.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabConfig;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.action.BaseAction;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;
import com.zhengsr.tablib.view.flow.base.AbsFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabNoViewPagerActivity extends BaseActivity {
    private static final String TAG = "TabNoViewPagerActivity";
    private List<String> mTitle = new ArrayList<>();
    //Life is like an ocean. Only strong willed people can reach the other side
    private ArrayList<String> mTitle2 = new ArrayList<>(Arrays.asList("Life is like an ocean".split(" ")));
    private ArrayList<String> mTitle3 = new ArrayList<>(Arrays.asList("Life is like an ocean. Only strong willed people can reach the other side".split(" ")));
    private TabFlowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.add("Java");
        mTitle.add("Android");
        setContentView(R.layout.activity_tab_no_view_pager);
        TabFlowLayout flowLayout = findViewById(R.id.new_test);
        TabConfig config = new TabConfig.Builder()
                .setTextId(R.id.item_text)
                .build();
        flowLayout.setAdapter(new TabFlowAdapter<String>( mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setDefaultText(view, data);
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                if (!isDetele) {
                    mTitle.set(0, "增加");
                } else {
                    mTitle.set(0, "减少");
                }
                isDetele = !isDetele;
                notifyDataChanged();
            }
        });
        rectFlow();
        triFlow();
        roundFlow();
        resFlow();
        cusFlow();

    }

    boolean isDetele = false;
    public void refresh(View view) {
        if (!isDetele) {
            mTitle.add("fsdf");
            mTitle.set(0, "增加");
        } else {
            mTitle.set(0, "减少");
            mTitle.remove(mTitle.size() - 1);
        }
        isDetele = !isDetele;
        mAdapter.notifyDataChanged();

    }

    private void rectFlow() {
        TabFlowLayout flowLayout = findViewById(R.id.rectflow);
        mAdapter = new TabFlowAdapter(mTitle);
        flowLayout.setAdapter(mAdapter);

        TabFlowLayout flowLayout2 = findViewById(R.id.rectflow2);
        flowLayout2.setAdapter(new TabFlowAdapter<String>( mTitle3));
    }


    private void triFlow() {
        TabFlowLayout flowLayout = findViewById(R.id.triflow);
        flowLayout.setAdapter(new TabFlowAdapter<String>(mTitle2));
    }

    private void roundFlow() {
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

        flowLayout.setAdapter(new TabFlowAdapter<String>( mTitle3));
    }

    private void resFlow() {
        TabFlowLayout flowLayout = findViewById(R.id.resflow);
        flowLayout.setAdapter(new TabFlowAdapter<String>( mTitle3) );
    }


    private void cusFlow() {
        TabFlowLayout flowLayout = findViewById(R.id.cusflow);
        flowLayout.setCusAction(new CircleAction());
        flowLayout.setAdapter(new TabFlowAdapter<String>( mTitle2) );
    }

    /**
     * 绘制一个圆的指示器
     */
    class CircleAction extends BaseAction {
        private static final String TAG = "CircleAction";

        @Override
        public void config(AbsFlowLayout parentView) {
            super.config(parentView);
            View child = parentView.getChildAt(0);
            if (child != null) {
                float l = parentView.getPaddingLeft() + child.getMeasuredWidth() / 2;
                float t = parentView.getPaddingTop() + child.getMeasuredHeight() - mTabBean.tabHeight / 2 - mTabBean.tabMarginBottom;
                float r = mTabBean.tabWidth + l;
                float b = child.getMeasuredHeight() - mTabBean.tabMarginBottom;
                mTabRect.set(l, t, r, b);
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
            mTabRect.left = value.left + mTabBean.tabWidth / 2;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mTabRect.left, mTabRect.top, mTabBean.tabWidth / 2, mPaint);
        }
    }

    class TabApdater extends TabFlowAdapter<String> {

        public TabApdater(int layoutId, List<String> data) {
            super(layoutId, data);
        }

        @Override
        public void onItemSelectState(View view, boolean isSelected) {
            super.onItemSelectState(view, isSelected);
            if (isSelected) {
                setDefaultTextColor(view, Color.WHITE);
            } else {
                setDefaultTextColor(view, getResources().getColor(R.color.unselect));
            }
        }

        @Override
        public void bindView(View view, String data, int position) {
            setDefaultText(view, data)
                    .setDefaultTextColor(view, getResources().getColor(R.color.unselect));
            if (position == 0) {
                setVisible(view, R.id.item_msg, true);
            }
        }

        @Override
        public void onItemClick(View view, String data, int position) {
            super.onItemClick(view, data, position);
        }
    }

}
