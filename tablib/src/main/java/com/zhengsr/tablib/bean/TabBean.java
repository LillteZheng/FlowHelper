package com.zhengsr.tablib.bean;

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
                '}';
    }
}
