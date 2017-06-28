package com.example.leon.article.presenter.videopresenter;

/**
 * Created by Administrator on 2017/6/27.
 */

public interface IVideoPresenter {

    void getClassify(String cookie,String sid);

    //获取用户全部文章列表
    void getuserVideoList(String cookie,String sid,int page);

    //获取单篇文章详情
    void getVideoDetail(String cookie,String aid,String sid);

    //获取用户发表的文章种类(0表示未审核，1表示已审核，2表示审核未通过)
    void getUserVideoTypeList(String cookie,String sid, int page,int type);

}
