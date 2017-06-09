package com.example.leon.article.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leon.article.Activity.BasicinformationActivity;
import com.example.leon.article.Activity.LoginActivity;
import com.example.leon.article.Activity.ModifyPasswordActivity;
import com.example.leon.article.Activity.ModifyWithoutPasswordActivity;
import com.example.leon.article.Activity.WithdrawDepositActivity;
import com.example.leon.article.Activity.art.EditorActivity;
import com.example.leon.article.Activity.bank.BankSettingActivity;
import com.example.leon.article.R;
import com.example.leon.article.adapter.VIPCenterAdapter;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.app;
import com.example.leon.article.bean.ItemBean;
import com.example.leon.article.databinding.FragmentVipcenterBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.GsonUtil;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.SPUtil;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by leonseven on 2017/5/25.
 */

public class VipFragment extends Fragment {

    View view = null;
    private FragmentVipcenterBinding binding;
    private VIPCenterAdapter vipCenterAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_vipcenter, container, false);

        binding = DataBindingUtil.bind(view);

        initView();
        initEvent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    private void initEvent() {
        ArrayList<ItemBean> data = new ArrayList<>();
        data.add(new ItemBean(R.drawable.basic_info, CommonUtils.getString(R.string.basic_info),
                BasicinformationActivity.class));
        data.add(new ItemBean(R.drawable.banks_setting, CommonUtils.getString(R.string.banks_setting),
                BankSettingActivity.class));
        data.add(new ItemBean(R.drawable.banks_setting, CommonUtils.getString(R.string.withdraw_deposit),
                WithdrawDepositActivity.class));
        data.add(new ItemBean(R.drawable.articles_list, CommonUtils.getString(R.string.articles_list),
                WithdrawDepositActivity.class));
        data.add(new ItemBean(R.drawable.password_modify, CommonUtils.getString(R.string.password_modify),
                ModifyPasswordActivity.class));
        data.add(new ItemBean(R.drawable.password_modify, CommonUtils.getString(R.string.withdraw_password_modify),
                ModifyWithoutPasswordActivity.class));
        binding.rvVipCenter.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        binding.rvVipCenter.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        vipCenterAdapter = new VIPCenterAdapter(data);
        binding.rvVipCenter.setAdapter(vipCenterAdapter);
        binding.rvVipCenter.setFocusable(false);
    }

    public void initView() {
        gotoArticle();
    }

    protected void loadUserData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        ApiFactory.getApi().article(Constant.Api.USER_DATA, hashMap)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        SystemClock.sleep(500);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(app.getInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        binding.setApibean(apiBean);
                        SPUtil.put(Constant.Share_prf.USER_DATA, GsonUtil.GsonString(apiBean));
                    }
                });
    }

    protected void gotoArticle() {
        binding.tvGotoArticle.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                startActivity(intent);
            }
        });

        binding.btnLogout.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SPUtil.clear();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }
}
