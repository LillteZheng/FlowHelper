package com.zhengsr.tablib.bean;

import android.animation.TypeEvaluator;

/**
 * 自定义 TypeEvaluator
 */
public class TabTypeEvaluator implements TypeEvaluator<TabValue> {
    // PointF pointF = new PointF();
    TabValue value = new TabValue();

    @Override
    public TabValue evaluate(float fraction, TabValue startValue, TabValue endValue) {
        //这里都采用匀速
        value.left = startValue.left + fraction * (endValue.left - startValue.left);
        value.right = startValue.right + fraction * (endValue.right - startValue.right);
        return value;
    }
}