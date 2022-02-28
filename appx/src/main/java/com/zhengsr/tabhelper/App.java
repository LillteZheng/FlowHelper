package com.zhengsr.tabhelper;

import android.app.Application;

import com.weikaiyun.fragmentation.Fragmentation;

/**
 * @author by zhengshaorui 2022/2/26
 * describe：
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fragmentation.builder() // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG) // 实际场景建议.debug(BuildConfig.DEBUG)
                .animation(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit, R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit) //设置默认动画
                .install();
    }
}
