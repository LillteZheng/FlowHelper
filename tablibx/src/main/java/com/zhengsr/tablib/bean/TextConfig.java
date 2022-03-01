package com.zhengsr.tablib.bean;

import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.annotation.ColorInt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by zhengshaorui 2022/3/1
 * describe：用于配置 TextView 相关的一些属性
 */
public class TextConfig {
    private Rect rect;
    private List<Typeface> list;
    private float size = 0;
    public TextConfig setPadding(int l,int t,int r,int b){
        if (rect == null) {
            rect = new Rect();
        }
        rect.set(l,t,r,b);
        return this;
    }
    public TextConfig setTypeface(Typeface tf){
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(tf);
        return this;
    }
    public TextConfig setTextSize(float size){
        this.size = size;
        return this;
    }


    public Rect getPadding() {
        return rect;
    }

    public List<Typeface> getTypefaces() {
        return list;
    }

    public float getTextSize() {
        return size;
    }

}
