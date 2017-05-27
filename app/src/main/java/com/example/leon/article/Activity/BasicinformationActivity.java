package com.example.leon.article.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityBasicinformationBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.GsonUtil;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.SPUtil;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Basic Information
 * Created by leonseven on 2017/5/16.
 */

public class BasicinformationActivity extends ToolBarBaseActivity<ActivityBasicinformationBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinformation);

        bindViews();

        initData();
    }


    private void bindViews() {
        setTitle(CommonUtils.getString(R.string.basic_info));
        setNavigationView();
    }

    private void initData() {
        loadUserData();

        String response = (String) SPUtil.get(Constant.Share_prf.USER_DATA, "");
        final ArticleApiBean userData = GsonUtil.GsonToBean(response, ArticleApiBean.class);
        binding.setUserdata(userData);

        binding.btnRegister.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                boolean shouldEdit = userData.getData().getNickname() == null ||
                        userData.getData().getWeixin() == null ||
                        userData.getData().getEmail() == null;
                if (shouldEdit) {
                    editUserData();
                } else {
                    Toast.makeText(BasicinformationActivity.this, "基本资料已完成！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editUserData() {
        final String nickName = binding.editUseraccount.getText().toString().trim();
        String weiXin = binding.editWeixin.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();

        if (TextUtils.isEmpty(nickName)) {
            binding.editUseraccount.setError("昵称不能为空");
            return;
        }
        if (TextUtils.isEmpty(weiXin)) {
            binding.editWeixin.setError("昵称不能为空");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            binding.editEmail.setError("昵称不能为空");
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        hashMap.put("nickname", nickName);
        hashMap.put("weixin", weiXin);
        hashMap.put("email", email);
        ApiFactory.getApi().article(Constant.Api.USER_INFO_EDIT, hashMap)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(BasicinformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        dismissProgressDialog();
                        Toast.makeText(BasicinformationActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
