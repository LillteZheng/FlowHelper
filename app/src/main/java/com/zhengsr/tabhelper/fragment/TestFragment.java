package com.zhengsr.tabhelper.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhengsr.tabhelper.R;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class TestFragment extends Fragment {

    public static final String ARGUMENT = "argument";
    private List<String> mTitle = new ArrayList<>(Arrays.asList("Life is like an ocean Only strong willed people can reach the other side".split(" ")));



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment,container,false);
        TabFlowLayout tabFlowLayout = view.findViewById(R.id.tabflow);
        tabFlowLayout.setAdapter(new TabFlowAdapter<String>(R.layout.item_msg,mTitle) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.item_text,data);
            }


        });
        return view;
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
