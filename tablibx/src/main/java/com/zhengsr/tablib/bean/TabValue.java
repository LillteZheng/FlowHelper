package com.zhengsr.tablib.bean;

import android.graphics.RectF;

public class TabValue {
    public float left;
    public float top;
    public float right;
    public float bottom;

    public TabValue(float left, float right) {
        this.left = left;
        this.right = right;
    }

    public TabValue() {
    }
    public float width(){
        return right - left;
    }

    @Override
    public String toString() {
        return "TabValue{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }

    public RectF valueToRect(){
        return new RectF(left,top,right,bottom);
    }

    public TabValue rectToValue(RectF rectF){
        left = rectF.left;
        top = rectF.top;
        right = rectF.right;
        bottom = rectF.bottom;
        return this;
    }
}