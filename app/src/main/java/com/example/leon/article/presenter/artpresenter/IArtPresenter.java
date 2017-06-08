package com.example.leon.article.presenter.artpresenter;

/**
 * Created by Administrator on 2017/5/22.
 */

public interface IArtPresenter {

    //获取用户全部文章列表
    void getuserArtList(String cookie,String sid,int page);

    //获取单篇文章详情
    void getArtDetail(String cookie,String aid,String sid);

    //上传用户文章
    void uploadUserArt(String cookie,String title,String content,String sid,String img,String Class_id);

    //获取用户发表的文章种类(0表示未审核，1表示已审核，2表示审核未通过)
    void getUserArtTypeList(String cookie,String sid, int page,int type);

    //获取上传文章分类信息
    void getUploadClassify(String cookie,String sid);

}
