package com.example.leon.article.api;

import com.example.leon.article.api.bean.ArtInfoBean;
import com.example.leon.article.api.bean.ArtListBean;
import com.example.leon.article.api.bean.UpLoadArtBean;
import com.example.leon.article.api.bean.UploadClassifyBean;
import com.example.leon.article.api.bean.VideoInfoBean;
import com.example.leon.article.api.bean.VideoListBean;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/20.
 */

public interface ArtApi {

    /**
     * 获取用户全部文章列表
     * @param cookie
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=Article")
    Observable<ArtListBean> getArtList(@Field("cookie") String cookie, @Field("sid") String sid
                                            , @Field("page")int page);

    /**
     * 获取用户发表文章列表分类
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=Article")
    Observable<ArtListBean> getUserArtTypeList(@Field("cookie") String cookie,
                                               @Field("sid") String sid,
                                               @Field("page")int page,
                                               @Field("type") int type);

    /**
     * 获取文章列表详情
     * @param cookie
     * @param aid
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=ArtInfo")
    Observable<ArtInfoBean> getArtInfo(@Field("cookie") String cookie, @Field("aid") String aid,
                                       @Field("sid") String sid);


    /**
     * 获取上传文章分类接口
     * @param cookie
     * @param sid
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=Classify")
    Observable<UploadClassifyBean> getClassify(@Field("cookie") String cookie, @Field("sid") String sid);

    /**
     * 上传用户文章
     * @param cookie
     * @param title
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=ArtAdd")
    Observable<UpLoadArtBean> uploadArt(@Field("cookie") String cookie,
                                        @Field("title") String title,
                                        @Field("content") String content,
                                        @Field("sid")String sid,
                                        @Field("img") String imgBase64,
                                        @Field("class_id")String class_id);

    /**
     * 上传用户视频
     */
    @Multipart
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=AddVideo")
    Observable<UpLoadArtBean> uploadVideo(@PartMap Map<String, RequestBody> params,
                                          @Part MultipartBody.Part file);

    /**
     * 获取用户全部视频列表
     * @param cookie
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=Video")
    Observable<VideoListBean> getVideoList(@Field("cookie") String cookie, @Field("sid") String sid
            , @Field("page")int page);

    /**
     * 获取视频列表详情
     * @param cookie
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=VideoInfo")
    Observable<VideoInfoBean> getVideoDetailInfo(@Field("cookie") String cookie, @Field("id") String id,
                                                 @Field("sid") String sid);


    /**
     * 获取用户发表视频列表分类
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=Video")
    Observable<VideoListBean> getUserVideoTypeList(@Field("cookie") String cookie,
                                               @Field("sid") String sid,
                                               @Field("page")int page,
                                               @Field("type") int type);

}
