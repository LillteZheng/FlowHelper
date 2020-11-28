package com.zhengsr.tabhelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhengsr.tabhelper.CommonUtils;
import com.zhengsr.tabhelper.R;
import com.zhengsr.tabhelper.bean.ArticleData;
import com.zhengsr.tabhelper.bean.BaseResponse;
import com.zhengsr.tabhelper.bean.NaviBean;
import com.zhengsr.tabhelper.rx.HttpCreate;
import com.zhengsr.tabhelper.utils.RxUtils;
import com.zhengsr.tablib.view.adapter.LabelFlowAdapter;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.LabelFlowLayout;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.observers.ResourceObserver;

public class VerticalTabActivity extends AppCompatActivity {
    private static final String TAG = "VerticalTabActivity";
    private List<String> mTitle = new ArrayList<>(Arrays.asList("Life is like an ocean Only strong willed people can reach the other side i am shao rui zheng xiao yuan".split(" ")));
    private TabFlowLayout mTabFlowLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private int mCurPosition;
    private boolean isNeedScroll;
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_tab);

        mTabFlowLayout = findViewById(R.id.tabflow);
        mRecyclerView = findViewById(R.id.recyclerview);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);

        HttpCreate.getServer().getNaviData()
                .compose(RxUtils.<BaseResponse<List<NaviBean>>>rxScheduers())
                .subscribeWith(new ResourceObserver<BaseResponse<List<NaviBean>>>() {
                    @Override
                    public void onNext(BaseResponse<List<NaviBean>> listBaseResponse) {
                        handleData(listBaseResponse);
                    }


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void handleData(final BaseResponse<List<NaviBean>> listBaseResponse) {
        mTabFlowLayout.setAdapter(new TabFlowAdapter<NaviBean>(R.layout.item_textview_navi,listBaseResponse.getData()) {
            @Override
            public void bindView(View view, NaviBean data, int position) {
                setText(view,R.id.item_text,data.getName());
                setTextColor(view, R.id.item_text, getResources().getColor(R.color.wechat));
            }

            @Override
            public void onItemSelectState(View view, boolean isSelected) {
                super.onItemSelectState(view, isSelected);
                if (isSelected){
                    setTextColor(view,R.id.item_text,getResources().getColor(R.color.colorPrimary));
                }else{
                    setTextColor(view,R.id.item_text,getResources().getColor(R.color.wechat));
                }
            }

            @Override
            public void onItemClick(View view, NaviBean data, int position) {
                super.onItemClick(view, data, position);
                int firstPosition = mManager.findFirstVisibleItemPosition();
                int lastPosition = mManager.findLastVisibleItemPosition();
                mCurPosition = position;

                /**
                 * 目标在 可见视图的上面
                 */
                if (position <= firstPosition) {
                    mRecyclerView.smoothScrollToPosition(position);
                    //防止不刷新视图
                    mRecyclerView.requestLayout();
                } else if (position <= lastPosition) {
                    //往下点，且 position 在中间，但是 lastposition 的数据也能看到.所以把它置顶
                    /**
                     * 目标在 first 和 last 的中间
                     */
                    int top = mRecyclerView.getChildAt(position - firstPosition).getTop();
                    if (top > 0) {
                        mRecyclerView.smoothScrollBy(0, top);
                    }

                } else {
                    /**
                     * 目标在可视视图的下面
                     */
                    //该函数让它滚动到可视界面
                    mRecyclerView.scrollToPosition(position);
                    //此时recycler 的item还未滚动到顶端，需要重新再让它滚动改一下
                    isNeedScroll = true;
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){

                    int firstPosition = mManager.findFirstVisibleItemPosition();
                    if (!mTabFlowLayout.isItemClick()) {
                        mTabFlowLayout.setItemClickByOutSet(firstPosition);
                        mTabFlowLayout.setItemClickStatus(true);
                    }else{
                        /**
                         * 如果上次为点击事件，则先还原，下次滑动时，监听即可
                         */
                        mTabFlowLayout.setItemClickStatus(false);
                    }

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isNeedScroll){
                    isNeedScroll = false;
                    int index = mCurPosition - mManager.findFirstVisibleItemPosition();
                    if (index >= 0 && mRecyclerView.getChildCount() > index){
                        int top = mRecyclerView.getChildAt(index).getTop();
                        mRecyclerView.smoothScrollBy(0,top);
                    }
                }
            }
        });

        mRecyclerView.setAdapter(new NaviAdapter(R.layout.item_navi_detail,listBaseResponse.getData()));
    }


    class NaviAdapter extends BaseQuickAdapter<NaviBean, BaseViewHolder> {

        public NaviAdapter(int layoutResId, @Nullable List<NaviBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, NaviBean item) {
            helper.setText(R.id.item_navi_title,item.getName());
            LabelFlowLayout flowLayout = helper.getView(R.id.labelflow);
            flowLayout.setAdapter(new LabelAdapter(R.layout.item_textview,item.getArticles()));
        }
    }

    class LabelAdapter extends LabelFlowAdapter<ArticleData> {

        public LabelAdapter(int layoutId, List<ArticleData> data) {
            super(layoutId, data);
        }

        @Override
        public void bindView(View view, ArticleData data, int position) {
            setText(view,R.id.item_text,data.getTitle())
                    .setTextColor(view,R.id.item_text, Color.WHITE);
            view.setBackground(CommonUtils.getColorDrawable(10));
        }

        @Override
        public void onItemClick(View view, ArticleData data, int position) {
            super.onItemClick(view, data, position);
        }
    }
}
