package com.zhengsr.tablib.callback;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public interface FlowListener {
    void notifyDataChanged();
    void resetAllTextColor(int viewId,int color);
    void pagerClickChangeColor(int viewId,int unSelectedColor,int selectedColor);
}
