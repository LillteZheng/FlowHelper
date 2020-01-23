package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengsr.tablib.R;
import com.zhengsr.tablib.callback.FlowListenerAdapter;
import com.zhengsr.tablib.view.adapter.BaseFlowAdapter;
import com.zhengsr.tablib.view.adapter.LabelFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther by zhengshaorui on 2020/1/8
 * describe: 标签瀑布流布局，支持单选，多选
 */
public class LabelFlowLayout extends ScrollFlowLayout {
    private LabelFlowAdapter mAdapter;
    private int mMaxSelectCount;
    private int mLastPosition = 0;
    public LabelFlowLayout(Context context) {
        this(context,null);
    }

    public LabelFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelFlowLayout);
        mMaxSelectCount = ta.getInteger(R.styleable.LabelFlowLayout_label_maxcount,1);
        ta.recycle();
    }


    public void setAdapter(LabelFlowAdapter adapter){
        mAdapter = adapter;
        mAdapter.setListener(new LabelListener());
        notifyData();
    }


    class LabelListener extends FlowListenerAdapter{
        @Override
        public void notifyDataChanged() {
            super.notifyDataChanged();
            notifyData();
        }

        @Override
        public void resetAllStatus() {
            super.resetAllStatus();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                view.setSelected(false);
                mAdapter.onItemSelectState(view,false);
            }
        }
    }

    /**
     * 更新数据
     */
    private void notifyData(){
        removeAllViews();
        int childCount = mAdapter.getItemCount();
        for (int i = 0; i < childCount; i++) {
            View view = LayoutInflater.from(getContext()).inflate(mAdapter.getLayoutId(),this,false);
            mAdapter.bindView(view,mAdapter.getDatas().get(i),i);
            addView(view);
            onItemViewConfig(mAdapter,view,i);
        }
    }

    private void onItemViewConfig(LabelFlowAdapter mAdapter, View view,final int position) {
        final LabelFlowAdapter adapter =  mAdapter;
        //单选
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.onItemClick(v, adapter.getDatas().get(position), position);

                //是否为单选
                if (mMaxSelectCount == 1) {
                    if (mLastPosition != position) {
                        View selectedView = getSelectedView();
                        if (selectedView != null) {
                            selectedView.setSelected(false);
                            adapter.onItemSelectState(selectedView, false);
                        }
                        adapter.onFocusChanged(selectedView, v);
                        //进行反选
                        if (v.isSelected()) {
                            v.setSelected(false);
                            adapter.onItemSelectState(v, false);
                        } else {
                            v.setSelected(true);
                            adapter.onItemSelectState(v, true);
                        }
                    }
                } else {
                    //进行反选
                    if (v.isSelected()) {
                        v.setSelected(false);
                        adapter.onItemSelectState(v, false);
                    } else {
                        v.setSelected(true);
                        adapter.onItemSelectState(v, true);
                    }
                    if (getSelectedCount() > mMaxSelectCount) {
                        v.setSelected(false);
                        adapter.onItemSelectState(v, false);
                        adapter.onReachMacCount(getSelecteds(), mMaxSelectCount);
                        return;
                    }
                }

                mLastPosition = position;
            }
        });

        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return adapter.onItemLongClick(view,position);
            }
        });
    }

    public LabelFlowLayout setMaxCount(int maxCount) {
        mMaxSelectCount = maxCount;
        return this;
    }



    /**
     * 获取选中的个数
     *
     * @return
     */
    private int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 拿到当前选中的view
     * 适合单选的时候
     *
     * @return
     */
    public View getSelectedView() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                return view;
            }
        }
        return null;
    }

    /**
     * 拿到选中数据
     * @return
     */
    public List<Integer> getSelecteds(){
        List<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                indexs.add(i);
            }
        }
        return indexs;
    }


    /**
     * 设置要选中的数据
     * @param indexs
     */
    public void setSelects(Integer... indexs){
        if (indexs != null && indexs.length > 0) {
            for (int i = 0; i < indexs.length; i++) {
                for (int j = 0; j < getChildCount(); j++) {
                    View view = getChildAt(j);
                    if (j == indexs[i]){
                        view.setSelected(true);
                        break;
                    }
                }
            }

        }
    }
}
