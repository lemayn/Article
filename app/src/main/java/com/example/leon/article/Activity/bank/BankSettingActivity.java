package com.example.leon.article.Activity.bank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.leon.article.R;
import com.example.leon.article.api.bean.UserBankBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityBankSettingBinding;
import com.example.leon.article.presenter.bankpresenter.bankpresenterImp.BankPresenterImp;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IBankSettingActivity;

public class BankSettingActivity extends ToolBarBaseActivity<ActivityBankSettingBinding> implements IBankSettingActivity,View.OnClickListener {

    private Button bt_addCard;
    private RecyclerView rv_list;
    private BankSettingAdapter adapter;
    private BankPresenterImp bankPresenterImp;
    private String sid;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_setting);

        initView();
        //从服务器获取用户绑定信息
        initDate();
        initEvent();
    }

    private void initEvent() {
        loadUserData();
        bt_addCard.setOnClickListener(this);
        //RV for user bind BankCard to click item (My holy light!)
        adapter.setOnItemClickListener(new BankSettingAdapter.RvItemClickListener() {
            @Override
            public void onItemClick(View v, int position, UserBankBean.DataBean date) {
                /*Toast.makeText(BankSettingActivity.this,"绑定银行为："+date.getBank()+"   用户名为："+date.getAccount_name(),
                        Toast.LENGTH_LONG).show();*/
            }
        });
    }

    private void initDate() {
        bankPresenterImp = new BankPresenterImp(this);
        //get cookie sid
        cookie = (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
        sid = (String) SPUtil.get(Constant.Share_prf.SID, "");
        bankPresenterImp.getUserBankInfo(cookie, sid);
    }

    private void initView() {
        setTitle("银行设置");
        setNavigationView();
        bt_addCard = getViewById(R.id.bt_addCard);
        rv_list = getViewById(R.id.rv_bank_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new BankSettingAdapter(this);
        rv_list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_addCard :
                goAddCardActivity();
                break;
        }
    }

    private void goAddCardActivity() {
        Intent intent = new Intent(this,AddCardActivity.class);
        startActivity(intent);
        finish();
    }

    private <T extends View> T getViewById(int res){
        return (T) findViewById(res);
    }


    @Override
    public void setBankDate(UserBankBean bankDate) {
        if (bankDate != null) {
            adapter.addBanks(bankDate);
        }
        if (adapter.getItemCount() >= 2) {
            bt_addCard.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bankPresenterImp.unsubcrible();
    }
}
