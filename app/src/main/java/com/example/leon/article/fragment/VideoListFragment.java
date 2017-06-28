package com.example.leon.article.fragment;

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
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.adapter.VideoListAdapter;
import com.example.leon.article.api.bean.VideoListBean;
import com.example.leon.article.presenter.videopresenter.videopresenterImp.VideoPresenterImp;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IVideoListFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class VideoListFragment extends Fragment implements IVideoListFragment{

    private ListView lv_video;
    private VideoListAdapter adapter;
    private SwipeRefreshLayout mRefresh;
    private int page = 1;   //当前页数
    private int totalpager; //总页数
    private int videoStatusTotalpager;//各个状态栏的总页数
    private int statusPage = 1; //当前状态栏的页数(默认为1)
    private boolean isBottom = false;
    private LinearLayout ll_footer_contain;
    private String videoType;
    private int artStatus;
    private Handler handler = new Handler();
    private boolean ifClick = false;
    private boolean isClickSpinner = false;
    private int statusIndex;
    private MaterialSpinner spinner;
    private VideoPresenterImp videoPresenterImp;

    public static String getCookie(){
        return (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
    }

    public static String getSid(){
        return (String) SPUtil.get(Constant.Share_prf.SID, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videolist, container, false);
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
        mRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clearDate();
                        switch (statusIndex) {
                            case 0: //刷新已发表文章
                                ifClick = false;
                                videoPresenterImp.getuserVideoList(getCookie(), getSid(), 1);
                                break;
                            case 1: //刷新已通过文章
                                ifClick = true;
                                videoPresenterImp.getUserVideoTypeList(getCookie(), getSid(), 1, 1);
                                break;
                            case 2: //刷新未通过文章
                                ifClick = true;
                                videoPresenterImp.getUserVideoTypeList(getCookie(), getSid(), 1, 2);
                                break;
                            case 3: //刷新审核中文章
                                ifClick = true;
                                videoPresenterImp.getUserVideoTypeList(getCookie(), getSid(), 1, 0);
                                break;
                        }
                        lv_video.setAdapter(adapter);
                        mRefresh.setRefreshing(false);
                        Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
                page = 1;
                statusPage = 1;
            }
        });

        lv_video.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && isBottom) {
                    if (ifClick) {//判断文章分类是否需要加载更多
                        if (statusPage < videoStatusTotalpager) {//超过一页需要加载更多
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
                videoPresenterImp.getUserVideoTypeList(getCookie(), getSid(), statusPage, artStatus);
                ll_footer_contain.setVisibility(View.GONE);
            }
        }, 1700);
    }

    private void loadMore() {
        ll_footer_contain.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoPresenterImp.getuserVideoList(getCookie(), getSid(), page);
                ll_footer_contain.setVisibility(View.GONE);
            }
        }, 1700);
    }

    private void initDate() {
        videoPresenterImp = new VideoPresenterImp(this);
        adapter = new VideoListAdapter(getContext());
        videoPresenterImp.getuserVideoList(getCookie(), getSid(), page);
        lv_video.setAdapter(adapter);
    }

    private void initView(View view) {
        spinner = (MaterialSpinner) view.findViewById(R.id.spinner_videoList);
        initSpinner();
        View footerView = View.inflate(getContext(), R.layout.listview_article_footerview, null);
        ll_footer_contain = (LinearLayout) footerView.findViewById(R.id.ll_lv_footer_contain);
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_videolist);
        lv_video = (ListView) view.findViewById(R.id.lv_video);
        lv_video.addFooterView(footerView);
    }

    private void initSpinner() {
        spinner.setItems("已发表", "已通过", "未通过", "审核中");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                statusIndex = position;
                videoType = item;
                page = 1;
                statusPage = 1;
                isClickSpinner = true;
                switch (position) {
                    case 0://点击了全部（已发表）
                        ifClick = false;
                        artStatus = 3;
                        adapter.clearDate();
                        videoPresenterImp.getuserVideoList(getCookie(), getSid(), page);
                        break;
                    case 1://点击了已发表（已通过）
                        ifClick = true;
                        artStatus = 1;
                        adapter.clearDate();
                        videoPresenterImp.getUserVideoTypeList(getCookie(), getSid(), 1, 1);
                        break;
                    case 2://点击了未通过
                        ifClick = true;
                        artStatus = 2;
                        adapter.clearDate();
                        videoPresenterImp.getUserVideoTypeList(getCookie(), getSid(), 1, 2);
                        break;
                    case 3://点击了审核中
                        ifClick = true;
                        artStatus = 0;
                        adapter.clearDate();
                        videoPresenterImp.getUserVideoTypeList(getCookie(), getSid(), page, 0);
                        break;
                }
            }
        });
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
    public void setVideoDate(List<VideoListBean.DataBean.VideoBean> date) {
        if (date != null) {
            adapter.addItems(date);
        } else {
            if (videoType != null && isClickSpinner) {
                Toast.makeText(getContext(), "您还没有" + videoType + "的视频", Toast.LENGTH_SHORT).show();
                isClickSpinner = false;
            }
        }
    }

    @Override
    public void getTotalPager(int totalPager) {
        this.totalpager = totalPager;
    }

    @Override
    public void getVideoStatusTotal(int statusTotalpager) {
        this.videoStatusTotalpager = statusTotalpager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoPresenterImp.unsubcrible();
    }
}
