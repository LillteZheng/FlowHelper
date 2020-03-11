package com.zhengsr.tablib.bean;

import com.zhengsr.tablib.FlowConstants;

/**
 * @auther by zhengshaorui on 2020/1/9
 * describe: 用来配置tab的bean
 */
public class TabBean {
    /**
     * tab 类型，rect、tri、round、res、color、cus
     */
    public int tabType = -1;
    /**
     * tab 的颜色
     */
    public int tabColor = -2;
    /**
     * tab 的宽度
     */
    public int tabWidth = -1;
    /**
     * tab 的高度
     */
    public int tabHeight = -1;
    /**
     * type选择 round 时的圆角大小
     */
    public int tabRoundSize = -1;
    /**
     * margin 的左上右下
     */
    public int tabMarginLeft = -1;
    public int tabMarginTop = -1;
    public int tabMarginRight = -1;
    public int tabMarginBottom = -1;
    /**
     * 点击的切换速度，当没有viewpager 时，相当于滑动速度
     */
    public int tabClickAnimTime = -1;
    /**
     * type 为 res时，你要关联的resouce，比如 bitmap、shape等
     */
    public int tabItemRes = -1;

    /**
     * 是否自动放大缩小的效果
     */
    public boolean autoScale = false;

    /**
     * 放大倍数
     */
    public float scaleFactor = 1;

    /**
     * TabFlow 的方向, FlowConstants.VERTICAL 竖向，FlowConstants.HORIZONTAL 横向
     */
    public int tabOrientation = FlowConstants.HORIZONTATAL;

    /**
     * tab 为 rect 或者 tri 的时候，左边还是在右边，也使用 flowConstants
     */
    public int actionOrientation = -1;

    /**
     * 是否自动滚动
     */
    public boolean isAutoScroll = true;

    /**
     * 可视个数
     */
    public int visualCount = -1;


    @Override
    public String toString() {
        return "TabBean{" +
                "tabType=" + tabType +
                ", tabColor=" + tabColor +
                ", tabWidth=" + tabWidth +
                ", tabHeight=" + tabHeight +
                ", tabRoundSize=" + tabRoundSize +
                ", tabMarginLeft=" + tabMarginLeft +
                ", tabMarginTop=" + tabMarginTop +
                ", tabMarginRight=" + tabMarginRight +
                ", tabMarginBottom=" + tabMarginBottom +
                ", tabClickAnimTime=" + tabClickAnimTime +
                ", tabItemRes=" + tabItemRes +
                ", autoScale=" + autoScale +
                ", scaleFactor=" + scaleFactor +
                ", tabOrientation=" + tabOrientation +
                ", actionOrientation=" + actionOrientation +
                ", isAutoScroll=" + isAutoScroll +
                '}';
    }
}
