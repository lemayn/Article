package com.example.leon.article.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.leon.article.Activity.LoginActivity;
import com.example.leon.article.api.bean.ApiBean;
import com.example.leon.article.app;

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
        final Activity taskTop = app.getInstance().getCurrentActivity();
        if (taskTop == null)
            return;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = new AlertDialog.Builder(taskTop)
                        .setMessage("您的账号已在其他终端登录,请重新登录")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(taskTop, LoginActivity.class);
                                taskTop.startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }
}
