package com.example.leon.article.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityCustomerServiceBinding;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.DownLoadImageService;
import com.example.leon.article.utils.ImageDownLoadCallBack;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CustomerServiceActivity extends ToolBarBaseActivity<ActivityCustomerServiceBinding> {
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        setNavigationView();
        hideHeaderInfo();

        setTitle(R.string.customer_service);

        getKeFu();
    }

    private void getKeFu() {
        ApiFactory.getApi().weixin(Constant.Api.KEFU)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CustomerServiceActivity.this, "获取公众号失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        loadExplain(apiBean.getData().getWeixin(), apiBean.getData().getImg());
                    }
                });
    }

    private void loadExplain(String weixin, String url) {
        URL = Constant.Api.BASE_URL + url;
        binding.weixin.append(weixin);
        Glide.with(this).load(URL)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(binding.image) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        binding.image.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                new AlertDialog.Builder(CustomerServiceActivity.this)
                                        .setItems(new String[]{"保存"}, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                PermissionGen.needPermission(CustomerServiceActivity.this,
                                                        123456,
                                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                                            }
                                        })
                                        .show();
                                return true;
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void downImages(String url) {
        new Thread(new DownLoadImageService(this, url, new ImageDownLoadCallBack() {

            @Override
            public void onDownLoadSuccess(Bitmap bitmap) {
                toast("保存成功");
            }

            @Override
            public void onDownLoadFailed(Throwable e) {
                toast("保存失败");
            }
        })).start();
    }

    private void toast(final String message) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CustomerServiceActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @PermissionSuccess(requestCode = 123456)
    private void downloadImage() {
        downImages(URL);
    }

    @PermissionFail(requestCode = 123456)
    private void showTip1() {
        Toast.makeText(CustomerServiceActivity.this, "未获取权限，保存失败", Toast.LENGTH_SHORT).show();
    }
}