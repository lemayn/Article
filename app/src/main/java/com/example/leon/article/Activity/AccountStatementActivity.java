package com.example.leon.article.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.adapter.AccountStatementAdapter;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.StatementBean;
import com.example.leon.article.databinding.ActivityAccountStatementBinding;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.widget.CustomDialog;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class AccountStatementActivity extends AppCompatActivity {

    private ActivityAccountStatementBinding binding;
    private boolean isLoading;
    private AccountStatementAdapter statementAdapter;
    private int totalPage;
    private int currentPage;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_statement);

        initView();
    }

    private void initView() {
        initToolbar();
        initDialog();
        initSwipeRefresh();
        initRecyclerView();

        loadMore(1, true);
        showProgressDialog();
    }

    private void initDialog() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        //设置不显示自带的 Title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.tvToolbarTitle.setText("我的账单");
        binding.ivToolbarBack.setVisibility(View.VISIBLE);
        binding.ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initSwipeRefresh() {
        binding.statementSwipe.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        binding.statementSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore(1, true);
            }
        });
    }

    private int lastVisibleItem;

    private void initRecyclerView() {
        binding.statementRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        statementAdapter = new AccountStatementAdapter();
        binding.statementRecycler.setAdapter(statementAdapter);
        binding.statementRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.statementRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == statementAdapter.getItemCount()) {
                    boolean isRefreshing = binding.statementSwipe.isRefreshing();
                    if (isRefreshing) {
                        statementAdapter.notifyItemRemoved(statementAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        if (currentPage < totalPage) {
                            isLoading = true;
                            loadMore(currentPage + 1, false);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadMore(int page, final boolean isClear) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        hashMap.put("page", String.valueOf(page));
        ApiFactory.getApi().statement(Constant.Api.V2_MONEY_CONFIG, hashMap)
                .lift(new BaseValueValidOperator<StatementBean>())
                .compose(this.<StatementBean>rxSchedulerHelper())
                .subscribe(new Subscriber<StatementBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                        if (binding.statementSwipe.isRefreshing()) {
                            binding.statementSwipe.setRefreshing(false);
                        }
                        Toast.makeText(AccountStatementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(StatementBean statementBean) {
                        isLoading = false;
                        if (binding.statementSwipe.isRefreshing()) {
                            binding.statementSwipe.setRefreshing(false);
                        }
                        if(statementBean.getData().getMoneyConfig() == null) {
                            return;
                        }
                        totalPage = statementBean.getData().getTotalpage();
                        currentPage = statementBean.getData().getPage();
                        statementAdapter.addStatement(statementBean.getData().getMoneyConfig(), isClear);
                    }
                });
    }

    public <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> commentsBeanObservable) {
                return commentsBeanObservable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                SystemClock.sleep(300);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                dismissProgressDialog();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private void showProgressDialog() {
        if (customDialog != null && !customDialog.isShowing()) {
            customDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }
}