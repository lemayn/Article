package com.example.leon.article.presenter.artpresenter.artpresenterImp;

import com.example.leon.article.presenter.Basepresenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/5/22.
 */

public class BasepresenterImp implements Basepresenter{

    private CompositeSubscription mCompositeSubscription;

    protected void addSubscription(Subscription subscription) {
        if (this.mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }

    @Override
    public void unsubcrible() {
        if (this.mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
