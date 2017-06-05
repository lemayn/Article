package com.example.leon.article.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by leonseven on 2017/5/25.
 */

public class HomeFragment extends Fragment implements IHomePre {

    View view = null;
    private RecyclerView recyclerView;
    private HomeRecycleAdapter adapter;
    private HomeRecycleAdapter2 adapter2;
    private HomePresenter homePresenter;

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
        DatabeanList.clear();
        reBeanlist.clear();
        noticeBean.clear();
        goodBean.clear();

        init();
        return view;
    }


    @Override
    public void init() {
        recyclerView = (RecyclerView) this.view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 1, GridLayoutManager.VERTICAL, false));

        homePresenter.AdvData();

        homePresenter.RecommendArticle();
        homePresenter.SystemNotice();
        homePresenter.excellentArticle();

        SetAda();

    }

    public void SetAda() {

        recyclerView.setAdapter(adapter = new HomeRecycleAdapter(DatabeanList, reBeanlist,
                noticeBean, getActivity()));
    }


    @Override
    public void showAdvList(List<AdvBean.DataBean> List) {
//        DatabeanList.addAll(List);
        adapter.addAdvItems(List);
    }

    @Override
    public void showRecommendList(List<RecomArtBean.DataBean.TuijianBean> List) {
//        reBeanlist.addAll(List);
        adapter.addArtItems(List);
    }

    @Override
    public void showGoodList(List<ExcellentBean.DataBean.GoodBean> List) {
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
