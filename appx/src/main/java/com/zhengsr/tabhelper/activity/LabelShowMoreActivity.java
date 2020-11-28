package com.zhengsr.tabhelper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tablib.bean.LabelBean;
import com.zhengsr.tablib.view.adapter.LabelFlowAdapter;
import com.zhengsr.tablib.view.flow.LabelFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LabelShowMoreActivity extends AppCompatActivity {

    private List<String> mTitle = new ArrayList<>(Arrays.asList("Life is like an ocean Only strong willed people can reach the other side ".split(" ")));
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_show_more);
        
        LabelFlowLayout flowLayout = findViewById(R.id.labelflow);

        //自定义属性
        LabelBean bean = new LabelBean();
        bean.showLines = 2;
        bean.showMoreLayoutId = R.layout.show_more;
        bean.showMoreColor = Color.WHITE;
        bean.handUpLayoutId = R.layout.handup;

        flowLayout.setLabelBean(bean);
        flowLayout.setAdapter(new LabelFlowAdapter<String>(R.layout.item_textview,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data)
                        .setTextColor(view, R.id.item_text, Color.BLACK);
            }
            @Override
            public void onItemSelectState(View view, boolean isSelected) {
                super.onItemSelectState(view, isSelected);
                if (isSelected){
                    setTextColor(view, R.id.item_text,getResources().getColor(R.color.colorPrimary));
                }else{
                    setTextColor(view, R.id.item_text,Color.BLACK);
                }
            }

            @Override
            public void onShowMoreClick(View view) {
                super.onShowMoreClick(view);
                Toast.makeText(LabelShowMoreActivity.this, "显示全部", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHandUpClick(View view) {
                super.onHandUpClick(view);
                Toast.makeText(LabelShowMoreActivity.this, "收起", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
