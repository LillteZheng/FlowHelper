package com.zhengsr.tablib.view.adapter;

import android.view.View;

import java.util.List;

/**
 * @auther by zhengshaorui on 2020/1/8
 * describe:
 */
public abstract class LabelFlowAdapter<T> extends TemplateAdapter<T> {

    public LabelFlowAdapter(int layoutId, List<T> data) {
        super(layoutId, data);
    }

    /**
     * 达到最大值
     * @param ids
     * @param count
     */
    public  void onReachMaxCount(List<Integer> ids, int count){}

    /**
     * 上个焦点和当前焦点的焦点情况，方便自定义动画，或者其他属性
     * @param oldView
     * @param newView
     */
    public  void onFocusChanged(View oldView, View newView){}


    /**
     * 显示更多
     * @param view
     */
    public void onShowMoreClick(View view){};

    /**
     * 收起
     * @param view
     */
    public void onHandUpClick(View view){};


    /**
     * 恢复所有状态
     */
    public void resetStatus(){
        if (mListener != null) {
            mListener.resetAllStatus();
        }
    }
}
