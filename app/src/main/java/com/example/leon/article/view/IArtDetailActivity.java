package com.example.leon.article.view;

import com.example.leon.article.api.bean.ArtInfoBean;

/**
 * Created by Administrator on 2017/5/23.
 */

public interface IArtDetailActivity {

    void showError();

    void showArtDetail(ArtInfoBean bean);
}
