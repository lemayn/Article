package com.example.leon.article.Activity.presenter;

import com.example.leon.article.bean.AdvBean;
import com.example.leon.article.bean.ExcellentBean;
import com.example.leon.article.bean.NoticeBean;
import com.example.leon.article.bean.RecomArtBean;

import java.util.List;

/**
 * Created by leonseven on 2017/5/27.
 */

public interface IHomePre extends IBaseView {

    void showAdvList(List<AdvBean.DataBean> List);
    void showRecommendList(List<RecomArtBean.DataBean.TuijianBean> List, int page);
    void showGoodList(List<ExcellentBean.DataBean.GoodBean> List, int page);
    void showNoticeList(List<NoticeBean.DataBean> List);

}
