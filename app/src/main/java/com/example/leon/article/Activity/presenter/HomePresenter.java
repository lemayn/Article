package com.example.leon.article.Activity.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.leon.article.Http.Api;
import com.example.leon.article.Http.XHttpUtils;
import com.example.leon.article.bean.AdvBean;
import com.example.leon.article.bean.ExcellentBean;
import com.example.leon.article.bean.NoticeBean;
import com.example.leon.article.bean.RecomArtBean;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by leonseven on 2017/5/26.
 */

public class HomePresenter extends BasePresenter<IHomePre> {

    private Gson gson = new Gson();

    public HomePresenter(Context context, IHomePre iView) {
        super(context, iView);
    }
    @Override
    public void release() {
    }

    public void AdvData() {

        FormBody formBody = new FormBody.Builder()
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl+Api.Ad,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "AdvData请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
//                        Log.i("MyTest", "AdvData请求成功01" + result.toString());

                        AdvBean bean = new AdvBean();
                        bean = gson.fromJson(result, AdvBean.class);

                        if (bean.getCode().equals("0")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("2")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("1")) {
                            iView.showAdvList(bean.getData());
                        }
                    }
                });

    }

    public void RecommendArticle(int page) {
        String Spage = String.valueOf(page);
        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl+Api.Index,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "RecommendArticle请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        Log.i("MyTest", "RecommendArticle请求成功01" + result.toString());

                        RecomArtBean bean = new RecomArtBean();
                        bean = gson.fromJson(result, RecomArtBean.class);

                        if (bean.getCode().equals("0")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("2")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("1")){
                            iView.showRecommendList(bean.getData().getTuijian(), bean.getData().getTotalpage());
                        }
                    }
                });
    }

    public void excellentArticle(int page) {
        String Spage = String.valueOf(page);
        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl+Api.Good,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "excellentArticle请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        Log.i("MyTest", "excellentArticle请求成功01" + result.toString());

//                        RecomArtBean bean1 = new RecomArtBean();
                        ExcellentBean bean = new ExcellentBean();
                        bean = gson.fromJson(result, ExcellentBean.class);

                        if (bean.getCode().equals("0")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("2")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("1")) {
                            iView.showGoodList(bean.getData().getGood(), bean.getData().getTotalpage());
                        }

                    }
                });
    }


    public void SystemNotice() {

        FormBody formBody = new FormBody.Builder()
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl+Api.Ncontent,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "SystemNotice请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
//                        Log.i("MyTest", "SystemNotice请求成功01" + result.toString());

                        NoticeBean bean = new NoticeBean();
                        bean = gson.fromJson(result, NoticeBean.class);

                        if (bean.getCode().equals("0")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("2")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("1")) {
                            iView.showNoticeList(bean.getData());
                        }

                    }
                });
    }

}
