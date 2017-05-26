package com.example.leon.article.api;

import com.example.leon.article.api.bean.ArtInfoBean;
import com.example.leon.article.api.bean.ArtListBean;
import com.example.leon.article.api.bean.UpLoadArtBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/20.
 */

public interface ArtApi {

    /**
     * 获取用户文章列表
     * @param cookie
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=Article")
    Observable<ArtListBean> getArtList(@Field("cookie") String cookie, @Field("sid") String sid
                                            , @Field("page")int page);

    /**
     * 获取文章列表详情
     * @param cookie
     * @param aid
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=ArtInfo")
    Observable<ArtInfoBean> getArtInfo(@Field("cookie") String cookie, @Field("aid") String aid, @Field("sid") String sid);

    /**
     * 上传用户文章
     * @param cookie
     * @param title
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=ArtAdd")
    Observable<UpLoadArtBean> uploadArt(@Field("cookie") String cookie, @Field("title") String title, @Field("content") String content
            , @Field("sid")String sid, @Field("img") String imgBase64);

}
