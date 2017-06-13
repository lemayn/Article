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
import android.widget.ImageView;
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
    private ArtListAdapter adapter;
    private ArtPresenterImp artPresenter;
    private SwipeRefreshLayout refreshActicle;
    private int page = 1;   //当前页数
    private int totalpager; //总页数
    private int artStatusTotalpager;//各个状态栏的总页数
    private int statusPage = 1; //当前状态栏的页数(默认为1)
    private boolean isBottom = false;
    private LinearLayout ll_footer_contain;
    private String artType;
    private int artStatus;
    private Handler handler = new Handler();
    private ImageView iv_creatArt;
    private boolean ifClick = false;
    private boolean isClickSpinner = false;
    private int statusIndex;

    public static String getCookie(){
        return (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
    }

    public static String getSid(){
        return (String) SPUtil.get(Constant.Share_prf.SID, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article, container, false);
        initView(view);
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
        iv_creatArt.setOnClickListener(this);
        refreshActicle.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshActicle.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshActicle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshActicle.setRefreshing(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clearDate();
                        switch (statusIndex) {
                            case 0: //刷新已发表文章
                                ifClick = false;
                                artPresenter.getuserArtList(getCookie(), getSid(), 1);
                                break;
                            case 1: //刷新已通过文章
                                ifClick = true;
                                artPresenter.getUserArtTypeList(getCookie(), getSid(), 1, 1);
                                break;
                            case 2: //刷新未通过文章
                                ifClick = true;
                                artPresenter.getUserArtTypeList(getCookie(), getSid(), 1, 2);
                                break;
                            case 3: //刷新审核中文章
                                ifClick = true;
                                artPresenter.getUserArtTypeList(getCookie(), getSid(), 1, 0);
                                break;
                        }
//                        artPresenter.getuserArtList(cookie, sid, 1);
                        lv_article.setAdapter(adapter);
                        refreshActicle.setRefreshing(false);
                        Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
                page = 1;
                statusPage = 1;
            }
        });

        lv_article.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && isBottom) {
                    if (ifClick) {//判断文章分类是否需要加载更多
                        if (statusPage < artStatusTotalpager) {//超过一页需要加载更多
                            statusPage++;
                            loadMoreStatus(artStatus);
                        }
                    } else {
                        if (page < totalpager) {
                            page++;
                            loadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int totalItems = firstVisibleItem + visibleItemCount;
                if ((totalItems) >= totalItemCount && totalItems >= 20) {
                    isBottom = true;
                }
            }
        });
    }

    private void loadMoreStatus(final int artStatus) {
        ll_footer_contain.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                artPresenter.getUserArtTypeList(getCookie(), getSid(), statusPage, artStatus);
                ll_footer_contain.setVisibility(View.GONE);
            }
        }, 1700);
    }

    private void loadMore() {
        ll_footer_contain.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                artPresenter.getuserArtList(getCookie(), getSid(), page);
                ll_footer_contain.setVisibility(View.GONE);
            }
        }, 1700);
    }

    private void initDate() {
        artPresenter = new ArtPresenterImp(this);
        adapter = new ArtListAdapter(getContext());
        artPresenter.getuserArtList(getCookie(), getSid(), page);
        lv_article.setAdapter(adapter);
    }

    private void initView(View view) {
        initSpinner(view);
        View footerView = View.inflate(getContext(), R.layout.listview_article_footerview, null);
        ll_footer_contain = (LinearLayout) footerView.findViewById(R.id.ll_lv_footer_contain);
        refreshActicle = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_acticle);
        lv_article = (ListView) view.findViewById(R.id.lv_article);
        lv_article.addFooterView(footerView);
        tv_createArt = (TextView) view.findViewById(R.id.tv_createArt);
        iv_creatArt = (ImageView) view.findViewById(R.id.icon_creat_article);
    }

    private void initSpinner(View view) {
        MaterialSpinner spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        spinner.setItems("已发表", "已通过", "未通过", "审核中");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                statusIndex = position;
                artType = item;
                page = 1;
                statusPage = 1;
                isClickSpinner = true;
                switch (position) {
                    case 0://点击了全部（已发表）
                        ifClick = false;
                        artStatus = 3;
                        adapter.clearDate();
                        artPresenter.getuserArtList(getCookie(), getSid(), page);
                        break;
                    case 1://点击了已发表（已通过）
                        ifClick = true;
                        artStatus = 1;
                        adapter.clearDate();
                        artPresenter.getUserArtTypeList(getCookie(), getSid(), 1, 1);
                        break;
                    case 2://点击了未通过
                        ifClick = true;
                        artStatus = 2;
                        adapter.clearDate();
                        artPresenter.getUserArtTypeList(getCookie(), getSid(), 1, 2);
                        break;
                    case 3://点击了审核中
                        ifClick = true;
                        artStatus = 0;
                        adapter.clearDate();
                        artPresenter.getUserArtTypeList(getCookie(), getSid(), page, 0);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_createArt:
            case R.id.icon_creat_article:
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
        } else {
            if (artType != null && isClickSpinner) {
                Toast.makeText(getContext(), "您还没有" + artType + "的文章", Toast.LENGTH_SHORT).show();
                isClickSpinner = false;
            }
        }
    }

    @Override
    public void getTotalPager(int totalPager) {
        this.totalpager = totalPager;
    }

    @Override
    public void getArtStatusTotal(int statusTotalpager) {
        this.artStatusTotalpager = statusTotalpager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        artPresenter.unsubcrible();
    }
}
