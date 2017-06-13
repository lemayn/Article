package com.example.leon.article.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leon.article.Activity.presenter.ILoginPre;
import com.example.leon.article.Activity.presenter.LoginPresenter;
import com.example.leon.article.Http.Api;
import com.example.leon.article.Http.XHttpUtils;
import com.example.leon.article.R;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.bean.AdvBean;
import com.example.leon.article.databinding.ActivityRegisterBinding;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.Validator;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * RegisterActivity
 * Created by leonseven on 2017/5/15.
 */

public class RegisterActivity extends ToolBarBaseActivity<ActivityRegisterBinding> implements ILoginPre {

    private LoginPresenter presenter;
    List<AdvBean.DataBean> advlist = new ArrayList<AdvBean.DataBean>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        presenter = new LoginPresenter(RegisterActivity.this, this);

        setHideToolBarView();
        hideHeaderInfo();
        hideHeaderMoneyInfo();
        setNavigationView();
        init();
    }


    @Override
    public void init() {
        setTitle(getString(R.string.register));

        presenter.AdvData();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }

        });
    }



    private void register() {

        String account = binding.edittextName.getText().toString().trim();
        String pwd = binding.edittextPwd.getText().toString().trim();
        String pwd2 = binding.edittextPwd2.getText().toString().trim();
        String phone = binding.edittextPhone.getText().toString().trim();
        String qq =  binding.edittextQq.getText().toString().trim();

        if (account == null || account.equals("")){
            binding.edittextName.setError("账号不能为空");
            return;
        }
        if (account.length() < 6 || account.length() > 10){
            binding.edittextName.setError("账号不正确");
            return;
        }
        if (!Validator.isPassword(pwd)) {
            binding.edittextPwd2.setError("请输入正确的6~16位密码");
            return;
        }
        if (pwd2 == null || pwd2.equals("")){
            binding.edittextPwd2.setError("重复密码不能为空");
            return;
        }
        if (!pwd.equals(pwd2)){
            binding.edittextPwd2.setError("两次密码不一致");
            return;
        }
        if (!Validator.isMobile(phone)){
            binding.edittextPhone.setError("请输入正确的手机号");
            return;
        }
        if (qq == null || qq.equals("")){
            binding.edittextQq.setError("QQ账号不能为空");
            return;
        }

        FormBody formBody = new FormBody.Builder()
                .add("name", account)
                .add("pwd", pwd)
                .add("repwd", pwd2)
                .add("tell", phone)
                .add("qq", qq)
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl + Api.Reg,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        Gson gson = new Gson();
//                        Log.i("MyTest", "bean 请求成功01" + result.toString());
                        ArticleApiBean bean = new ArticleApiBean();
                        bean = gson.fromJson(result, ArticleApiBean.class);

                        if (bean.getCode().equals("0")){
                            Toast.makeText(RegisterActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("2")){
                            Toast.makeText(RegisterActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("1")) {
                            Toast.makeText(RegisterActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }

                    }
                });

    }


    @Override
    public void showAdvList(List<AdvBean.DataBean> List) {
        advlist.addAll(List);
        rollview();
    }


    private void rollview() {

        binding.rollpagerviewRegister.setAnimationDurtion(500);    //设置切换时间
        binding.rollpagerviewRegister.setAdapter(new TestLoopAdapter(binding.rollpagerviewRegister, advlist)); //设置适配器
        binding.rollpagerviewRegister.setHintView(new ColorPointHintView(this, Color.WHITE, Color.GRAY));// 设置圆点指示器颜色
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
        public View getView(ViewGroup container, int position) {

            final int picNo = position + 1;
            ImageView view = new ImageView(container.getContext());

            Glide.with(RegisterActivity.this)
                    .load(headurl + list.get(position).getImg())
                    .fitCenter()
                    .into(view);
            //            view.setImageResource(list[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.MATCH_PARENT));

            view.setOnClickListener(new View.OnClickListener()      // 点击事件
            {
                @Override
                public void onClick(View v) {
                }

            });

            return view;
        }

        @Override
        public int getRealCount() {
            return list.size();
        }
    }
}
