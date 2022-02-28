package com.zhengsr.tablib.utils;

import android.content.Context;

public class DisplayUtil {

    /**
     * px传dp,dip,保证尺寸大小不变
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }

    /**
     * dip,dp传px，保证尺寸大小不变
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context,float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale+0.5f);
    }

    /**
     * px转sp，保证文字大小不变
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context,float pxValue){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue/fontScale + 0.5f);
    }

    /**
     * sp转px,保证文字大小不变
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context,float spValue){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale + 0.5f);
    }

}