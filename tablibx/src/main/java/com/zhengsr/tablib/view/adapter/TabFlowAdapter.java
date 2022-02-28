package com.zhengsr.tablib.view.adapter;

import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 数据构建基类
 */
public class TabFlowAdapter<T> extends TemplateAdapter<T> {


    public TabFlowAdapter(int layoutId, List<T> data) {
        super(layoutId, data);
    }
    public TabFlowAdapter(List<T> data){
        super(-1,data);
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
