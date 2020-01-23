package com.zhengsr.tabhelper.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerticalTabActivity extends AppCompatActivity {
    private List<String> mTitle = new ArrayList<>(Arrays.asList("Life is like an ocean Only strong willed people can reach the other side i am shao rui zheng xiao yuan".split(" ")));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_tab);
        TabFlowLayout flowLayout = findViewById(R.id.tabflow);
        flowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }
        });
    }
}
