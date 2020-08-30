package com.zhengsr.tablib.utils;

import android.content.res.TypedArray;
import android.graphics.Color;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import java.text.AttributedString;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author by zhengshaorui 2020/8/29 11:17
 * describe：自定义属性转换类
 */
public class AttrsUtils {
    /**
     * 获取TabBean的自定义数据
     * @param ta
     * @return
     */
    public static TabBean getTabBean(TypedArray ta){
        TabBean bean = new TabBean();

        bean.tabType = ta.getInteger(R.styleable.TabFlowLayout_tab_type, -1);
        bean.tabClickAnimTime = ta.getInt(R.styleable.TabFlowLayout_tab_click_animTime, 300);
        bean.tabOrientation = ta.getInteger(R.styleable.TabFlowLayout_tab_orientation, FlowConstants.HORIZONTATAL);
        bean.isAutoScroll = ta.getBoolean(R.styleable.TabFlowLayout_tab_isAutoScroll, true);
        bean.visualCount = ta.getInteger(R.styleable.TabFlowLayout_tab_visual_count, -1);

        bean.tabWidth = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_width, -1);
        bean.tabHeight = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_height, -1);
        bean.tabColor = ta.getColor(R.styleable.TabFlowLayout_tab_color, Color.RED);
        bean.tabMarginLeft = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_l, 0);
        bean.tabMarginTop = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_t, 0);
        bean.tabMarginRight = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_r, 0);
        bean.tabMarginBottom = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_b, 0);
        bean.isAutoScroll = ta.getBoolean(R.styleable.TabFlowLayout_tab_item_autoScale, false);
        bean.scaleFactor = ta.getFloat(R.styleable.TabFlowLayout_tab_scale_factor, 1);
        bean.actionOrientation = ta.getInteger(R.styleable.TabFlowLayout_tab_action_orientaion,-1);
        bean.tabRoundSize = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_round_size,10);
        bean.tabItemRes = ta.getResourceId(R.styleable.TabFlowLayout_tab_item_res,-1);
        ta.recycle();
        return bean;
    }



}
