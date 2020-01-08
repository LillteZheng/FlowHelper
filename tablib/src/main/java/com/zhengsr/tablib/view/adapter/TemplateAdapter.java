package com.zhengsr.tablib.view.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @auther by zhengshaorui on 2020/1/8
 * describe: 模板类
 */
public abstract class TemplateAdapter<T> extends BaseFlowAdapter<T> {
    public TemplateAdapter(int layoutId, List data) {
        super(layoutId, data);
    }

    /**
     * 常用模板
     */
    public TemplateAdapter setText(View view, int viewId, int resId) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setText(resId);
        }
        return this;
    }

    public TemplateAdapter setText(View view, int viewId, String msg) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setText(msg);
        }
        return this;
    }

    public TemplateAdapter setTextColor(View view, int viewId, int textColor) {
        TextView textView = view.findViewById(viewId);
        if (textView != null) {
            textView.setTextColor(textColor);
        }
        return this;
    }

    
    public TemplateAdapter setImageView(View view, int viewId, int res){
        ImageView imageView = view.findViewById(viewId);
        if (imageView != null) {
            imageView.setImageResource(res);
        }
        return this;
    }

    public TemplateAdapter setImageView(View view, int viewId, Bitmap bitmap){
        ImageView imageView = view.findViewById(viewId);
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
        return this;
    }


    public TemplateAdapter setVisible(View view, int viewId, boolean isVisible) {
        View childView = view.findViewById(viewId);
        if (childView != null) {
            childView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
        return this;
    }
    public TemplateAdapter setVisible(View view, int viewId, int visible) {
        View childView = view.findViewById(viewId);
        if (childView != null) {
            childView.setVisibility(visible);
        }
        return this;
    }


}
