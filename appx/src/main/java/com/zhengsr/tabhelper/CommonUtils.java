package com.zhengsr.tabhelper;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import java.util.Random;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class CommonUtils {
    /**
     * Tab colors
     */
    public static final int[] TAB_COLORS = new int[]{
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E"),
            Color.parseColor("#3399ff")
    };
    public static int randomTagColor() {
        int randomNum = new Random().nextInt();
        int position = randomNum % TAB_COLORS.length;
        if (position < 0) {
            position = -position;
        }
        return TAB_COLORS[position];
    }

    public static Drawable getColorDrawable(int radius){
        GradientDrawable drawable  = new GradientDrawable();
        drawable.setColor(randomTagColor());
        drawable.setCornerRadius(radius);
        return drawable;
    }
}
