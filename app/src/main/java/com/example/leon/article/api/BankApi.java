package com.example.leon.article.api;

import com.example.leon.article.api.bean.BankConfigBean;
import com.example.leon.article.api.bean.BindBankBean;
import com.example.leon.article.api.bean.UserBankBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/27.
 */

public interface BankApi {

    /**
     * 获取银行列表接口
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=BankConfig")
    Observable<BankConfigBean>  getBanklist(@Field("cookie") String cookie, @Field("sid") String sid);


    /**
     * 绑定银行卡接口,没有返回值
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=BanksSetAdd")
    Observable<BindBankBean> bindBankCard(@Field("cookie") String cookie, @Field("bid") String bid, @Field("card") String card
            , @Field("sid") String sid, @Field("account_name") String account_name,
                                          @Field("address") String address,
                                          @Field("password") String password);


    /**
     * 获取用户绑定银行卡信息接口
     */
    @FormUrlEncoded
    @POST("?Key=55a50c1a06f9c1032014112cbd68f34b&Action=UserBank")
    Observable<UserBankBean> getUserCardInfo(@Field("cookie") String cookie, @Field("sid") String sid);

}
