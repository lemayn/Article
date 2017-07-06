package com.example.leon.article.presenter.videopresenter.videopresenterImp;

import com.example.leon.article.api.ApiManager;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.UploadClassifyBean;
import com.example.leon.article.api.bean.VideoInfoBean;
import com.example.leon.article.api.bean.VideoListBean;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.BasepresenterImp;
import com.example.leon.article.presenter.videopresenter.IVideoPresenter;
import com.example.leon.article.view.IUpVideoActivity;
import com.example.leon.article.view.IVideoListFragment;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/27.
 */

public class VideoPresenterImp extends BasepresenterImp implements IVideoPresenter {

    private IUpVideoActivity iUpVideoActivity;
    private IVideoListFragment iVideoListFragment;

    public VideoPresenterImp(IVideoListFragment iVideoListFragment) {
        this.iVideoListFragment = iVideoListFragment;
    }

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
                        iUpVideoActivity.setClassfiy(classifyBean);
                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void getuserVideoList(String cookie, String sid, int page) {
        iVideoListFragment.showProgress();
        Subscription subscribe = ApiManager.getInstance().getArtApiService().getVideoList(cookie, sid, page)
                .lift(new BaseValueValidOperator<VideoListBean>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iVideoListFragment.hideProgress();
                        iVideoListFragment.showError();
                    }

                    @Override
                    public void onNext(VideoListBean videoListBean) {
                        iVideoListFragment.hideProgress();
                        iVideoListFragment.setVideoDate(videoListBean.getData().getVideo());
                        iVideoListFragment.getTotalPager(videoListBean.getData().getTotalpage());
                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void getVideoDetail(String cookie, String id, String sid) {
        Subscription subscribe = ApiManager.getInstance().getArtApiService()
                .getVideoDetailInfo(cookie, id, sid)
                .lift(new BaseValueValidOperator<VideoInfoBean>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(VideoInfoBean videoInfoBean) {

                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void getUserVideoTypeList(String cookie, String sid, int page, int type) {
        Subscription subscribe = ApiManager.getInstance().getArtApiService()
                .getUserVideoTypeList(cookie, sid, page, type)
                .lift(new BaseValueValidOperator<VideoListBean>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(VideoListBean videoListBean) {
                        int videoStatustoTalpage = videoListBean.getData().getTotalpage();
                        iVideoListFragment.setVideoDate(videoListBean.getData().getVideo());
                        iVideoListFragment.getVideoStatusTotal(videoStatustoTalpage);
                    }
                });
        addSubscription(subscribe);
    }

}
