package com.example.leon.article.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leon.article.Activity.art.ArtConstant;
import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.VideoInfoBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityJiecaoPlayerBinding;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;

import java.util.HashMap;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class JieCaoPlayerActivity extends ToolBarBaseActivity<ActivityJiecaoPlayerBinding> {
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiecao_player);

        initView();
        initEvent();
    }

    private void initView() {
        mId = getIntent().getStringExtra(ArtConstant.DETAIL_AID);
        String title = getIntent().getStringExtra(ArtConstant.DETAIL_TITLE);
        if (TextUtils.isEmpty(mId)) {
            Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        hideHeaderInfo();
        setTitle(title);
        setNavigationView();
        binding.player.setUp("", JCVideoPlayer.CURRENT_STATE_NORMAL, "");
    }

    private void initEvent() {
        showProgressDialog();
        loadData();
    }

    private void loadData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        hashMap.put("id", mId);
        ApiFactory.getApi().video(Constant.Api.VIDEO_INFO, hashMap)
                .lift(new BaseValueValidOperator<VideoInfoBean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VideoInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(JieCaoPlayerActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(VideoInfoBean apiBean) {
                        binding.setVideo(apiBean);
                        binding.player.setUp(Constant.Api.BASE_URL + apiBean.getData().getVideo(),
                                JCVideoPlayer.CURRENT_STATE_NORMAL, "");
                        Glide.with(JieCaoPlayerActivity.this)
                                .load(Constant.Api.BASE_URL + apiBean.getData().getImg())
                                .into(binding.player.thumbImageView);
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
