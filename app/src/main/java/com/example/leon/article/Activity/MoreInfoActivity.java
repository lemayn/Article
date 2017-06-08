package com.example.leon.article.Activity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityMoreInfoBinding;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


public class MoreInfoActivity extends ToolBarBaseActivity<ActivityMoreInfoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        setNavigationView();
        hideHeaderInfo();

        setTitle(R.string.app_intro);

        loadExplain();
    }

    private void loadExplain() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        ApiFactory.getApi().article(Constant.Api.EXPLAIN, hashMap)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        dismissProgressDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MoreInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        binding.webView.loadData(apiBean.getData().getExplain(), "text/html; charset=UTF-8", null);
                    }
                });

    }
}
