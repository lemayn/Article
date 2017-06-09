package com.example.leon.article.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leon.article.Activity.art.EditorActivity;
import com.example.leon.article.R;
import com.example.leon.article.adapter.ArtListAdapter;
import com.example.leon.article.api.bean.ArtListBean;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.ArtPresenterImp;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IArticleFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

public class ArticleFragment extends Fragment implements View.OnClickListener, IArticleFragment {

//    private TextView tv_myArt;
    private TextView tv_createArt;
    private ListView lv_article;
//    private ProgressBar progressBar;
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
    private View headerView;
    private String type;
    private int artStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article, container, false);
        initView(view);
        //get cookie sid
        cookie = (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
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
                        page ++;
                        loadMore(-1);
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

    private void loadMore(final int type) {
        ll_footer_contain.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (type == 0){
                    artPresenter.getUserArtTypeList(cookie,sid,page,type);
                    ll_footer_contain.setVisibility(View.GONE);
                }
                if (type == 1){
                    artPresenter.getUserArtTypeList(cookie,sid,page,type);
                    ll_footer_contain.setVisibility(View.GONE);
                }
                if (type == 2) {
                    artPresenter.getUserArtTypeList(cookie,sid,page,type);
                    ll_footer_contain.setVisibility(View.GONE);
                } else {
                    artPresenter.getuserArtList(cookie,sid,page);
                    ll_footer_contain.setVisibility(View.GONE);
                }
            }
        },2000);
    }

    private void initDate() {
        artPresenter = new ArtPresenterImp(this);
        adapter = new ArtListAdapter(getContext());

        artPresenter.getuserArtList(cookie, sid, page);
        lv_article.setAdapter(adapter);
    }

    private void initView(View view) {
        headerView = View.inflate(getContext(), R.layout.listview_article_headerview, null);
        initSpinner(headerView);
        View footerView = View.inflate(getContext(), R.layout.listview_article_footerview, null);
        ll_footer_contain = (LinearLayout) footerView.findViewById(R.id.ll_lv_footer_contain);
//      progressBar = (ProgressBar) view.findViewById(R.id.article_progressBar);
        refreshActicle = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_acticle);
        ll_empty = (LinearLayout) view.findViewById(R.id.lv_article_empty);
        lv_article = (ListView) view.findViewById(R.id.lv_article);
        lv_article.setEmptyView(ll_empty);
        lv_article.addHeaderView(headerView);
        lv_article.addFooterView(footerView);
        tv_createArt = (TextView) view.findViewById(R.id.tv_createArt);
    }

    private void initSpinner(View headerView) {
        MaterialSpinner spinner = (MaterialSpinner) headerView.findViewById(R.id.spinner);
        spinner.setItems("全部", "已发表", "未通过", "审核中");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                type = item;
                switch (position) {
                    case 0://点击了全部
                        artStatus = -1;
                        adapter.clearDate();
                        artPresenter.getuserArtList(cookie,sid,1);
                        break;
                    case 1://点击了已发表
                        artStatus = 1;
                        adapter.clearDate();
                        artPresenter.getUserArtTypeList(cookie,sid,1,1);
                        if (adapter.getCount() >= 20) {
                            loadMore(1);
                        }
                        break;
                    case 2://点击了未通过
                        artStatus = 2;
                        adapter.clearDate();
                        artPresenter.getUserArtTypeList(cookie,sid,1,2);
                        if (adapter.getCount() >= 20) {
                            loadMore(2);
                        }
                        break;
                    case 3://点击了审核中
                        artStatus = 0;
                        adapter.clearDate();
                        artPresenter.getUserArtTypeList(cookie,sid,1,0);
                        if (adapter.getCount() >= 20) {
                            loadMore(0);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_createArt:
                goEditorActivity();
                break;
        }
    }

    private void goEditorActivity() {
        Intent intent = new Intent(getContext(), EditorActivity.class);
        startActivity(intent);
    }


    @Override
    public void showProgress() {
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
//        Toast.makeText(getContext(),"服务器繁忙，刷新一下试试吧",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setArtDate(List<ArtListBean.DataBean.ArticleBean> date) {
        if (date != null) {
            adapter.addItems(date);
        }else{
            artPresenter.getuserArtList(cookie,sid,1);
            if (type != null) {
                Toast.makeText(getContext(),"您还没有"+type+"的文章",Toast.LENGTH_SHORT).show();
            }
        }
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
