package com.example.leon.article.presenter.artpresenter.artpresenterImp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.leon.article.api.ApiManager;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArtInfoBean;
import com.example.leon.article.api.bean.ArtListBean;
import com.example.leon.article.api.bean.UpLoadArtBean;
import com.example.leon.article.api.bean.UploadClassifyBean;
import com.example.leon.article.presenter.artpresenter.IArtPresenter;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IArtDetailActivity;
import com.example.leon.article.view.IArticleFragment;
import com.example.leon.article.view.IEditorActivity;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/22.
 */

public class ArtPresenterImp extends BasepresenterImp implements IArtPresenter{

    private Context context;
    private IArticleFragment articleFragment;
    private IEditorActivity editorActivity;
    private IArtDetailActivity detailActivity;

    public ArtPresenterImp(IArticleFragment articleFragment) {
        this.articleFragment = articleFragment;
    }

    public ArtPresenterImp(Context context,IEditorActivity editorActivity) {
        this.context = context;
        this.editorActivity = editorActivity;
    }

    public ArtPresenterImp(IArtDetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    @Override
    public void getuserArtList(String cookie,String sid,int page) {
        cookie = (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
        sid = (String) SPUtil.get(Constant.Share_prf.SID, "");
        articleFragment.showProgress();
        Subscription subscribe = ApiManager.getInstance().getArtApiService()
                .getArtList(cookie,sid,page)
                .lift(new BaseValueValidOperator<ArtListBean>())
                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        articleFragment.hideProgress();
                        articleFragment.showError();
                    }

                    @Override
                    public void onNext(ArtListBean artListBean) {
                        articleFragment.hideProgress();
                        articleFragment.setArtDate(artListBean.getData().getArticle());
                        articleFragment.getTotalPager(artListBean.getData().getTotalpage());
                    }
                });
            addSubscription(subscribe);
    }

    @Override
    public void getArtDetail(String cookie,String aid,String sid) {
        Subscription subscribe = ApiManager.getInstance().getArtApiService()
                .getArtInfo(cookie, aid, sid)
                .lift(new BaseValueValidOperator<ArtInfoBean>())
                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        detailActivity.showError();
                    }

                    @Override
                    public void onNext(ArtInfoBean artInfoBean) {
                        detailActivity.showArtDetail(artInfoBean);
                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void uploadUserArt(String cookie, String title, String content,String sid,String imgBase64,String Class_id) {
        editorActivity.showProgress();
        Subscription subscribe = ApiManager.getInstance().getArtApiService()
                .uploadArt(cookie, title, content,sid,imgBase64,Class_id)
                .lift(new BaseValueValidOperator<UpLoadArtBean>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpLoadArtBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        editorActivity.hideProgress();
                        Log.i("HT", "onError: "+e.getMessage());
//                        editorActivity.showError();
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(final UpLoadArtBean upLoadArtBean) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                editorActivity.hideProgress();
                                if (upLoadArtBean.getCode().equals("1")) {
                                    editorActivity.showSuccess();
                                } else {
                                    editorActivity.showFailure();
                                }
                            }
                        },2000);
                    }
                });
            addSubscription(subscribe);
    }

    @Override
    public void getUserArtTypeList(String cookie, String sid, int page, int type) {
        Subscription subscribe = ApiManager.getInstance().getArtApiService()
                .getUserArtTypeList(cookie, sid, page, type)
                .lift(new BaseValueValidOperator<ArtListBean>())
                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("HT", "getUserArtTypeList--->onError: "+e.getMessage());
                    }

                    @Override
                    public void onNext(ArtListBean artListBean) {
                        int artStatustoTalpage = artListBean.getData().getTotalpage();
                        articleFragment.setArtDate(artListBean.getData().getArticle());
                        articleFragment.getArtStatusTotal(artStatustoTalpage);
                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void getUploadClassify(String cookie, String sid) {
        Subscription subscribe = ApiManager.getInstance().getArtApiService()
                .getClassify(cookie, sid)
                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadClassifyBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UploadClassifyBean uploadClassifyBean) {
                        editorActivity.setUploadClassfiy(uploadClassifyBean);
                    }
                });
        addSubscription(subscribe);
    }

}
