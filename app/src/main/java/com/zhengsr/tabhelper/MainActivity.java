package com.zhengsr.tabhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengsr.tabhelper.activity.LabelActivity;
import com.zhengsr.tabhelper.activity.TabActivity;
import com.zhengsr.tabhelper.activity.TabNoViewPagerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, TabActivity.class));
    }

    public void tablayout(View view) {
        startActivity(new Intent(this, TabActivity.class));
    }

    public void tablayout2(View view) {
        startActivity(new Intent(this, TabNoViewPagerActivity.class));
    }

    public void labelflow(View view) {
        startActivity(new Intent(this, LabelActivity.class));
    }
}
