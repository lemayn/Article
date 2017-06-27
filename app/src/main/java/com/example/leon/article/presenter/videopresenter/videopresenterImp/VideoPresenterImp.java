package com.example.leon.article.presenter.videopresenter.videopresenterImp;

import android.util.Log;

import com.example.leon.article.api.ApiManager;
import com.example.leon.article.api.bean.UploadClassifyBean;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.BasepresenterImp;
import com.example.leon.article.presenter.videopresenter.IVideoPresenter;
import com.example.leon.article.view.IUpVideoActivity;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/27.
 */

public class VideoPresenterImp extends BasepresenterImp implements IVideoPresenter {
    private IUpVideoActivity iUpVideoActivity;

    public VideoPresenterImp(IUpVideoActivity iUpVideoActivity) {
        this.iUpVideoActivity = iUpVideoActivity;
    }

    @Override
    public void getClassify(String cookie, String sid) {
        Subscription subscribe = ApiManager.getInstance().getArtApiService()
                .getClassify(cookie, sid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadClassifyBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UploadClassifyBean classifyBean) {
                        Log.i("FiDo", "onNext: "+classifyBean.getData().toString());
                        iUpVideoActivity.setClassfiy(classifyBean);
                    }
                });
        addSubscription(subscribe);
    }
}
