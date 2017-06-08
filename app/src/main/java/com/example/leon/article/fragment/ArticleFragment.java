package com.example.leon.article.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leon.article.Activity.art.EditorActivity;
import com.example.leon.article.Activity.art.MySqlActivity;
import com.example.leon.article.R;
import com.example.leon.article.adapter.ArtListAdapter;
import com.example.leon.article.api.bean.ArtListBean;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.ArtPresenterImp;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IArticleFragment;

import java.util.List;

public class ArticleFragment extends Fragment implements View.OnClickListener, IArticleFragment {

    private TextView tv_myArt;
    private TextView tv_createArt;
    private ListView lv_article;
    private ProgressBar progressBar;
    private ArtListAdapter adapter;
    private ArtPresenterImp artPresenter;
    private SwipeRefreshLayout refreshActicle;
    private int page = 1;   //当前页数
    private int totalpager; //总页数
    private String cookie;
    private String sid;
    private LinearLayout ll_empty;
    private boolean isBottom = false;
    private LinearLayout ll_footer_contain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article, container, false);
        initView(view);
        //get cookie sid
        cookie = (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
        Log.e("Retrofit", cookie);
        sid = (String) SPUtil.get(Constant.Share_prf.SID, "");
        initDate();
        initEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (adapter.getCount() > 0) {
            adapter.clearDate();
            artPresenter.getuserArtList(cookie,sid,1);
        }*/
    }

    private void initEvent() {
        tv_myArt.setOnClickListener(this);
        tv_createArt.setOnClickListener(this);
        refreshActicle.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshActicle.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshActicle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActicle.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clearDate();
                        page = 1;
                        artPresenter.getuserArtList(cookie, sid, page);
                        lv_article.setAdapter(adapter);
                        refreshActicle.setRefreshing(false);
                        Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
            }
        });

        lv_article.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && isBottom) {
                    if (page < totalpager) {
                        page++;
                        loadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) >= 20) {
                    isBottom = true;
                }
            }
        });
    }

    private void loadMore() {
        ll_footer_contain.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                artPresenter.getuserArtList(cookie, sid, page);
                ll_footer_contain.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void initDate() {
        artPresenter = new ArtPresenterImp(this);
        adapter = new ArtListAdapter(getContext());
        Log.e("Retrofit  initDate", cookie);
        artPresenter.getuserArtList(cookie, sid, page);
        lv_article.setAdapter(adapter);
    }

    private void initView(View view) {
        View headerView = View.inflate(getContext(), R.layout.listview_article_headerview, null);
        View footerView = View.inflate(getContext(), R.layout.listview_article_footerview, null);
        ll_footer_contain = (LinearLayout) footerView.findViewById(R.id.ll_lv_footer_contain);
        progressBar = (ProgressBar) view.findViewById(R.id.article_progressBar);
        refreshActicle = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_acticle);
        ll_empty = (LinearLayout) view.findViewById(R.id.lv_article_empty);
        lv_article = (ListView) view.findViewById(R.id.lv_article);
        lv_article.setEmptyView(ll_empty);
        lv_article.addHeaderView(headerView);
        lv_article.addFooterView(footerView);
        tv_myArt = (TextView) view.findViewById(R.id.tv_myArt);
        tv_createArt = (TextView) view.findViewById(R.id.tv_createArt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_myArt:
                goMySqlActivity();
                break;
            case R.id.tv_createArt:
                goEditorActivity();
                break;
        }
    }

    private void goEditorActivity() {
        Intent intent = new Intent(getContext(), EditorActivity.class);
        startActivity(intent);
    }

    private void goMySqlActivity() {
        Intent intent = new Intent(getContext(), MySqlActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        //        Toast.makeText(getContext(),"服务器繁忙，刷新一下试试吧",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setArtDate(List<ArtListBean.DataBean.ArticleBean> date) {
        adapter.addItems(date);
    }

    @Override
    public void getTotalPager(int totalPager) {
        if (totalPager != -1) {
            this.totalpager = totalPager;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        artPresenter.unsubcrible();
    }
}
