package com.zhengsr.tablib.bean;

/**
 * @auther by zhengshaorui on 2020/2/1
 * describe:
 */
public class LabelBean {

    /**
     * 多选最大个数
     */
    public int maxSelectCount =1;
    /**
     * 是否超过控件高度，自动滚动，默认为true
     */
    public boolean isAutoScroll = true;
    /**
     * 支持最多显示的行数,默认显示全部
     */
    public int showLines = -1;

    /**
     * 显示更多的layoutId
     */
    public int showMoreLayoutId = -1;

    /**
     * 显示更多的背景色，建议与主布局的背景色一直
     */
    public int showMoreColor = -2;

    /**
     * 收起的 layoutId
     */
    public int handUpLayoutId = -1;


}
