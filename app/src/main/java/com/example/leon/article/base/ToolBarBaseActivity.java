package com.example.leon.article.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.leon.article.Activity.art.EditorActivity;
import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.databinding.ActivityToolBarBaseBinding;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.GsonUtil;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.widget.CustomDialog;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ToolBarBaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    protected T binding;
    protected ActivityToolBarBaseBinding baseBinding;
    protected CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        baseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_tool_bar_base, null, false);
        binding = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        binding.getRoot().setLayoutParams(params);
        FrameLayout mContainer = (FrameLayout) baseBinding.getRoot().findViewById(R.id.container);
        mContainer.addView(binding.getRoot());
        getWindow().setContentView(baseBinding.getRoot());

        setToolBar();
        setCustomDialog();
        gotoArticle();
    }

    private void setCustomDialog() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
    }

    private void setToolBar() {
        setSupportActionBar(baseBinding.toolbar);
        //设置不显示自带的 Title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setTitle(CharSequence title) {
        baseBinding.tvToolbarTitle.setText(title);
    }

    public void setNavigationView() {
        baseBinding.ivToolbarBack.setVisibility(View.VISIBLE);
        baseBinding.ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void hideNavigationView() {
        baseBinding.ivToolbarBack.setVisibility(View.GONE);
    }

    protected void hideHeaderInfo() {
        baseBinding.llHeaderInfo.setVisibility(View.GONE);
    }

    protected void hideHeaderMoneyInfo() {
        baseBinding.llHeaderMoney.setVisibility(View.GONE);
    }

    protected void showProgressDialog() {
        if (customDialog != null && !customDialog.isShowing()) {
            customDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    protected void loadUserData() {
        loadUserInfo();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        ApiFactory.getApi().article(Constant.Api.USER_DATA, hashMap)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ToolBarBaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        baseBinding.setApibean(apiBean);
                        SPUtil.put(Constant.Share_prf.USER_DATA, GsonUtil.GsonString(apiBean));
                    }
                });
    }

    private void loadUserInfo() {
        String loginResponse = (String) SPUtil.get(Constant.Share_prf.LOGIN_RESPONSE, "");
        if (!TextUtils.isEmpty(loginResponse)) {
            ArticleApiBean userInfoBean = GsonUtil.GsonToBean(loginResponse, ArticleApiBean.class);
            baseBinding.setUserinfo(userInfoBean);
        }
    }

    protected void gotoArticle() {
        baseBinding.tvGotoArticle.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent(ToolBarBaseActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

}
