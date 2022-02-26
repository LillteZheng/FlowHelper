package com.zhengsr.tabhelper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class TestFragment extends BaseFragment {

    public static final String ARGUMENT = "argument";
    private List<String> mTitle = new ArrayList<>(Arrays.asList("Life is like an ocean Only strong willed people can reach the other side".split(" ")));


    @Override
    public int getLayoutId() {
        return R.layout.test_fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        TabFlowLayout tabFlowLayout = view.findViewById(R.id.tabflow);
        tabFlowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view, R.id.item_text,data);
            }


        });
    }

    /**
     * 弄一个静态工厂的方法调用 用于传参
     * @return
     */
    public static TestFragment newInStance(){
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
