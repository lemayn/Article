package com.example.leon.article.Activity.art;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leon.article.R;
import com.example.leon.article.api.bean.ArtInfoBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityArtDetailBinding;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.ArtPresenterImp;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IArtDetailActivity;
import com.zzhoujay.richtext.RichText;

/**
 * 文章列表的详情页,不可编辑可查看
 */
public class ArtDetailActivity extends ToolBarBaseActivity<ActivityArtDetailBinding> implements IArtDetailActivity{

    private String editorDate;
    private TextView tv_title;
    private ArtPresenterImp artPresenter;
    private TextView tv_content;
    private String imgUrl = "http://118.89.233.35:8989";
    private ImageView iv_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_detail);

        initDate();
        initView();
    }

    private void initDate() {
        String cookie = (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
        String aid = getIntent().getStringExtra(ArtConstant.DETAIL_AID);
        String sid = (String) SPUtil.get(Constant.Share_prf.SID, "");
        artPresenter = new ArtPresenterImp(this);

        artPresenter.getArtDetail(cookie,aid,sid);
    }

    private void initView() {
        setTitle(getString(R.string.article_detail));
        hideHeaderInfo();
        //标题头
        tv_title = (TextView) findViewById(R.id.tv_art_detail_title);
        tv_content = (TextView) findViewById(R.id.tv_art_detail_content);
        iv_detail = (ImageView) findViewById(R.id.iv_art_detail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.recycle();
        artPresenter.unsubcrible();
    }

    @Override
    public void showError() {
        //if something was wrong when network,do something here

    }

    @Override
    public void showArtDetail(ArtInfoBean bean) {
        String atitle = bean.getData().getAtitle();
        String artImg = (String) bean.getData().getAimg();
        if (artImg != null) {
            Glide.with(this)
                    .load(imgUrl+artImg)
                    .into(iv_detail);
        }
        tv_title.setText(atitle);
        editorDate = bean.getData().getAcontent();
        RichText.fromHtml(editorDate)
                .into(tv_content);
    }
}
