package com.example.leon.article.api;

import android.util.Log;

import com.example.leon.article.utils.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static OkHttpClient.Builder builder = new OkHttpClient.Builder();


    private static Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(setLoggingClient())
            .baseUrl(Constant.Api.BASE_URL)
            .build();

    private static OkHttpClient setLoggingClient() {
        addBasicParamInterceptor();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("Retrofit", message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        return builder.build();
    }

    private static void addBasicParamInterceptor() {
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                .addQueryParam("Key", Constant.Api.URL_KEY)
                .build();
        builder.addInterceptor(basicParamsInterceptor);
    }

    public static <T> T createService(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
