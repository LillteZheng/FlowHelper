package com.zhengsr.tablib.view.flow;

import android.view.ViewGroup;

/**
 * @author by zhengshaorui 2021/5/23 07:26
 * describe：
 */
public abstract class ITabHelper {
    /**
      *  View 此时可视化了
     */
    public void onViewVisible(AbsFlowLayout layout){}

    public void onComputeScroll(ViewGroup viewGroup){}
}
