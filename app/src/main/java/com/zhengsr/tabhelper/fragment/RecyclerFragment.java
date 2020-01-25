package com.zhengsr.tabhelper.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhengsr.tabhelper.R;
import com.zhengsr.tabhelper.bean.ArticleData;
import com.zhengsr.tabhelper.bean.BaseResponse;
import com.zhengsr.tabhelper.bean.NaviChildrenBean;
import com.zhengsr.tabhelper.bean.PageDataInfo;
import com.zhengsr.tabhelper.rx.HttpCreate;
import com.zhengsr.tabhelper.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class RecyclerFragment extends SupportFragment {
    private static final String TAG = "RecyclerFragment";
    private NaviChildrenBean mBean;
    private ArticleAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_layout,container,false);
        initView(view);
        return view;
    }

    private List<ArticleData> mArticleBeans = new ArrayList<>();
    public static RecyclerFragment newInstance(NaviChildrenBean bean) {

        Bundle args = new Bundle();
        args.putSerializable("bean",bean);
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        HttpCreate.getServer().getSystematicDetail(0,mBean.getId())
                .compose(RxUtils.<BaseResponse<PageDataInfo<List<ArticleData>>>>rxScheduers())
                .subscribe(new Observer<BaseResponse<PageDataInfo<List<ArticleData>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse<PageDataInfo<List<ArticleData>>> pageDataInfoBaseResponse) {
                        PageDataInfo<List<ArticleData>> data = pageDataInfoBaseResponse.getData();
                        List<ArticleData> datas = data.getDatas();
                        mAdapter.setNewData(datas);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initView(View view) {
        Bundle arguments = getArguments();
        mBean = (NaviChildrenBean) arguments.getSerializable("bean");

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        mAdapter = new ArticleAdapter(R.layout.item_article_recy_layout, mArticleBeans);
        recyclerView.setAdapter(mAdapter);




    }

    public class ArticleAdapter extends BaseQuickAdapter<ArticleData, BaseViewHolder> {

        private boolean isCollected = false;

        public ArticleAdapter(int layoutResId, @Nullable List<ArticleData> data) {
            super(layoutResId, data);
        }

        public void setCollected(boolean collected) {
            isCollected = collected;
        }



        @Override
        protected void convert(BaseViewHolder helper, ArticleData item) {
            String msg;
            if (!TextUtils.isEmpty(item.getSuperChapterName())){
                msg = item.getSuperChapterName()+"/"+item.getChapterName();
            }else{
                msg = item.getChapterName();
            }
            String author = (item.getAuthor() != null && item.getAuthor().length() > 0) ? item.getAuthor():item.getShareUser();
            helper.setText(R.id.item_article_author,author)
                    .setText(R.id.item_article_chapat, msg)
                    .setText(R.id.item_article_title,item.getTitle())
                    .setText(R.id.item_article_time,item.getNiceDate());

        }


    }
}
