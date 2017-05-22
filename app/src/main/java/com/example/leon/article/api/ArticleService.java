package com.example.leon.article.api;

import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.utils.Constant;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ArticleService {

    @FormUrlEncoded
    @POST(Constant.Api.BASE_URL)
    Observable<ArticleApiBean> article(@Query("Action") String type,
                                       @FieldMap HashMap<String,String> options);

}
