package com.zhengsr.tablib.callback;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public abstract class FlowListenerAdapter  {

    public abstract void notifyDataChanged();

    public void resetAllTextColor(int viewId,int color) { }

    public abstract void resetAllStatus();


}
