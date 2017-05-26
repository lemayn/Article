package com.example.leon.article.presenter.artpresenter;

/**
 * Created by Administrator on 2017/5/22.
 */

public interface IArtPresenter {

    //获取用户文章列表
    void getuserArtList(String cookie,String sid,int page);

    //获取单篇文章详情
    void getArtDetail(String cookie,String aid,String sid);

    //上传用户文章
    void uploadUserArt(String cookie,String title,String content,String sid,String img);
}
