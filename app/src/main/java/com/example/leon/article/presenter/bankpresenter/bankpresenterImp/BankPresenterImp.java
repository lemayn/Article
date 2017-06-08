package com.example.leon.article.presenter.bankpresenter.bankpresenterImp;

import android.content.Context;
import android.widget.Toast;

import com.example.leon.article.api.ApiManager;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.BankConfigBean;
import com.example.leon.article.api.bean.BindBankBean;
import com.example.leon.article.api.bean.UserBankBean;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.BasepresenterImp;
import com.example.leon.article.presenter.bankpresenter.IBankPresenter;
import com.example.leon.article.view.IAddCardActivity;
import com.example.leon.article.view.IBankSettingActivity;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hu on 2017/5/30.
 */

public class BankPresenterImp extends BasepresenterImp implements IBankPresenter {
    private Context context;
    private IBankSettingActivity bankSettingActivity;
    private IAddCardActivity addCardActivity;

    public BankPresenterImp(IBankSettingActivity bankSettingActivity) {
        this.bankSettingActivity = bankSettingActivity;
    }

    public BankPresenterImp(Context context, IAddCardActivity addCardActivity) {
        this.context = context;
        this.addCardActivity = addCardActivity;
    }

    @Override
    public void getBankConfig(String cookie, String sid) {
        Subscription subscribe = ApiManager.getInstance().getBankApiService()
                .getBanklist(cookie, sid)
                .lift(new BaseValueValidOperator<BankConfigBean>("getBanklist  " + cookie))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankConfigBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BankConfigBean bankConfigBean) {

                        addCardActivity.setBankConfig(bankConfigBean);
                    }
                });

        addSubscription(subscribe);

    }

    @Override
    public void bindBankCard(String cookie, String bid, String card, String sid, String account_name, String address,
                             String password) {
        Subscription subscribe = ApiManager.getInstance().getBankApiService()
                .bindBankCard(cookie, bid, card, sid, account_name, address, password)
                .lift(new BaseValueValidOperator<BindBankBean>("bindBankCard  " + cookie))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BindBankBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(BindBankBean bindBankBean) {
                        if (bindBankBean.getMsg() != null) {
                            Toast.makeText(context, bindBankBean.getMsg(), Toast.LENGTH_LONG).show();
                            addCardActivity.showResult();
                        }
                    }
                });
        addSubscription(subscribe);
    }


    @Override
    public void getUserBankInfo(String cookie, String sid) {
        Subscription subscribe = ApiManager.getInstance().getBankApiService()
                .getUserCardInfo(cookie, sid)
                .lift(new BaseValueValidOperator<UserBankBean>("getUserCardInfo  " + cookie))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserBankBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserBankBean userBankBean) {
                        bankSettingActivity.setBankDate(userBankBean);
                    }
                });
        addSubscription(subscribe);
    }
}
