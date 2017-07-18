package com.example.leon.article.Activity.art;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leon.article.Activity.MainActivity;
import com.example.leon.article.Activity.art.util.ImageUtils;
import com.example.leon.article.Activity.art.util.SDCardUtil;
import com.example.leon.article.R;
import com.example.leon.article.api.ApiManager;
import com.example.leon.article.api.bean.ImgTagBean;
import com.example.leon.article.api.bean.UploadClassifyBean;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.ArtPresenterImp;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.CornersTransform;
import com.example.leon.article.utils.ImageCompress;
import com.example.leon.article.utils.PhotoSelectUtils;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IEditorActivity;
import com.example.leon.article.widget.SelectPicturePopupWindow;
import com.example.leon.article.widget.SpinnerDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sendtion.xrichtext.RichTextEditor;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArtEditorActivity03 extends AppCompatActivity implements IEditorActivity, SelectPicturePopupWindow.OnSelectedListener, View.OnClickListener {

    private EditText artTitle;
    private TextView tv_insertImg;
    private Button bt_send, bt_addImg,bt_artList;
    private ImageView iv_back, iv_insert;
    private Dialog dialog;
    private ProgressDialog insertDialog;
    private SelectPicturePopupWindow picturePopupWindow;
    private PhotoSelectUtils photoSelectUtils;
    private MaterialSpinner spinner;
    private RichTextEditor mEditor;
    private static final int REQUEST_CODE_INSERT_IMAGE = 666666;
    private static final int REQUEST_CODE_CHOOSE = 2323;
    private ArtPresenterImp artPresenter;
    //获取上传类型集合
    private List<UploadClassifyBean.DataBean> classifys = new ArrayList<>();
    //存放Classify的集合
    private List<String> items = new ArrayList<>();
    //选择的类型位置
    private int selectPosition = 1;
    //获取到的img标签
    private List<String> imgTags = new ArrayList<>();
    //记录插入图片的下标
    private List<Integer> indexs = new ArrayList<>();
    //存放图片地址的集合
    private List<String> imageList = new ArrayList<>();
    //存放位置于服务器地址的Map
    private Map<Integer, String> tags = new HashMap<>();

    private String imgpath;
    private Bitmap insertBitmap;
    private boolean isDelet = false;
    private String bytesFromBitmap;

    public static String getCookie() {
        return (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
    }

    public static String getSid() {
        return (String) SPUtil.get(Constant.Share_prf.SID, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_editor);

        initView();

        initPic();
    }

    private void initPic() {
        photoSelectUtils = new PhotoSelectUtils(this, new PhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                if (outputUri != null) {
                    imgpath = outputFile.getAbsolutePath();
                    Glide.with(ArtEditorActivity03.this)
                            .load(outputUri)
                            .centerCrop()
                            .transform(new CornersTransform(ArtEditorActivity03.this))
                            .crossFade()
                            .into(iv_insert);
                    iv_insert.setVisibility(View.VISIBLE);

                    //压缩图片
                    ImageCompress imageCompress = new ImageCompress();
                    ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                    options.uri = Uri.fromFile(new File(imgpath));
                    options.maxHeight = 800;
                    options.maxWidth = 480;
                    insertBitmap = imageCompress.compressFromUri(ArtEditorActivity03.this, options);
                }
            }
        }, true);//图像裁剪功能 false不开启，true开启
    }

    private void requestPermission() {
        PermissionGen.needPermission(this, REQUEST_CODE_INSERT_IMAGE,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA});
    }

    private void initView() {
        iv_insert = (ImageView) findViewById(R.id.iv_artEditor_insert); //底部插入的图片
        bt_send = (Button) findViewById(R.id.bt_artEditor_send);
        bt_addImg = (Button) findViewById(R.id.bt_artEditor_addImg);
        bt_artList = (Button)findViewById(R.id.bt_artEditor_newslist);
        iv_back = (ImageView) findViewById(R.id.iv_artEditor_back);
        artTitle = (EditText) findViewById(R.id.et_artEditor_title);
        tv_insertImg = (TextView) findViewById(R.id.tv_artEditor_insertImg);
        dialog = SpinnerDialog.createSpinnerDialog(this, "文章上传中...");
        //图片选择的popWindown
        picturePopupWindow = new SelectPicturePopupWindow(this);
        picturePopupWindow.setOnSelectedListener(this);
        //上传状态选择器
        spinner = (MaterialSpinner) findViewById(R.id.spinner_artEditor);

        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);
        initEditor();
        GetDate();
        initEvent();
    }

    private void initSpinner() {
        for (UploadClassifyBean.DataBean classify : classifys) {
            items.add(classify.getClass_name());
        }
        spinner.setItems(items);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                selectPosition = position + 1;
            }
        });
    }

    private void initEditor() {
        mEditor = (RichTextEditor) findViewById(R.id.et_artEditor_content);
    }

    private void GetDate() {
        artPresenter = new ArtPresenterImp(this, this);
        //获取上传类型
        artPresenter.getUploadClassify(getCookie(), getSid());
    }

    private void initEvent() {
        iv_back.setOnClickListener(this);
        tv_insertImg.setOnClickListener(this);
        bt_send.setOnClickListener(this);
        bt_addImg.setOnClickListener(this);
        bt_artList.setOnClickListener(this);
        iv_insert.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showifDelDialog();
                return true;
            }
        });
    }

    @PermissionSuccess(requestCode = REQUEST_CODE_INSERT_IMAGE)
    private void selectFrorPic() {
        insertImage();
    }

    private void insertImage() {
        //图片选择
        Matisse.from(ArtEditorActivity03.this)
                .choose(MimeType.ofImage()) // 选择 mime 的类型
                .countable(true)
                .maxSelectable(5) // 图片选择的最多数量
                .capture(true)
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .captureStrategy(new CaptureStrategy(true,"com.zhihu.matisse.fileprovider"))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .theme(R.style.Matisse_Dracula)
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }

    private void insertImagesSync(final Intent data) {
        insertDialog.show();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    mEditor.measure(0, 0);
                    //插入的图片地址集合
                    List<String> photos = Matisse.obtainPathResult(data);
                    //同时插入多张图片
                    for (String imgPath : photos) {
                        //将图片进行压缩并保存到SD卡,为了之后拿到压缩后的路径
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imgPath, 480, 800);//压缩图片
                        imgPath = SDCardUtil.saveToSdCard(bitmap);
                        subscriber.onNext(imgPath);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        insertDialog.dismiss();
                        mEditor.addEditTextAtIndex(mEditor.getLastIndex(), " ");
                        showToast("插入图片成功");
                        //插入结束后自动上传
                        upLoadMuiltImg();
                    }

                    @Override
                    public void onError(Throwable e) {
                        insertDialog.dismiss();
                        Log.i("FiDo", "插入图片失败  onError: "+e.getMessage());
                        showToast("请选择正确的图片插入哦");
                    }

                    @Override
                    public void onNext(String imgPath) {
                        mEditor.insertImage(imgPath, mEditor.getMeasuredWidth());
                        Log.i("FiDo", "onNext: imagePath:" + imgPath);
                    }
                });
    }

    //隐藏虚拟键盘
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示吐司
     **/
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnSelected(View v, int position) {
        switch (position) {
            case 0:
                // "拍照"按钮被点击了
                takePhoto();
                picturePopupWindow.dismissPopupWindow();
                break;
            case 1:
                // "从相册选择"按钮被点击了
                pickFromGallery();
                picturePopupWindow.dismissPopupWindow();
                break;
            case 2:
                // "取消"按钮被点击了
                picturePopupWindow.dismissPopupWindow();
                break;
        }
    }

    private void pickFromGallery() {
        //从图库选取
        PermissionGen.needPermission(this,
                PhotoSelectUtils.REQ_SELECT_PHOTO,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    private void takePhoto() {
        //调用拍照方法
        PermissionGen.with(this)
                .addRequestCode(PhotoSelectUtils.REQ_TAKE_PHOTO)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .request();
    }

    @PermissionSuccess(requestCode = PhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takephoto() {
        photoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectphoto() {
        photoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        showRequestDialog();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        showRequestDialog();
    }

    /**
     * 负责处理编辑数据提交等事宜
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editList = mEditor.buildEditData();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < editList.size(); i++) {
            if (editList.get(i).inputStr != null) {
                content.append(editList.get(i).inputStr);
            } else if (editList.get(i).imagePath != null) {
                content.append("<img src=\"").append(editList.get(i).imagePath).append("\"/>");
                imageList.add(editList.get(i).imagePath);
                indexs.add(i);
            }
        }
        Log.i("FiDo", "imageList: " + imageList);
        Log.i("FiDo", "插如图片位置的集合为：: " + indexs);
        Log.i("FiDo", "getEditData: " + content.toString());
        return content.toString();
    }

    private String getEditDataWithTag() {
        List<RichTextEditor.EditData> editDatas = mEditor.buildEditData();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < editDatas.size(); i++) {
            if (editDatas.get(i).inputStr != null) {
                content.append(editDatas.get(i).inputStr);
            } else if (editDatas.get(i).imagePath != null) {
                content.append("<img src=\"").append(tags.get(i)).append("\"/>");
                //html中添加上传的图片宽高为300，可调整
//                content.append("<img src=\"").append(tags.get(i)).append("\"").append("width=\"300\" height=\"300\"").append("/>");
                imageList.add(editDatas.get(i).imagePath);
            }
        }
        Log.i("FiDo", "getEditDataWithTag  imageList: " + imageList);
        Log.i("FiDo", "getEditDataWithTag: " + content.toString());
        return content.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_artEditor_back:
                onBackPressed();
                break;
            case R.id.tv_artEditor_insertImg://点击了插入图片
                requestPermission();
                break;
            case R.id.bt_artEditor_addImg://点击了添加底部图片
                picturePopupWindow.showPopupWindow(ArtEditorActivity03.this);
                break;
            case R.id.bt_artEditor_newslist://点击了新闻列表
                if (TextUtils.isEmpty(getEditData()) && TextUtils.isEmpty(artTitle.getText().toString())) {
                    goArticleFragment();
                }else{
                    showifSaveDialog();
                }
                break;
            case R.id.bt_artEditor_send://点击了上传
                upLoadArt();
                break;
        }
    }

    private void upLoadArt() {
        hideKeyboard();
        String title = artTitle.getText().toString();//获取到的文章标题
        String dataWithTag = getEditDataWithTag(); //获取到的文章内容
        if (insertBitmap != null) {
            bytesFromBitmap = getBytesFromBitmap(insertBitmap);
        } else {
            if (isDelet) {
                insertBitmap = null;
            }
        }
        artPresenter.uploadUserArt(getCookie(), title, dataWithTag, getSid(), bytesFromBitmap, String.valueOf(selectPosition));
    }

    private void showifDelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示：")
                .setMessage("是否删除该图片")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (iv_insert != null && iv_insert.getVisibility() == View.VISIBLE) {
                            iv_insert.setVisibility(View.GONE);
                            imgpath = "";
                            insertBitmap = null;
                            isDelet = true;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing or what you want
                    }
                })
                .create().show();
    }

    private void upLoadMuiltImg() {
        hideKeyboard();
        getEditData();
        RequestBody body1 = RequestBody.create(MediaType.parse("multipart/form-data"), getSid());
        RequestBody body2 = RequestBody.create(MediaType.parse("multipart/form-data"), getCookie());
        Map<String, RequestBody> map = new HashMap<>();
        map.put("sid", body1);
        map.put("cookie", body2);
        Log.i("FiDo", "upLoadMuiltImg，上传时的图片集合为："+imageList);
        List<MultipartBody.Part> list = new ArrayList<>();
        if (imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                File file = new File(imageList.get(i));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                list.add(MultipartBody.Part.createFormData("imgs[]", new File(imageList.get(i)).getName(), requestBody));
            }
        }
        ApiManager.getInstance().getArtApiService()
                .upLoadMuiltImgs(map, list)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImgTagBean>() {
                    @Override
                    public void onCompleted() {
                        if (imageList.size() > 0) {
                            for (String imgPath : imageList) {
                                //上传结束后删除压缩后图片
                                SDCardUtil.deleteFile(imgPath);
                            }
                        }
                        imageList.clear();
                        indexs.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        Log.i("FiDo", "upLoadMuiltImg  onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ImgTagBean bean) {
                        if (bean.getCode().equals("1")) {
                            imgTags = bean.getData();
                            for (int i = 0; i < imgTags.size(); i++) {
                                tags.put(indexs.get(i), imgTags.get(i));
                            }
                            Log.i("FiDo", "upLoadMuiltImg 返回数据为: "+bean.toString());
                           /* imageList.clear();
                            indexs.clear();*/
                        }else if(bean.getCode().equals("3")){
                            showToast("图片格式错误");
                        }else{
                            showToast(getString(R.string.retry_letter));
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE) {
            //异步方式插入图片
            insertImagesSync(data);
        }
        // 在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        photoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void showifSaveDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您还没有发布哦，确定退出吗？")
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

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(getEditData()) && TextUtils.isEmpty(artTitle.getText().toString())) {//如果输入的内容为空
            super.onBackPressed();
        } else {//用户输入的内容不为空
            showifSaveDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        artPresenter.unsubcrible();
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    private void showRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("申请权限")
                .setMessage("在设置-应用-微创秀文-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能")
                //添加确定按钮点击事件
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //这里用来跳到手机设置页，方便用户开启权限
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + ArtEditorActivity03.this.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                //取消按钮点击事件
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing or what you want
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showProgress() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showError() {
        Toast.makeText(this, getString(R.string.retry_letter), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess() {
        Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
        goArticleFragment();
    }

    @Override
    public void showFailure() {
        Toast.makeText(this, getString(R.string.retry_letter), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUploadClassfiy(UploadClassifyBean classifyBean) {
        classifys.addAll(classifyBean.getData());
        //类型选择框
        initSpinner();
    }

    private void goArticleFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ArtConstant.SHOW_ARTICLEFRAGMENT, 1);
        startActivity(intent);
        finish();
    }

    public String getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] bytes = baos.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        return imgString;
    }

}
