package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.example.leon.article.Activity.art.ArticleActivity;
import com.example.leon.article.R;
import com.example.leon.article.adapter.VIPCenterAdapter;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.OnItemClickListener;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.bean.ItemBean;
import com.example.leon.article.databinding.ActivityVipCenterBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VIPCenterActivity extends ToolBarBaseActivity<ActivityVipCenterBinding> {


    private VIPCenterAdapter vipCenterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_center);
        initView();
        initEvent();
    }

    private void initEvent() {
        ArrayList<ItemBean> data = new ArrayList<>();
        data.add(new ItemBean(R.drawable.basic_info, CommonUtils.getString(R.string.basic_info),
                BasicinformationActivity.class));
        data.add(new ItemBean(R.drawable.banks_setting, CommonUtils.getString(R.string.banks_setting),
                SetBankActivity.class));
        data.add(new ItemBean(R.drawable.banks_setting, CommonUtils.getString(R.string.withdraw_deposit),
                WithdrawDepositActivity.class));
        data.add(new ItemBean(R.drawable.articles_list, CommonUtils.getString(R.string.articles_list),
                ArticleActivity.class));
        data.add(new ItemBean(R.drawable.password_modify, CommonUtils.getString(R.string.password_modify),
                ModifyPasswordActivity.class));
        binding.rvVipCenter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvVipCenter.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        vipCenterAdapter = new VIPCenterAdapter(data);
        binding.rvVipCenter.setAdapter(vipCenterAdapter);

        loadUserData();

    }

    private void loadUserData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", "12cf8d32f7012f8c4e128cfa8a4aa2df");
        hashMap.put("sid", "c5etakebn6grkst6csqk2a5o62");
        ApiFactory.getApi().article(Constant.Api.USER_DATA, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(VIPCenterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        Toast.makeText(VIPCenterActivity.this, apiBean.getCode(), Toast.LENGTH_SHORT).show();
                        if ("1".equals(apiBean.getCode())) {
                            baseBinding.setApibean(apiBean);
                        }
                    }
                });
    }

    public void initView() {
        setTitle(getString(R.string.vip_center));
    }


    private void onClick() {

        vipCenterAdapter.setOnItemClickListener(new OnItemClickListener<ItemBean>() {
            @Override
            public void onClick(ItemBean itemBean, int position) {
                Toast.makeText(VIPCenterActivity.this, "asd" + position, Toast.LENGTH_LONG).show();
                startActivity(new Intent(VIPCenterActivity.this, itemBean.getClazz()));
            }
        });
    }

}
