package com.zhengsr.tabhelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
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
import java.util.List;

import io.reactivex.observers.ResourceObserver;

public class VerticalTabActivity extends BaseActivity {
    private static final String TAG = "VerticalTabActivity";
    private TabFlowLayout mTabFlowLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private int mCurPosition;
    private boolean isNeedScroll;
    private List<NaviBean> mDatas = new ArrayList<>();
    private NaviAdapter mNaviAdapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_tab);

        mTabFlowLayout = findViewById(R.id.tabflow);
        mRecyclerView = findViewById(R.id.recyclerview);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mNaviAdapter = new NaviAdapter(R.layout.item_navi_detail, mDatas);
        TextView textView = new TextView(this);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        textView.setText("正在加载，请稍等....");
        textView.setGravity(Gravity.CENTER);
        mNaviAdapter.setEmptyView(textView);
        mRecyclerView.setAdapter(mNaviAdapter);

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
                setText(view, R.id.item_text,data.getName());
                setTextColor(view, R.id.item_text, getResources().getColor(R.color.wechat));
            }

            @Override
            public void onItemSelectState(View view, boolean isSelected) {
                super.onItemSelectState(view, isSelected);
                if (isSelected){
                    setTextColor(view, R.id.item_text,getResources().getColor(R.color.colorPrimary));
                }else{
                    setTextColor(view, R.id.item_text,getResources().getColor(R.color.wechat));
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
                        mTabFlowLayout.setItemClickByOutside(firstPosition);
                        mTabFlowLayout.setItemClick(true);
                    }else{
                        /**
                         * 如果上次为点击事件，则先还原，下次滑动时，监听即可
                         */
                        mTabFlowLayout.setItemClick(false);
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
        mDatas.clear();
        mDatas.addAll(listBaseResponse.getData());
        mNaviAdapter.notifyDataSetChanged();
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
            setText(view, R.id.item_text,data.getTitle())
                    .setTextColor(view, R.id.item_text, Color.WHITE);
            view.setBackground(CommonUtils.getColorDrawable(10));
        }

        @Override
        public void onItemClick(View view, ArticleData data, int position) {
            super.onItemClick(view, data, position);
        }
    }
}
