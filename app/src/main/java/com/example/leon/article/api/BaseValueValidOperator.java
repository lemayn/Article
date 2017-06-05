package com.example.leon.article.api;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.leon.article.Activity.LoginActivity;
import com.example.leon.article.api.bean.ApiBean;
import com.example.leon.article.app;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.DialogHelper;

import rx.Observable;
import rx.Subscriber;

/**
 * 对接口返回值进行预处理
 */
public class BaseValueValidOperator<T extends ApiBean>
        implements Observable.Operator<T, T> {

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }

            @Override
            public void onNext(T data) {
                Log.e("Retrofit_", "data: " + data);
                if (!subscriber.isUnsubscribed()) {
                    if (data == null) {
                        subscriber.onError(new Exception("网络异常"));
                        return;
                    }
                    if ("1".equals(data.getCode())) {
                        subscriber.onNext(data);
                        return;
                    }
                    if ("2".equals(data.getCode())) {
                        onConnectionConflict();
                        return;
                    }
                    subscriber.onError(new Exception(data.getMsg()));
                }
            }
        };
    }

    /**
     * 被踢下线处理
     */
    private void onConnectionConflict() {
        final Activity topActivity = app.getInstance().getCurrentActivity();
        if (topActivity == null) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showDialog(topActivity, null, "您的账号已在其他终端登录,请重新登录",
                        new DialogHelper.OnClickYesListener() {
                            @Override
                            public void onClickYes(DialogInterface dialog, int which) {
                                Intent intent = new Intent(topActivity, LoginActivity.class);
                                intent.putExtra(Constant.Intent_Extra.IS_CONSTRAINT_LOIN, true);
                                topActivity.startActivity(intent);
                                dialog.dismiss();
                            }
                        }, null);
            }
        });
    }
}
