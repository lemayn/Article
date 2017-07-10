package com.example.leon.article.Activity.art;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leon.article.Activity.art.util.ImageUtils;
import com.example.leon.article.Activity.art.util.ScreenUtils;
import com.example.leon.article.R;
import com.example.leon.article.api.ApiManager;
import com.example.leon.article.utils.PhotoSelectUtils;
import com.example.leon.article.widget.SelectPicturePopupWindow;
import com.example.leon.article.widget.SpinnerDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.SDCardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArtEditorActivity extends AppCompatActivity implements SelectPicturePopupWindow.OnSelectedListener, View.OnClickListener {

    private EditText artTitle;
    private TextView tv_clear,tv_insertImg;
    private Button bt_send;
    private ImageView iv_back,iv_insert;
    private Dialog dialog;
    private ProgressDialog insertDialog;
    private SelectPicturePopupWindow picturePopupWindow;
    private PhotoSelectUtils photoSelectUtils;
    private MaterialSpinner spinner;
    private RichTextEditor mEditor;
    private static final int REQUEST_CODE_SELECTFROM_Gallery = 666666;
    //存放图片地址的集合
    private List<String> imageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_editor);

        initView();

    }

    private void requestPermission() {
        PermissionGen.needPermission(this,REQUEST_CODE_SELECTFROM_Gallery,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    private void initView() {
        tv_clear = (TextView) findViewById(R.id.tv_artEditor_clear);
        iv_insert = (ImageView) findViewById(R.id.iv_artEditor_insert); //底部插入的图片
        bt_send = (Button) findViewById(R.id.bt_artEditor_send);
        iv_back = (ImageView) findViewById(R.id.iv_artEditor_back);
        tv_insertImg = (TextView) findViewById(R.id.tv_artEditor_insertImg);
        dialog = SpinnerDialog.createSpinnerDialog(this, "文章上传中...");
        //图片选择的popWindown
        picturePopupWindow = new SelectPicturePopupWindow(this);
        picturePopupWindow.setOnSelectedListener(this);
        //上传状态选择器
        spinner = (MaterialSpinner) findViewById(R.id.spinner_editor);

        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);
        initEditor();
        GetDate();
        initEvent();
    }

    private void initEditor() {
        artTitle = (EditText) findViewById(R.id.et_title);
        mEditor = (RichTextEditor) findViewById(R.id.et_artEditor_content);

    }
    private void GetDate() {

    }
    private void initEvent() {
        iv_back.setOnClickListener(this);
        tv_insertImg.setOnClickListener(this);
        bt_send.setOnClickListener(this);
    }

    @PermissionSuccess(requestCode = REQUEST_CODE_SELECTFROM_Gallery)
    private void selectFrorPic(){
        Log.i("FiDo", "selectFrorPic: 执行了！！！！");
        callGallery();
    }

    private void callGallery() {
        Log.i("FiDo", "callGallery: 执行了！！！！");
        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(5)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    private void insertImagesSync(final Intent data) {
        insertDialog.show();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    mEditor.measure(0,0);
                    int screenWidth = ScreenUtils.getScreenWidth(ArtEditorActivity.this);
                    int screenHeight = ScreenUtils.getScreenHeight(ArtEditorActivity.this);
                    //插入的图片地址集合
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //同时插入多张图片
                    for (String imgPath : photos) {
                        //将图片保存到SD卡，如不需要可以不用
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imgPath, screenWidth, screenHeight);
                        imgPath = SDCardUtil.saveToSdCard(bitmap);
                        subscriber.onNext(imgPath);
                    }
                    subscriber.onCompleted();
                }catch (Exception e){
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
                        mEditor.addEditTextAtIndex(mEditor.getLastIndex()," ");
                        showToast("插入图片成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        insertDialog.dismiss();
                        showToast("插如图片失败："+e.getMessage());
                    }

                    @Override
                    public void onNext(String imgPath) {
                        mEditor.insertImage(imgPath,mEditor.getMeasuredWidth());
                        Log.i("FiDo", "onNext: imagePath:"+imgPath);
                    }
                });
    }

    //隐藏虚拟键盘
    private void hideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** 显示吐司 **/
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
    private void takephoto(){
        photoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectphoto(){
        photoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1(){
        showRequestDialog();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2(){
        showRequestDialog();
    }

    /**
     * 负责处理编辑数据提交等事宜
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editList = mEditor.buildEditData();
        StringBuilder content = new StringBuilder();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
                //Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
                //Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
                imageList.add(itemData.imagePath);
            }
        }
        Log.i("FiDo", "imageList: "+imageList);
        Log.i("FiDo", "getEditData: "+content.toString());
        return content.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_artEditor_back:
                onBackPressed();
                break;
            case R.id.tv_artEditor_insertImg: //点击了插入图片
                requestPermission();
                break;
            case R.id.bt_artEditor_addImg://点击了添加底部图片

                break;
            case R.id.bt_artEditor_newslist://点击了新闻列表

                break;
            case R.id.bt_artEditor_send://点击了上传
                hideKeyboard();
                getEditData();
                Log.i("FiDo", "点击了上传: "+imageList);
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(imageList.get(0)));
                RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), new File(imageList.get(1)));
                Map<String,RequestBody> map = new HashMap<>();
                map.put("imgs",requestFile);
                map.put("imgs",requestFile2);
                ApiManager.getInstance().getArtApiService()
                        .upLoadMuiltImgs(map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("FiDo", "onError: "+e.getMessage());
                            }

                            @Override
                            public void onNext(String s) {
                                Log.i("FiDo", "onNext: "+s);
                            }
                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == RESULT_OK) {
            if (data != null) {
                // 在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
//                photoSelectUtils.attachToActivityForResult(requestCode,resultCode,data);
                if (requestCode == 1){
                    //处理调用系统图库
                } else if (requestCode == PhotoPicker.REQUEST_CODE){
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }*/
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1){
                    //处理调用系统图库
                } else if (requestCode == PhotoPicker.REQUEST_CODE){
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
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
                        intent.setData(Uri.parse("package:" + ArtEditorActivity.this.getPackageName()));
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

}
