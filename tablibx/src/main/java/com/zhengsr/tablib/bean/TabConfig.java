package com.zhengsr.tablib.bean;

import android.graphics.Rect;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.view.TabColorTextView;

/**
 * @author by zhengshaorui 2021/5/21 08:53
 * describe：TabFlowLayout 配置类
 */
public class TabConfig {
    private Builder builder;
    private TabConfig(Builder builder){
        this.builder = builder;
    }

    public int getTextId() {
        return builder.textId;
    }

    public ViewPager2 getViewPager2() {
        return builder.viewpager2;
    }

    public int getUnSelectColor() {
        return builder.unSelectColor;
    }

    public int getDefaultPos() {
        return builder.defaultPos;
    }

    public int getSelectedColor() {
        return builder.selectedColor;
    }

    public ViewPager getViewPager() {
        return builder.viewPager;
    }

    public int getVisibleCount() {
        return builder.visibleCount;
    }

    public TextConfig getTextConfig(){
        return builder.textConfig;
    }

    public Builder getBuilder() {
        return builder;
    }

    public boolean isUseColorText(){
        return builder.useColorText;
    }
    @Override
    public String toString() {
        return "TabConfig{" +
                "builder=" + builder.toString() +
                '}';
    }

    public static class Builder{
        private int textId = -1;
        private int unSelectColor = FlowConstants.COLOR_ILLEGAL;
        private int defaultPos;
        private int selectedColor = FlowConstants.COLOR_ILLEGAL;
        private ViewPager viewPager;
        private  ViewPager2 viewpager2;
        private int visibleCount=-1;
        private boolean useColorText;
        private TextConfig textConfig;
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
         * 若设置了layout，不设置id，不会有颜色变化
         * @param textId TextView 的id
         */
        public Builder setTextId(@IdRes int textId){
            this.textId = textId;
            return this;
        }

        public Builder setSelectedColor(@ColorInt int selectedColor){
            this.selectedColor = selectedColor;
            return this;
        }
        public Builder setUnSelectColor(@ColorInt int unSelectColor){
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


        /**
         * 不设置 layout ，默认 TextView 的大小
         * @param type {@link FlowConstants#NORMALTEXT} 或者 {@link FlowConstants#COLORTEXT}
         * @return
         */
        public Builder setDefaultTextType(int type) {
            useColorText = type == FlowConstants.COLORTEXT;
            return this;
        }

        /**
         * 设置 TextView 的通用属性
         * @param config
         * @return
         */
        public Builder setTextConfig(TextConfig config) {
            this.textConfig = config;
            return this;
        }


        public TabConfig build(){
            //判断参数
            if (viewpager2 != null && viewPager != null){
                throw  new IllegalArgumentException("you cannot set ViewPager and ViewPager2");
            }

            return new TabConfig(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "textId=" + textId +
                    ", unSelectColor=" + unSelectColor +
                    ", defaultPos=" + defaultPos +
                    ", selectedColor=" + selectedColor +
                    ", viewPager=" + viewPager +
                    ", viewpager2=" + viewpager2 +
                    ", visibleCount=" + visibleCount +
                    '}';
        }
    }

}
