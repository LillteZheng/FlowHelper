package com.zhengsr.tabhelper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author by zhengshaorui 2022/2/26
 * describe：
 */
public abstract class BaseFragment extends SupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (getLayoutId() != -1) {
            view = inflater.inflate(getLayoutId(), container, false);
            initView(view);
        }else{
            view = getContentView();
        }
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();
    }

    protected void initData() {
    }

    ;

    protected void initView(View view) {
    }



    public View getContentView(){
        return null;
    }
    public  int getLayoutId() {
        return -1;
    }
}
