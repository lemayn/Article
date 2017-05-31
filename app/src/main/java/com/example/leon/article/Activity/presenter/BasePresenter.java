package com.example.leon.article.Activity.presenter;

import android.content.Context;

/**
 * Created by leonseven on 2017/5/27.
 */

public abstract class BasePresenter <T extends IBaseView>{
    protected Context context;
    protected T iView;

    public BasePresenter(Context context, T iView) {
        this.context = context;
        this.iView = iView;
    }

    public void init() {
        iView.init();
    }

    public abstract void release();
}
