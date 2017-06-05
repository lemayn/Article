package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leon.article.Http.Api;
import com.example.leon.article.Http.XHttpUtils;
import com.example.leon.article.R;
import com.example.leon.article.api.ArtApi;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityForgetpwdBinding;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * ForgetPassword
 * Created by leonseven on 2017/5/15.
 */

public class ForgetPwdActivity extends ToolBarBaseActivity<ActivityForgetpwdBinding> implements View.OnClickListener{

    private EditText edittext_account;
    private EditText edittext_phone;
    private Button btn_submit;
    private Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);

        initview();
    }


    private void initview() {

        setTitle(getString(R.string.back_password));
        setNavigationView();
        hideHeaderInfo();

        edittext_account = (EditText) findViewById(R.id.edittext_account);
        edittext_phone = (EditText) findViewById(R.id.edittext_phone);
        btn_submit = (Button) findViewById(R.id.btn_submit);btn_submit.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                backpwd();
                break;

            case R.id.btn_login:
                startActivity(new Intent(ForgetPwdActivity.this, LoginActivity.class));
                break;
        }
    }

    private void backpwd() {

        String name = edittext_account.getText().toString().trim();
        String phone = edittext_phone.getText().toString().trim();

        if (name.equals("")||name==null){
            return;
        }
        if (phone.equals("")||phone==null){
            return;
        }

        final Gson gson = new Gson();
        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("tell", phone)
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl+Api.RetPwd,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "backpwd  error");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        Log.i("MyTest", "backpwd suc" + result.toString());
                        ArticleApiBean bean = gson.fromJson(result, ArticleApiBean.class);
                        if (bean.getCode().equals("0")){
                            Toast.makeText(ForgetPwdActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("2")){
                            Toast.makeText(ForgetPwdActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("1")) {
                            Toast.makeText(ForgetPwdActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
    }

}
