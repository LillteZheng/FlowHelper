package com.zhengsr.tablib.bean;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @author by zhengshaorui 2021/5/21 08:53
 * describe：TabFlowLayout 配置类
 */
public class TabConfig {
    private int textId = -2;
    private int unSelectColor = -2;
    private int defaultPos = 0;
    private int selectedColor = -2;
    private ViewPager viewPager;
    private  ViewPager2 viewpager2;
    private int visibleCount = -1;
    private TabConfig(Builder builder){
        textId = builder.textId;
        unSelectColor = builder.unSelectColor;
        selectedColor = builder.selectedColor;
        defaultPos = builder.defaultPos;
        viewPager = builder.viewPager;
        viewpager2 = builder.viewpager2;
        visibleCount = builder.visibleCount;
    }

    public int getTextId() {
        return textId;
    }

    public ViewPager2 getViewPager2() {
        return viewpager2;
    }

    public int getUnSelectColor() {
        return unSelectColor;
    }

    public int getDefaultPos() {
        return defaultPos;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public int getVisibleCount() {
        return visibleCount;
    }

    @Override
    public String toString() {
        return "TabConfig{" +
                "textId=" + textId +
                ", unSelectColor=" + unSelectColor +
                ", defaultPos=" + defaultPos +
                ", selectedColor=" + selectedColor +
                ", viewPager=" + viewPager +
                ", viewpager2=" + viewpager2 +
                ", visibleCount=" + visibleCount +
                '}';
    }

    public static class Builder{
        private int textId = -2;
        private int unSelectColor = -2;
        private int defaultPos;
        private int selectedColor = -2;
        private ViewPager viewPager;
        private  ViewPager2 viewpager2;
        private int visibleCount=-1;
        public Builder setViewPager(ViewPager viewPager){
            this.viewPager = viewPager;
            return this;
        }


        public Builder setViewpager(ViewPager2 viewpager2){
            this. viewpager2 =  viewpager2;
            return this;
        }

        public Builder setDefaultPos(int defaultPos){
            this.defaultPos = defaultPos;
            return this;
        }

        /**
         * 不设置id，不会有颜色变化
         * @param textId TextView 的id
         */
        public Builder setTextId(int textId){
            this.textId = textId;
            return this;
        }

        public Builder setSelectedColor(int selectedColor){
            this.selectedColor = selectedColor;
            return this;
        }
        public Builder setUnSelectColor(int unSelectColor){
            this.unSelectColor = unSelectColor;
            return this;
        }

        /**
         * 可见可数
         */
        public Builder setVisibleCount(int visibleCount){
            this.visibleCount = visibleCount;
            return this;
        }



        public TabConfig build(){
            //判断参数
            if (viewpager2 != null && viewPager != null){
                throw  new IllegalArgumentException("you cannot set ViewPager and ViewPager2");
            }
            if (selectedColor != -2 && unSelectColor != -2 && textId == 2){
                throw new IllegalArgumentException("you need to config setTextId()");
            }
            return new TabConfig(this);
        }
    }

}
