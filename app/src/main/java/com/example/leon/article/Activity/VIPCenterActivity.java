package com.example.leon.article.Activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.example.leon.article.R;
import com.example.leon.article.adapter.VIPCenterAdapter;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.bean.ItemBean;
import com.example.leon.article.databinding.ActivityVipCenterBinding;
import com.example.leon.article.utils.CommonUtils;

import java.util.ArrayList;

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
        data.add(new ItemBean(R.drawable.basic_info, CommonUtils.getString(R.string.basic_info)));
        data.add(new ItemBean(R.drawable.banks_setting, CommonUtils.getString(R.string.banks_setting)));
        data.add(new ItemBean(R.drawable.banks_setting, CommonUtils.getString(R.string.withdraw_deposit)));
        data.add(new ItemBean(R.drawable.articles_list, CommonUtils.getString(R.string.articles_list)));
        data.add(new ItemBean(R.drawable.password_modify, CommonUtils.getString(R.string.password_modify), ModifyPasswordActivity.class));
        binding.rvVipCenter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvVipCenter.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        vipCenterAdapter = new VIPCenterAdapter(data);
        binding.rvVipCenter.setAdapter(vipCenterAdapter);
    }

    public void initView() {
        setTitle(getString(R.string.vip_center));
    }
}
