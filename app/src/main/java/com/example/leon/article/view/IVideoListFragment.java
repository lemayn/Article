package com.example.leon.article.view;

import com.example.leon.article.api.bean.VideoListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public interface IVideoListFragment extends IBaseActivity{

    void setVideoDate(List<VideoListBean.DataBean.VideoBean> date);

    void getTotalPager(int totalPager);

    void getVideoStatusTotal(int statusTotalpager);
}
