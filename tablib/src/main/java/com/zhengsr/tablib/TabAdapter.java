package com.zhengsr.tablib;

import android.view.View;
import android.widget.TextView;

import com.zhengsr.tablib.callback.FlowListener;
import com.zhengsr.tablib.callback.FlowListenerAdapter;

import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 数据构建基类
 */
public abstract class TabAdapter<T> {
    private int mLayoutId;
    private List<T> mDatas;

    public TabAdapter(int layoutId, List<T> data) {
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

    public FlowListenerAdapter mListener;

    public void setListener(FlowListenerAdapter listener) {
        mListener = listener;
    }

    /**
     * 恢复某个id的颜色
     */
    public void resetAllColor(int viewId, int color) {
        if (mListener != null) {
            mListener.resetAllTextColor(viewId, color);
        }
    }


    /**
     * 常用模板
     */
    public TabAdapter setText(View view, int viewId, int resId) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setText(resId);
        }
        return this;
    }

    public TabAdapter setText(View view, int viewId, String msg) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setText(msg);
        }
        return this;
    }

    public TabAdapter setTextColor(View view, int viewId, int textColor) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setTextColor(textColor);
        }
        return this;
    }

    public TabAdapter setVisiable(View view, int viewId, boolean isVisiable) {
        View childView = view.findViewById(viewId);
        if (childView != null) {
            childView.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
        }
        return this;
    }


}
