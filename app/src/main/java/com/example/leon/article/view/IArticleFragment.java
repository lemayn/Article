package com.example.leon.article.view;

import com.example.leon.article.api.bean.ArtListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public interface IArticleFragment extends IBaseActivity{

    void setArtDate(List<ArtListBean.DataBean.ArticleBean> date);

}
