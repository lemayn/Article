package com.example.leon.article;

import android.support.test.runner.AndroidJUnit4;

import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.utils.Constant;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        String url = "http://118.89.233.35:8989/?Action=UserData&Key=55a50c1a06f9c1032014112cbd68f34b";
        String cookie = "2493109ec31930d9205d3e614cd43ced";

        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("cookie", cookie)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    @Test
    public void login() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "test01");
        hashMap.put("pwd", "1234567");
        ApiFactory.getApi().article(Constant.Api.LOGIN, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("cookie", apiBean.getData().getCookie());
                        hashMap.put("sid", apiBean.getData().getSid());
                        hashMap.put("aid", "20");
                        ApiFactory.getApi().article(Constant.Api.ART_INFO, hashMap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<ArticleApiBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(ArticleApiBean apiBean) {
                                        System.out.println(new Gson().toJson(apiBean));
                                    }
                                });
                    }
                });
    }

    @Test
    public void osConfig() {

    }

}
