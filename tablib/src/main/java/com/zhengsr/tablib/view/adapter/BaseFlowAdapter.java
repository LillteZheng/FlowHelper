package com.zhengsr.tablib.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.zhengsr.tablib.callback.FlowListenerAdapter;

import java.util.List;

/**
 * @auther by zhengshaorui on 2020/1/8
 * describe:
 */
public abstract class BaseFlowAdapter<T> {
    private int mLayoutId;
    private List<T> mDatas;
    public FlowListenerAdapter mListener;

    public BaseFlowAdapter(int layoutId, List<T> data) {
        mLayoutId = layoutId;
        mDatas = data;
    }

    /**
     * 获取个数
     *
     * @return
     */
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取id
     *
     * @return
     */
    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 公布给外部的数据
     *
     * @param view
     * @param data
     * @param position
     */
    public abstract void bindView(View view, T data, int position);


    /**
     * 单击
     *
     * @param view
     * @param position
     */
    public void onItemClick(View view, T data, int position) {
    }


    /**
     * 长按
     */
    public  boolean onItemLongClick(View view,int position){
        return true;
    };

    /**
     * 子控件的点击事件
     * @param childView
     * @param position
     */
    public void onItemChildClick(View childView,int position){}

    /**
     * 通知数据改变
     */
    public void notifyDataChanged() {
        if (mListener != null) {
            mListener.notifyDataChanged();
        }
    }

    /**
     * 构建一个listener，用来改变数据
     */
    public void setListener(FlowListenerAdapter listener) {
        mListener = listener;
    }

    /**
     * 如果布局里的子控件需要点击事件，需要现在这里注册
     * @param viewId
     * @return
     */
    public BaseFlowAdapter addChildrenClick(View view, int viewId, final int position){
        final View viewById = view.findViewById(viewId);
        if (viewById != null) {
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemChildClick(viewById,position);
                }
            });
        }
        return this;
    }


}
