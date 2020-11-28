package com.zhengsr.tablib.bean;

import android.graphics.RectF;
import android.util.TypedValue;

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