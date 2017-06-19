package com.example.leon.article.Http;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * OKHTTP 简单封装
 *
 * Created by leonseven on 2017/4/25.
 */

public class XHttpUtils {

    private static XHttpUtils mClient;
    private OkHttpClient okHttpClient;
    private Handler handler;

    private XHttpUtils() {
        okHttpClient = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    public static XHttpUtils getInstance() {
        if (mClient == null) {
            synchronized (XHttpUtils.class) {
                if (mClient == null) {
                    mClient = new XHttpUtils();
                }
            }
        }
        return mClient;
    }

    class XCallBack implements Callback {

        private HttpCallBack httpCallBack;
        private Request request;

        public XCallBack(Request request, HttpCallBack httpCallBack) {
            this.request = request;
            this.httpCallBack = httpCallBack;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            final IOException fe = e;
            if (httpCallBack != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpCallBack.onError(request, fe);
                    }
                });
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            final String json = response.body().string();
            if (httpCallBack != null && response.isSuccessful()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpCallBack.onSuccess(request, json);
                    }
                });
            }
        }
    }

    public void asyncPost(String url, FormBody formBody, HttpCallBack httpCallBack) {
        Request request = new Request.Builder().url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue(new XCallBack(request, httpCallBack));
    }


    public interface HttpCallBack {
        void onError(Request request, IOException e);

        void onSuccess(Request request, String result);
    }

}
