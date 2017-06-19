package com.example.leon.article.api;

import com.example.leon.article.Activity.art.ArtConstant;
import com.example.leon.article.app;
import com.example.leon.article.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/20.
 */

public class ApiManager {
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetWorkUtil.isNetWorkAvailable(app.getInstance())) {
                int maxAge = 60;//在线缓存在一分钟之内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28;//离线时缓存保存四周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    private static File httpCacheDirectory = new File(app.getInstance().getCacheDir(), "artCache");
    private static int cacheSize = 1024 * 1024 * 10;//10MB
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .cache(cache)
            .build();

    private ArtApi artApi;
    private BankApi bankApi;
    private final Object artMonitor = new Object();
    private static ApiManager apiManager;

    public static ApiManager getInstance() {
        if (apiManager == null) {
            synchronized (ApiManager.class) {
                if (apiManager == null) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;
    }

    public ArtApi getArtApiService() {
        if (artApi == null) {
            synchronized (artMonitor) {
                if (artApi == null) {
                    artApi = new Retrofit.Builder()
                            .baseUrl(ArtConstant.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build()
                            .create(ArtApi.class);
                }
            }
        }
        return artApi;
    }

    public BankApi getBankApiService(){
        if (bankApi == null) {
            synchronized (artMonitor) {
                if (bankApi == null) {
                    bankApi = new Retrofit.Builder()
                            .baseUrl(ArtConstant.BASE_URL)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build()
                            .create(BankApi.class);
                }
            }
        }
        return bankApi;
    }

}
