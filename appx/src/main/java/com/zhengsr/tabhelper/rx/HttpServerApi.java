package com.zhengsr.tabhelper.rx;


import com.zhengsr.tabhelper.bean.ArticleData;
import com.zhengsr.tabhelper.bean.BaseResponse;
import com.zhengsr.tabhelper.bean.NaviBean;
import com.zhengsr.tabhelper.bean.PageDataInfo;
import com.zhengsr.tabhelper.bean.SystematicBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author by  zhengshaorui on 2019/10/9
 * Describe: 统一网络服务接口类
 */
public interface HttpServerApi {
    @GET
    Observable<String> getJson(@Url String url);



    /**
     * 获取体系
     * https://www.wanandroid.com/tree/json
     */
    @GET("tree/json")
    Observable<BaseResponse<List<SystematicBean>>> getTreeKnowledge();

    /**
     * 获取系列的具体内容
     * https://www.wanandroid.com/article/list/0/json?cid=60
     *
     */
    @GET("article/list/{page}/json")
    Observable<BaseResponse<PageDataInfo<List<ArticleData>>>> getSystematicDetail(@Path("page") int page, @Query("cid") int cid);




    /**
     * https://www.wanandroid.com/navi/json
     * 获取导航数据
     */
    @GET("navi/json")
    Observable<BaseResponse<List<NaviBean>>> getNaviData();


}
