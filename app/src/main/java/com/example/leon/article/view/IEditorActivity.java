package com.example.leon.article.view;

import com.example.leon.article.api.bean.UploadClassifyBean;

/**
 * Created by Administrator on 2017/5/22.
 */

public interface IEditorActivity extends IBaseActivity{

    void showSuccess();

    void showFailure();

    void setUploadClassfiy(UploadClassifyBean classifyBean);
}
