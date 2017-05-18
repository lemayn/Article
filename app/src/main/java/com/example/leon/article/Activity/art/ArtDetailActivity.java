package com.example.leon.article.Activity.art;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityArtDetailBinding;

/**
 * 文章列表的详情页,不可编辑可查看
 */
public class ArtDetailActivity extends ToolBarBaseActivity<ActivityArtDetailBinding> {

    private WebView mWebView;
    private String editorDate;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_detail);

        editorDate = getIntent().getStringExtra("");

        initView();
    }

    private void initView() {
        setTitle(getString(R.string.article_detail));
        hideHeaderInfo();
        //标题头
        tv_title = (TextView) findViewById(R.id.tv_art_detail_title);
        mWebView = (WebView) findViewById(R.id.web_artDetail);
        mWebView.loadDataWithBaseURL(null, editorDate,"text/html","utf-8",null);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mWebView = null;
    }
}
