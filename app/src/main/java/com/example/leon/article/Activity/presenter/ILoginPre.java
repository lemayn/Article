package com.example.leon.article.Activity.presenter;

import com.example.leon.article.bean.AdvBean;

import java.util.List;

/**
 * Created by leonseven on 2017/5/31.
 */

public interface ILoginPre extends IBaseView {

    void showAdvList(List<AdvBean.DataBean> List);
}
