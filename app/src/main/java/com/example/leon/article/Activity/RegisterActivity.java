package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.leon.article.Http.Api;
import com.example.leon.article.Http.XHttpUtils;
import com.example.leon.article.R;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityRegisterBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Validator;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * RegisterActivity
 * Created by leonseven on 2017/5/15.
 */

public class RegisterActivity extends ToolBarBaseActivity<ActivityRegisterBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        hideHeaderInfo();
        hideHeaderMoneyInfo();
        bindViews();
    }


    private void bindViews() {
        setTitle(getString(R.string.register));

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
                .add("pwd", CommonUtils.getMD5Str(pwd))
                .add("repwd", CommonUtils.getMD5Str(pwd2))
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


}
