package com.zhengsr.tablib.view.adapter;

import android.view.View;

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


    public boolean onItemChildLongClick(View childView,int position){
        return true;
    }

    /**
     * 通知数据改变，全部刷新
     */
    public void notifyDataChanged() {
        if (mListener != null) {
            mListener.notifyDataChanged();
        }
    }

    public void notifyInsertOrRemoveChange(){
        if (mListener != null) {
            mListener.notifyInsertOrRemoveChange();
        }
    }




    /**
     * view 是否选中状态
     * @param view
     * @param isSelected
     */
    public void onItemSelectState(View view, boolean isSelected){}

    /**
     * 构建一个listener，用来改变数据
     */
    public void setListener(FlowListenerAdapter listener) {
        mListener = listener;
    }




}
