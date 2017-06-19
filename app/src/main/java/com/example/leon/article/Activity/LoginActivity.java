package com.example.leon.article.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leon.article.Activity.presenter.ILoginPre;
import com.example.leon.article.Activity.presenter.LoginPresenter;
import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.app;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.bean.AdvBean;
import com.example.leon.article.databinding.ActivityLoginBinding;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.utils.Validator;
import com.example.leon.article.widget.ForgetPwdDialog;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * loginActivity
 * Created by leonseven on 2017/5/9.
 */

public class LoginActivity extends ToolBarBaseActivity<ActivityLoginBinding> implements View.OnClickListener,
        ILoginPre {

    private boolean is_constraint_loin;
    private LoginPresenter presenter;
    List<AdvBean.DataBean> advlist = new ArrayList<AdvBean.DataBean>();

    ForgetPwdDialog dialog;
    String account = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = getIntent().getStringExtra("account");

        is_constraint_loin = getIntent().getBooleanExtra(Constant.Intent_Extra.IS_CONSTRAINT_LOIN, false);

        presenter = new LoginPresenter(LoginActivity.this, this);
        hideHeaderInfo();
        hideHeaderMoneyInfo();
        setHideToolBarView();
        init();

        boolean isLogin = !TextUtils.isEmpty((String) SPUtil.get(Constant.Share_prf.COOKIE, "")) ||
                !TextUtils.isEmpty((String) SPUtil.get(Constant.Share_prf.SID, ""));
        if (is_constraint_loin) {
            SPUtil.clear();
        } else {
            if (isLogin) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    @Override
    public void init() {
        setTitle(getString(R.string.login));

        presenter.AdvData();

        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.tvForgetpwd.setOnClickListener(this);
        binding.edittextPhone.setText(account);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                Login();
                break;

            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.tv_forgetpwd:
//                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                inputTitleDialog();
                break;
        }

    }


    /**
     * 找回密码弹窗
     */
    private void inputTitleDialog() {

        dialog = new ForgetPwdDialog(this,R.style.loading_dialog,onClickListener);
        dialog.show();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_login:
                    dialog.cancel();
                    break;

                case R.id.btn_submit:

                    String account = dialog.edittext_account.getText().toString().trim();
                    String mobilephone = dialog.edittext_phone.getText().toString().trim();

                    presenter.Backpwd(account, mobilephone);
                    dialog.cancel();
                    break;
            }
        }
    };




    private void Login() {

        String name = binding.edittextPhone.getText().toString().trim();
        final String pwd = binding.edittextPwd.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Validator.isPassword(pwd)) {
            Toast.makeText(LoginActivity.this, "请输入正确的6~16位密码", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("pwd", pwd);
        ApiFactory.getApi().article(Constant.Api.LOGIN, hashMap)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        SPUtil.put(Constant.Share_prf.COOKIE, apiBean.getData().getCookie());
                        SPUtil.put(Constant.Share_prf.SID, apiBean.getData().getSid());
                        SPUtil.put(Constant.Share_prf.NAME, apiBean.getData().getMname());
                        SPUtil.put(Constant.Share_prf.PWD, pwd);
                        if (!is_constraint_loin) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        app.getInstance().exit();
    }

    @Override
    public void showAdvList(List<AdvBean.DataBean> List) {
        //        Log.i("MyTest", "advlist001  请求成功" + List.toString());
        advlist.addAll(List);
        rollview();
    }

    private void rollview() {

        binding.rollpagerviewLogin.setAnimationDurtion(500);    //设置切换时间
        binding.rollpagerviewLogin.setAdapter(new TestLoopAdapter(binding.rollpagerviewLogin, advlist)); //设置适配器
        binding.rollpagerviewLogin.setHintView(new ColorPointHintView(this, Color.WHITE, Color.GRAY));// 设置圆点指示器颜色
    }

    /**
     * 轮播图adapter
     */
    class TestLoopAdapter extends LoopPagerAdapter {
        String headurl = Constant.Api.BASE_URL;
        List<AdvBean.DataBean> list;

        public TestLoopAdapter(RollPagerView viewPager, List<AdvBean.DataBean> list) {
            super(viewPager);
            this.list = list;
        }

        @Override
        public View getView(ViewGroup container, final int position) {

            final int picNo = position + 1;
            ImageView view = new ImageView(container.getContext());

            Glide.with(LoginActivity.this)
                    .load(headurl + list.get(position).getImg())
                    .centerCrop()
                    .into(view);
            //            view.setImageResource(list[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.MATCH_PARENT));

            view.setOnClickListener(new View.OnClickListener()      // 点击事件
            {
                @Override
                public void onClick(View v) {}

            });

            return view;
        }

        @Override
        public int getRealCount() {
            return list.size();
        }
    }
}
