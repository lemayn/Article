package com.example.leon.article.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leon.article.Activity.presenter.HomePresenter;
import com.example.leon.article.Activity.presenter.IHomePre;
import com.example.leon.article.R;
import com.example.leon.article.adapter.HomeRecycleAdapter;
import com.example.leon.article.adapter.HomeRecycleAdapter2;
import com.example.leon.article.bean.AdvBean;
import com.example.leon.article.bean.ExcellentBean;
import com.example.leon.article.bean.NoticeBean;
import com.example.leon.article.bean.RecomArtBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by leonseven on 2017/5/25.
 */

public class HomeFragment extends Fragment implements IHomePre {

    View view = null;

    private Handler mHandler = new Handler();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private HomeRecycleAdapter adapter;
    private HomeRecycleAdapter2 adapter2;
    private HomePresenter homePresenter;

//    private LinearLayout layout_more;

    private int page = 1;   //当前页数
    private int totalpager; //总页数
    private boolean isLoadMore;

    private static final String ACTION = "simple_action";
    private static final String DATA = "data";

    BroadcastReceiver mReceiver;
    List<AdvBean.DataBean> DatabeanList = new ArrayList<AdvBean.DataBean>();
    List<RecomArtBean.DataBean.TuijianBean> reBeanlist = new ArrayList<RecomArtBean.DataBean.TuijianBean>();
    List<RecomArtBean.DataBean.TuijianBean> reBeanlist2 = new ArrayList<RecomArtBean.DataBean.TuijianBean>();

    List<NoticeBean.DataBean> noticeBean = new ArrayList<NoticeBean.DataBean>();

    List<ExcellentBean.DataBean.GoodBean> goodBean = new ArrayList<ExcellentBean.DataBean.GoodBean>();


    //广播接受
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("article1")){
                SetAda();
            }
            else if (intent.getAction().equals("article2")){
                recyclerView.setAdapter(adapter2 = new HomeRecycleAdapter2(DatabeanList, goodBean,
                        noticeBean, getActivity()));
            }
        }

    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        homePresenter = new HomePresenter(getContext(), this);
        ClearData();
        init();
        return view;
    }


    @Override
    public void init() {
//        layout_more = (LinearLayout) this.view.findViewById(R.id.layout_more);

        recyclerView = (RecyclerView) this.view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = (SwipeRefreshLayout) this.view.findViewById(R.id.swipeRefresh_home);

        getData(page);

        swipeRefreshLayout.setColorSchemeResources(R.color.red,
                R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
//        refresh();//初始刷新
        SetAda();

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "正在刷新", Toast.LENGTH_SHORT).show();
                refresh();
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem ;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //停止滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示Item的Position
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部，并且不在加载状态
                    if (lastVisibleItem == (totalItemCount - 1) && !isLoadMore) {
//                        layout_more.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(true);
                        Toast.makeText(getActivity(), "加载中...", Toast.LENGTH_SHORT).show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLoadMore = true;
                                Log.i("MyTest", "正在加载");

                                if (page < totalpager){
                                    page = page + 1;
                                    setLoadMore(true);
                                }
                                else {
                                    setLoadMore(false);
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 2000);
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

    }

    public void setLoadMore(boolean complete) {
        if (complete) {
//            layout_more.setVisibility(View.GONE);
            loadMore(page);
        } else {
//            layout_more.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "没有更多数据了!", Toast.LENGTH_SHORT).show();
        }
        isLoadMore = false;
    }


    private void refresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ClearData();
                page = 1;
                getData(page);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);

    }

    public void SetAda() {
//        adapter.clearDate();
        recyclerView.setAdapter(adapter = new HomeRecycleAdapter(DatabeanList, reBeanlist,
                noticeBean, getActivity()));
    }


    //获得数据
    private void getData(int pagenow){
        homePresenter.AdvData();
        homePresenter.RecommendArticle(pagenow);
        homePresenter.SystemNotice();
        homePresenter.excellentArticle(pagenow);
    }

    private void loadMore(int pagenow){
        homePresenter.RecommendArticle(pagenow);
        homePresenter.excellentArticle(pagenow);
    }

    private void ClearData() {
        DatabeanList.clear();
        reBeanlist.clear();
        noticeBean.clear();
        goodBean.clear();
    }

    @Override
    public void showAdvList(List<AdvBean.DataBean> List) {
//        DatabeanList.addAll(List);
        adapter.addAdvItems(List);
    }


    @Override
    public void showRecommendList(List<RecomArtBean.DataBean.TuijianBean> List, int page) {
        totalpager = page;
//        Log.i("MyTest", "rebeanlistsize "+List.size() + " page "+page);
        reBeanlist.addAll(List);
//        SetAda();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        adapter.addArtItems(List);
    }

    @Override
    public void showGoodList(List<ExcellentBean.DataBean.GoodBean> List, int page) {
        totalpager = page;
        goodBean.addAll(List);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showNoticeList(List<NoticeBean.DataBean> List) {
        adapter.addNotItems(List);
    }


    @Override
    public void onResume() {
        super.onResume();

        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("article2");
        filter.addAction("article1");
        getActivity().registerReceiver(this.broadcastReceiver, filter);
    }
}
