package com.example.leon.article.Activity.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.leon.article.Activity.ForgetPwdActivity;
import com.example.leon.article.Http.Api;
import com.example.leon.article.Http.XHttpUtils;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.bean.AdvBean;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by leonseven on 2017/5/31.
 */

public class LoginPresenter extends BasePresenter<ILoginPre> {

    private Gson gson = new Gson();

    public LoginPresenter(Context context, ILoginPre iView) {
        super(context, iView);
    }

    @Override
    public void release() {

    }

    public void AdvData() {

        FormBody formBody = new FormBody.Builder()
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl+Api.Ad,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "AdvData请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
//                        Log.i("MyTest", "AdvData请求成功01" + result.toString());

                        AdvBean bean = new AdvBean();
                        if(result != null){
                            bean = gson.fromJson(result, AdvBean.class);

                            if (bean.getCode().equals("0")){
                                Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                            else if(bean.getCode().equals("2")){
                                Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                            else if(bean.getCode().equals("1")) {
                                iView.showAdvList(bean.getData());
                            }
                        }
                        else { Log.i("MyTest", "AdvData请求成功01 null");}

                    }
                });

    }


    public void Backpwd(String name, String phone) {

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
//                        Log.i("MyTest", "backpwd suc" + result.toString());
                        ArticleApiBean bean = gson.fromJson(result, ArticleApiBean.class);
                        if (bean.getCode().equals("0")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("2")){
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("1")) {
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
