package com.example.leon.article.Activity.video;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.leon.article.Activity.MainActivity;
import com.example.leon.article.Activity.art.ArtConstant;
import com.example.leon.article.R;
import com.example.leon.article.api.ApiManager;
import com.example.leon.article.api.bean.UpLoadArtBean;
import com.example.leon.article.api.bean.UploadClassifyBean;
import com.example.leon.article.presenter.videopresenter.videopresenterImp.VideoPresenterImp;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.CreateBitmap;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.utils.UriAllUriUtils;
import com.example.leon.article.view.IUpVideoActivity;
import com.example.leon.article.widget.FllScreenVideoView;
import com.example.leon.article.widget.SelectVideoPopupWindow;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpVideoActivity extends AppCompatActivity implements View.OnClickListener, SelectVideoPopupWindow.OnSelectedListener,IUpVideoActivity {

    private static final int VIDEO_KU = 12345;
    private EditText et_title;
    private MaterialSpinner mSpinner;
    private Button bt_addvideo,bt_videolist,bt_upload;
    private SelectVideoPopupWindow selectVideoPopupWindow;
    private FllScreenVideoView mVideoView;
    private ImageView iv_play;
    private FrameLayout fl_myVideo;
    private ArrayList<UploadClassifyBean.DataBean> classifys = new ArrayList<>();
    private List<String> items = new ArrayList<>();
    private int selectPosition = 1;
    private String path = "";
    private AVLoadingIndicatorView avi_upload;
    private VideoPresenterImp videoPresenterImp;
    private ImageView iv_cover,iv_back;
    private String bytesFromBitmap;
    private EditText et_videoContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_video);
        initView();
        GetData();
        initEvent();
    }

    private void GetData() {
        //初始化presenter
        videoPresenterImp = new VideoPresenterImp(this);
        videoPresenterImp.getClassify(getCookie(),getSid());
    }

    private void initSpinner() {
        for (UploadClassifyBean.DataBean classify : classifys) {
            items.add(classify.getClass_name());
        }
        mSpinner.setItems(items);
        mSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectPosition = position + 1;
            }
        });
    }

    private void initView() {
        mSpinner = (MaterialSpinner) findViewById(R.id.spinner_videoList);
        iv_cover = (ImageView) findViewById(R.id.iv_cover_upVideo);
        avi_upload = (AVLoadingIndicatorView) findViewById(R.id.avi_upvideo);
        et_title = (EditText) findViewById(R.id.et_video);
        et_videoContent = (EditText) findViewById(R.id.et_videoContent);
        iv_back = (ImageView) findViewById(R.id.iv_upload_back);
        iv_play = (ImageView) findViewById(R.id.iv_start);
        fl_myVideo = (FrameLayout) findViewById(R.id.fl_myVideo);
        bt_addvideo = (Button) findViewById(R.id.bt_upvideo_addvideo);
        bt_videolist = (Button) findViewById(R.id.bt_upvideo_videolist);
        bt_upload = (Button) findViewById(R.id.bt_upvideo_upload);
        selectVideoPopupWindow = new SelectVideoPopupWindow(this);
        selectVideoPopupWindow.setOnSelectedListener(this);
        mVideoView = (FllScreenVideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mediaController.setVisibility(View.INVISIBLE);
        mVideoView.setMediaController(mediaController);
        mVideoView.requestFocus();
        //设置正文空格
        et_videoContent.setText("\t\t");
        et_videoContent.setSelection(et_videoContent.getText().length());
    }

    private void initEvent() {
        bt_addvideo.setOnClickListener(this);
        bt_videolist.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
        iv_play.setOnClickListener(this);//点击开始播放
        iv_back.setOnClickListener(this);
        //设置点击空白处消失popwindow
        selectVideoPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    selectVideoPopupWindow.dismissPopupWindow();
                    return true;
                }
                return false;
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "视频播放结束", Toast.LENGTH_LONG).show();
                iv_play.setVisibility(View.VISIBLE);
//                iv_cover.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_upvideo_addvideo: //添加视频
                selectVideoPopupWindow.showPopupWindow(UpVideoActivity.this);
                break;
            case R.id.bt_upvideo_videolist://视频列表
                GoVideoListFragment();
                break;
            case R.id.bt_upvideo_upload://上传视频
                avi_upload.setVisibility(View.VISIBLE);
                avi_upload.show();
                uploaduserVideo(path);
                break;
            case R.id.iv_start:   //播放视频
                mVideoView.start();
                iv_cover.setVisibility(View.INVISIBLE);//隐藏第一帧图
                iv_play.setVisibility(View.GONE);
                break;
            case R.id.iv_upload_back:     //点击返回
                finish();
                break;
        }
    }

    private void GoVideoListFragment() {
        int myVideoVisibility = fl_myVideo.getVisibility();
        if (!TextUtils.isEmpty(et_title.getText().toString().trim()) && myVideoVisibility == View.VISIBLE) {
            showifsaveDialog();
        } else {
            GoVideoFragment();
        }
    }

    private void GoVideoFragment() {
        Intent intent = new Intent(UpVideoActivity.this, MainActivity.class);
        intent.putExtra(ArtConstant.SHOW_ARTICLEFRAGMENT,1);
        startActivity(intent);
        finish();
    }

    private void showifsaveDialog() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("您还没有上传哦，确定退出吗？")
                    .setMessage("退出后数据将不会保存")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing or what you want
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create()
                    .show();
    }

    private void uploaduserVideo(String path) {
        mVideoView.pause();
        //获取视频第一帧图片
        Bitmap bitmap = CreateBitmap.getLocalVideoThumbnail(path);
        if (bitmap != null) {
            bytesFromBitmap = getBytesFromBitmap(bitmap);
        }
        //获取Title
        String title = et_title.getText().toString();
        String content = et_videoContent.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && bitmap!=null && !TextUtils.isEmpty(content)) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), new File(path));
            // MultipartBody.Part  和后端约定好Key，这里的partName是用image
            MultipartBody.Part body = MultipartBody.Part.createFormData("video", new File(path).getName(), requestFile);
            // 执行请求
            RequestBody body1 = RequestBody.create(MediaType.parse("multipart/form-data"),getSid());
            RequestBody body2 = RequestBody.create(MediaType.parse("multipart/form-data"),getCookie());
            RequestBody body3 = RequestBody.create(MediaType.parse("multipart/form-data"),title);
            RequestBody body4 = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(selectPosition));
            RequestBody body5 = RequestBody.create(MediaType.parse("multipart/form-data"),content);
            RequestBody body6 = RequestBody.create(MediaType.parse("multipart/form-data"),bytesFromBitmap);
            Map<String,RequestBody> map = new HashMap<>();
            map.put("sid",body1);
            map.put("cookie",body2);
            map.put("title",body3);
            map.put("class_id",body4);
            map.put("content",body5);
            map.put("img",body6);
            ApiManager.getInstance().getArtApiService()
                    .uploadVideo(map,body)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UpLoadArtBean>() {
                        @Override
                        public void onCompleted() {
                            avi_upload.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            avi_upload.setVisibility(View.GONE);
                            Log.i("FiDo", "onNext: onError"+e.getMessage());
                        }

                        @Override
                        public void onNext(UpLoadArtBean upLoadArtBean) {
                            avi_upload.hide();
                            if (upLoadArtBean.getCode().equals("1")) {
                                Toast.makeText(UpVideoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                GoVideoFragment();
                            }else{
                                Toast.makeText(UpVideoActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            if (path != null && fl_myVideo.getVisibility() == View.VISIBLE) {
                Toast.makeText(UpVideoActivity.this,"别忘了给视频加上标题与内容哦~",Toast.LENGTH_SHORT).show();
                avi_upload.hide();
            }else{
                Toast.makeText(UpVideoActivity.this,"别忘了添加您的视频哦~",Toast.LENGTH_SHORT).show();
                avi_upload.hide();
            }
        }
    }

    @Override
    public void OnSelected(View v, int position) {
        switch (position) {
            case 0:
                // "录制"按钮被点击了
                recording();
                selectVideoPopupWindow.dismissPopupWindow();
                break;
            case 1:
                // "从相册选择"按钮被点击了
                selectorFromGallery();
                selectVideoPopupWindow.dismissPopupWindow();
                break;
            case 2:
                // "取消"按钮被点击了
                selectVideoPopupWindow.dismissPopupWindow();
                break;
        }
    }

    private void selectorFromGallery() {
        PermissionGen.needPermission(this,VIDEO_KU,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    private void recording() {
        PermissionGen.with(this)
                .addRequestCode(0)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .request();
    }

    @PermissionSuccess(requestCode = VIDEO_KU)
    private void selectFromGallery(){
        // TODO 启动相册
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, VIDEO_KU);
    }

    @PermissionSuccess(requestCode = 0)
    private void recordVideo(){
        // 激活系统的照相机进行录像
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");

        // 保存录像到指定的路径
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/video.mp4");
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK){
                    uri = data.getData();
                    path = UriAllUriUtils.getPath(this, uri);
                    if (uri != null) {
                        Bitmap bitmap = CreateBitmap.getLocalVideoThumbnail(path);
                        iv_cover.setImageBitmap(bitmap);
                        fl_myVideo.setVisibility(View.VISIBLE);
                    }else{
                        fl_myVideo.setVisibility(View.INVISIBLE);
                    }
                    mVideoView.setVideoURI(uri);
                }
                break;
            case VIDEO_KU:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        uri = data.getData();
                        if (uri != null) {
                            fl_myVideo.setVisibility(View.VISIBLE);
                        } else {
                            fl_myVideo.setVisibility(View.INVISIBLE);
                        }
                        path = UriAllUriUtils.getPath(this, uri);
                        Log.i("FiDo", "onActivityResult: Gallerypath" + path);
                        mVideoView.setVideoURI(uri);
                    }catch (Exception e) {
                        String  a=e+"";
                    } catch (OutOfMemoryError e) {
                        String  a=e+"";
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public String getBytesFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,baos);
        byte[] bytes = baos.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        return imgString;
    }

    public static String getCookie(){
        return (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
    }

    public static String getSid(){
        return (String) SPUtil.get(Constant.Share_prf.SID, "");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void setClassfiy(UploadClassifyBean classifyBean) {
        if (classifyBean != null) {
            classifys.addAll(classifyBean.getData());
        }
        initSpinner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPresenterImp.unsubcrible();
        if (mVideoView != null) {
            mVideoView = null;
        }
    }
}
