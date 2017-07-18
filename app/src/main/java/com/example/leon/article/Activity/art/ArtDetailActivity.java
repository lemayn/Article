package com.example.leon.article.Activity.art;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leon.article.Activity.art.util.StringUtils;
import com.example.leon.article.R;
import com.example.leon.article.api.bean.ArtInfoBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityArtDetailBinding;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.ArtPresenterImp;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.CornersTransform;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IArtDetailActivity;
import com.sendtion.xrichtext.RichTextView;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文章列表的详情页,不可编辑可查看
 */
public class ArtDetailActivity extends ToolBarBaseActivity<ActivityArtDetailBinding> implements IArtDetailActivity{

    private String editorDate;
    private TextView tv_title;
    private ArtPresenterImp artPresenter;
    private RichTextView tv_content;
    private String imgUrl = ArtConstant.BASE_IMGURL;
    private ImageView iv_detail;
    private TextView tv_time;
    private ProgressDialog loadingDialog;

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
        setNavigationView();
        //标题头
        tv_title = (TextView) findViewById(R.id.tv_art_detail_title);
        tv_content = (RichTextView) findViewById(R.id.tv_art_detail_content);
        tv_time = (TextView) findViewById(R.id.tv_art_detail_time);
        iv_detail = (ImageView) findViewById(R.id.iv_art_detail);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("正在加载中...");
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        String addtime = bean.getData().getAaddtime();
        if (!TextUtils.isEmpty(artImg)) {
            Glide.with(this)
                    .load(imgUrl+artImg)
                    .centerCrop()
                    .transform(new CornersTransform(ArtDetailActivity.this))
                    .crossFade()
                    .into(iv_detail);
            iv_detail.setVisibility(View.VISIBLE);
        }else{
            iv_detail.setVisibility(View.VISIBLE);
        }
        tv_title.setText(atitle);
        tv_time.setText("更新时间："+addtime);
        editorDate = bean.getData().getAcontent();
        tv_content.post(new Runnable() {
            @Override
            public void run() {
                //showEditData(myContent);
                tv_content.clearAllLayout();
                showDataSync(editorDate);
            }
        });
       /* String replace = editorDate.replace("<br/>", "\n" + "\t\t\t\t").replace();
        tv_content.setText("\t\t\t\t" + replace);*/
    }

    /**
     * 异步方式显示数据
     * @param html
     */
    private void showDataSync(final String html) {
        loadingDialog.show();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                showEditData(subscriber , html);
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        e.printStackTrace();
                        showToast("解析错误：图片不存在或已损坏");
                    }

                    @Override
                    public void onNext(String text) {
                        Log.i("FiDo", "获得的数据为： "+text);
                        if (text.contains("/upload/aimg/")){
                            // TODO 后期换成特定的字符
                            String imagePath = text;
                            Log.i("FiDo", "获得的图片地址为: "+imagePath);
                            tv_content.addImageViewAtIndex(tv_content.getLastIndex(), imgUrl + imagePath);
                        } else {
                            tv_content.addTextViewAtIndex(tv_content.getLastIndex(), Html.fromHtml(text));
                        }
                    }
                });
    }

    /**
     * 显示数据
     * @param html
     */
    private void showEditData(Subscriber<? super String> subscriber, String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img") && text.contains("src=")) {
                    String imagePath = StringUtils.getImgSrc(text);
                    Log.i("FiDo", "showEditData: "+imagePath);
                    subscriber.onNext(imagePath);
//                    tv_content.addImageViewAtIndex(i,imagePath);
                    /*if (new File(imagePath).exists()) {
                        subscriber.onNext(imagePath);
                    } else {
                        showToast("图片"+1+"已丢失，请重新插入！");
                    }*/
                } else {
                    subscriber.onNext(text);
                }
            }
            subscriber.onCompleted();
        } catch (Exception e){
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    /** 显示吐司 **/
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
