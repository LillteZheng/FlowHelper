package com.zhengsr.tablib.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.zhengsr.tablib.callback.FlowListenerAdapter;

import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 数据构建基类
 */
public abstract class TabAdapter<T> extends TemplateAdapter<T> {


    public TabAdapter(int layoutId, List<T> data) {
        super(layoutId, data);
    }



    /**
     * 恢复某个id的颜色
     */
    public void resetAllColor(int viewId, int color) {
        if (mListener != null) {
            mListener.resetAllTextColor(viewId, color);
        }
    }

}
