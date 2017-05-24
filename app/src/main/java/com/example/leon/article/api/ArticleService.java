package com.example.leon.article.api;

import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.api.bean.BankApiBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ArticleService {

    @FormUrlEncoded
    @POST("api/")
    Observable<ArticleApiBean> article(@Query("Action") String type,
                                       @FieldMap HashMap<String,String> options);

    @FormUrlEncoded
    @POST("api/")
    Observable<BankApiBean> bank(@Query("Action") String type,
                                 @FieldMap HashMap<String,String> options);

}
