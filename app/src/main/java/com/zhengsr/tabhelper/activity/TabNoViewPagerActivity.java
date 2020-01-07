package com.zhengsr.tabhelper.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tablib.TabAdapter;
import com.zhengsr.tablib.view.TabFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabNoViewPagerActivity extends AppCompatActivity {
    private List<String> mTitle = Arrays.asList("Java","Android","Kotlin");
    //Life is like an ocean. Only strong willed people can reach the other side
    private ArrayList<String> mTitle2 = new ArrayList<>(Arrays.asList("Life is like an ocean".split(" ")));
    private ArrayList<String> mTitle3 = new ArrayList<>(Arrays.asList("Life is like an ocean. Only strong willed people can reach the other side".split(" ")));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_no_view_pager);
        rectFlow();
        triFlow();
        roundFlow();
        resFlow();

    }

    private void rectFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.rectflow);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
                if (position == 0){
                    setVisiable(view,R.id.item_msg,true);
                }
            }
        });

        TabFlowLayout flowLayout2 = findViewById(R.id.rectflow2);
        flowLayout2.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }
        });
    }

    private void triFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.triflow);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle2) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data)
                        .setTextColor(view,R.id.item_text,Color.BLACK);
                if (position == 0){
                    setTextColor(view,R.id.item_text,Color.WHITE);
                }
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                resetAllColor(R.id.item_text,Color.BLACK);
                setTextColor(view,R.id.item_text,Color.WHITE);
            }
        });
    }

    private void roundFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.roundflow);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle3) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data)
                        .setTextColor(view,R.id.item_text, Color.WHITE);
            }
        });
    }

    private void resFlow(){
        TabFlowLayout flowLayout = findViewById(R.id.resflow);
        flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle3) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data)
                        .setTextColor(view,R.id.item_text, Color.WHITE);
            }
        });
    }


}
