package com.zhengsr.tablib.utils;

import android.content.res.TypedArray;
import android.graphics.Color;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;

/**
 * @author by zhengshaorui 2020/8/29 11:17
 * describe：自定义属性转换类
 */
public class AttrsUtils {
    private static final String TAG = "AttrsUtils";
    /**
     * 获取TabBean的自定义数据
     * @param ta
     * @return
     */

    public static TabBean getTabBean(TypedArray ta){
        TabBean bean = new TabBean();

        bean.tabType = ta.getInteger(R.styleable.AbsFlowLayout_tab_type, -1);
        bean.tabColor = ta.getColor(R.styleable.AbsFlowLayout_tab_color, FlowConstants.COLOR_ILLEGAL);
        bean.tabWidth = ta.getDimensionPixelSize(R.styleable.AbsFlowLayout_tab_width, -1);
        bean.tabHeight = ta.getDimensionPixelSize(R.styleable.AbsFlowLayout_tab_height, -1);

        bean.tabRoundSize = ta.getDimensionPixelSize(R.styleable.AbsFlowLayout_tab_round_size,-1);

        bean.tabMarginLeft = ta.getDimensionPixelSize(R.styleable.AbsFlowLayout_tab_margin_l, 0);
        bean.tabMarginTop = ta.getDimensionPixelSize(R.styleable.AbsFlowLayout_tab_margin_t, 0);
        bean.tabMarginRight = ta.getDimensionPixelSize(R.styleable.AbsFlowLayout_tab_margin_r, 0);
        bean.tabMarginBottom = ta.getDimensionPixelSize(R.styleable.AbsFlowLayout_tab_margin_b, 0);

        bean.tabItemRes = ta.getResourceId(R.styleable.AbsFlowLayout_tab_item_res,-1);
        bean.tabClickAnimTime = ta.getInt(R.styleable.AbsFlowLayout_tab_click_animTime, 300);
        bean.autoScale = ta.getBoolean(R.styleable.AbsFlowLayout_tab_item_autoScale, false);
        bean.scaleFactor = ta.getFloat(R.styleable.AbsFlowLayout_tab_scale_factor, 1);

        bean.tabOrientation = ta.getInteger(R.styleable.AbsFlowLayout_tab_orientation, FlowConstants.HORIZONTATAL);
        bean.actionOrientation = ta.getInteger(R.styleable.AbsFlowLayout_tab_action_orientaion,-1);
        bean.isAutoScroll = ta.getBoolean(R.styleable.AbsFlowLayout_tab_isAutoScroll, true);
        bean.visualCount = ta.getInteger(R.styleable.AbsFlowLayout_tab_visual_count, -1);
        bean.tabWidthEqualsText = ta.getBoolean(R.styleable.AbsFlowLayout_tab_width_equals_text,true);
        bean.textType = ta.getInteger(R.styleable.AbsFlowLayout_tab_default_textType,1);
        bean.selectedColor = ta.getInteger(R.styleable.AbsFlowLayout_tab_text_select_color,FlowConstants.COLOR_ILLEGAL);
        bean.unSelectedColor = ta.getInteger(R.styleable.AbsFlowLayout_tab_text_unselect_color,FlowConstants.COLOR_ILLEGAL);
        return bean;
    }


    /**
     * 对比数据，xml的数据会被用户的覆盖
     * @param xmlBean
     * @param userBean
     * @return
     */
    public static TabBean diffTabBean(TabBean xmlBean,TabBean userBean){
        //todo 是有有工具帮忙优化？
        if (userBean.tabType != -1){
            xmlBean.tabType = userBean.tabType;
        }
        if (userBean.tabColor != FlowConstants.COLOR_ILLEGAL){
            xmlBean.tabColor = userBean.tabColor;
        }
        if (userBean.tabWidth != -1){
            xmlBean.tabWidth = userBean.tabWidth;
        }

        if (userBean.tabHeight != -1){
            xmlBean.tabHeight = userBean.tabHeight;
        }

        if (userBean.tabRoundSize != -1){
            xmlBean.tabRoundSize = userBean.tabRoundSize;
        }
        if (userBean.tabMarginLeft != -1){
            xmlBean.tabMarginLeft = userBean.tabMarginLeft;
        }
        if (userBean.tabMarginTop != -1){
            xmlBean.tabMarginTop = userBean.tabMarginTop;
        }
        if (userBean.tabMarginRight != -1){
            xmlBean.tabMarginRight = userBean.tabMarginRight;
        }
        if (userBean.tabMarginBottom != -1){
            xmlBean.tabMarginBottom = userBean.tabMarginBottom;
        }


        if (userBean.tabClickAnimTime != -1){
            xmlBean.tabClickAnimTime = userBean.tabClickAnimTime;
        }
        if (userBean.tabItemRes != -1){
            xmlBean.tabItemRes = userBean.tabItemRes;
        }
        if (userBean.autoScale){
            xmlBean.autoScale = true;
        }

        if (userBean.scaleFactor != 1){
            xmlBean.scaleFactor = userBean.scaleFactor;
        }
        if (userBean.tabOrientation != FlowConstants.HORIZONTATAL){
            xmlBean.tabOrientation = userBean.tabOrientation;
        }
        if (userBean.actionOrientation != -1){
            xmlBean.actionOrientation = userBean.actionOrientation;
        }

        if (!userBean.isAutoScroll){
            xmlBean.isAutoScroll = false;
        }

        if (userBean.visualCount != -1){
            xmlBean.visualCount = userBean.visualCount;
        }

        if (userBean.unSelectedColor != FlowConstants.COLOR_ILLEGAL) {
            xmlBean.unSelectedColor = userBean.unSelectedColor;
        }

        if (userBean.selectedColor != FlowConstants.COLOR_ILLEGAL) {
            xmlBean.selectedColor = userBean.selectedColor;
        }

        if (userBean.textType != FlowConstants.NORMALTEXT) {
            xmlBean.textType = userBean.textType;
        }
        return xmlBean;
    }



}
