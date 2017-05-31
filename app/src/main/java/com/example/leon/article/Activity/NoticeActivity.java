package com.example.leon.article.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.leon.article.Http.Api;
import com.example.leon.article.Http.XHttpUtils;
import com.example.leon.article.R;
import com.example.leon.article.adapter.NoticeAdapter;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.bean.NoticeBean;
import com.example.leon.article.databinding.ActivityNoticeBinding;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 *  首页 通知详情
 * Created by leonseven on 2017/5/27.
 */

public class NoticeActivity extends ToolBarBaseActivity<ActivityNoticeBinding> {

    private RecyclerView recyclerview_notice;
    NoticeBean bean = new NoticeBean();
    List<NoticeBean.DataBean> beanlsit = new ArrayList<NoticeBean.DataBean>();
    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        initview();
    }

    private void initview() {
        setTitle(getString(R.string.system_notice));
        hideHeaderInfo();

        recyclerview_notice = (RecyclerView) findViewById(R.id.recyclerview_notice);
        recyclerview_notice.setLayoutManager(new LinearLayoutManager(recyclerview_notice.getContext(), LinearLayoutManager.VERTICAL, false));

        loadData();
    }

    private void setAdapter() {
        recyclerview_notice.setAdapter(adapter = new NoticeAdapter(beanlsit, this));
    }

    private void loadData() {
        final Gson gson = new Gson();
        FormBody formBody = new FormBody.Builder()
                .add("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""))
                .add("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""))
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl+Api.Ncontent,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "Ncontent请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {

                        bean = gson.fromJson(result, NoticeBean.class);
                        Log.i("MyTest", "Ncontent请求失败"+bean.toString());
                        if (bean.getCode().equals("0")){
                            Toast.makeText(NoticeActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("2")){
                            Toast.makeText(NoticeActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        else if(bean.getCode().equals("1")) {
                            if (bean != null){
                                beanlsit = bean.getData();
                            }
                        }

                        setAdapter();

                    }
                });
    }

}
