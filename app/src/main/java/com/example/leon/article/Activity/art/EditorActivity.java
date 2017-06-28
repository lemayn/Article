package com.example.leon.article.Activity.art;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leon.article.Activity.MainActivity;
import com.example.leon.article.R;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class EditorActivity extends AppCompatActivity implements IEditorActivity,SelectPicturePopupWindow.OnSelectedListener{

    private EditText mEditor;
    private EditText artTitle;
    //输入的内容
    private TextView tv_clear;

    private ArtPresenterImp artPresenter;
    private Dialog dialog;
    private Bitmap insertBitmap;

    //用户添加的图片与图片地址
    private ImageView iv_insert;
    private String imgpath;
    private String cookie;
    private String sid;
    private SelectPicturePopupWindow picturePopupWindow;
    private PhotoSelectUtils photoSelectUtils;
    private ImageView iv_back;
    //获取上传类型集合
    private List<UploadClassifyBean.DataBean> classifys= new ArrayList<>();
    private MaterialSpinner spinner;
    private int selectPosition = 1;
    private boolean isDelet = false;
    //存放Classify的集合
    private List<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //获取用户输入，cookie，sid
        cookie = (String) SPUtil.get(Constant.Share_prf.COOKIE,"");
        sid = (String) SPUtil.get(Constant.Share_prf.SID,"");
        initView();

        initPic();
    }

    private void initPic() {
        photoSelectUtils = new PhotoSelectUtils(this, new PhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                if (outputUri != null) {
                    imgpath = outputFile.getAbsolutePath();
                    Glide.with(EditorActivity.this)
                            .load(outputUri)
                            .centerCrop()
                            .transform(new CornersTransform(EditorActivity.this))
                            .crossFade()
                            .into(iv_insert);
                    iv_insert.setVisibility(View.VISIBLE);

                    //压缩图片
                    ImageCompress imageCompress = new ImageCompress();
                    ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                    options.uri = Uri.fromFile(new File(imgpath));
                    options.maxHeight = 800;
                    options.maxWidth = 400;
                    insertBitmap = imageCompress.compressFromUri(EditorActivity.this, options);
                    /*//设置图片缩放比例
                    ImageResizeUtil.resizeImage(insertBitmap,500,500);*/
                }
            }
        },true);//图像裁剪功能 false不开启，true开启
    }

    private void GetDate() {
        artPresenter = new ArtPresenterImp(this,this);
        //获取上传类型
        artPresenter.getUploadClassify(cookie,sid);
        mEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tv_clear.setVisibility(View.VISIBLE);
                    clearEditor();
                }else{
                    tv_clear.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void clearEditor() {
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setText("");
            }
        });
    }

    private void initView() {
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        iv_insert = (ImageView) findViewById(R.id.iv_editor_insert);
        iv_back = (ImageView) findViewById(R.id.iv_editor_back);
        dialog = SpinnerDialog.createSpinnerDialog(EditorActivity.this, "文章上传中...");
        //图片选择的popWindown
        picturePopupWindow = new SelectPicturePopupWindow(this);
        picturePopupWindow.setOnSelectedListener(this);
        //上传状态选择器
        spinner = (MaterialSpinner) findViewById(R.id.spinner_editor);
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
        artTitle = (EditText) findViewById(R.id.et_title);
        mEditor = (EditText) findViewById(R.id.et_eritor);
        mEditor.setText("\t\t");
        mEditor.setSelection(mEditor.getText().length());
    }

    private void initEvent() {
        //设置点击空白处消失popwindow
        picturePopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    picturePopupWindow.dismissPopupWindow();
                    return true;
                }
                return false;
            }
        });

        //点击发布后,做提交处理
        findViewById(R.id.bt_editor_send).setOnClickListener(new View.OnClickListener() {
            private String bytesFromBitmap;
            @Override
            public void onClick(View v) {
                //隐藏软键盘
                hideKeyboard();
                String title = artTitle.getText().toString();
                String mEditorDate = mEditor.getText().toString();
                if (insertBitmap != null) {
                    bytesFromBitmap = getBytesFromBitmap(insertBitmap);
                }
                else{
                    /*if (!TextUtils.isEmpty(imgpath)) {
                        //压缩图片
                        ImageCompress imageCompress = new ImageCompress();
                        ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                        options.uri = Uri.fromFile(new File(imgpath));
                        options.maxHeight = 960;
                        options.maxWidth = 540;
                        insertBitmap = imageCompress.compressFromUri(EditorActivity.this, options);
                        bytesFromBitmap = getBytesFromBitmap(insertBitmap);
                    }*/
                    if (isDelet) {
                        insertBitmap = null;
                    }
                    /*else{//不上传默认图片
                        insertBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rec_bg);
                        bytesFromBitmap = getBytesFromBitmap(insertBitmap);
                    }*/
                }
                artPresenter.uploadUserArt(cookie,title,mEditorDate,sid,bytesFromBitmap,String.valueOf(selectPosition));
            }
        });

        iv_insert.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showifDelDialog();
                return true;
            }
        });

        //点击返回按钮
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //点击添加图片
        findViewById(R.id.bt_editor_addImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturePopupWindow.showPopupWindow(EditorActivity.this);
            }
        });

        //点击新闻列表
        findViewById(R.id.bt_editor_newslist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEditor.getText().toString().trim()) && TextUtils.isEmpty(artTitle.getText().toString())) {
                    startActivity(new Intent(EditorActivity.this, MainActivity.class));
                }else{
                    showifSaveDialog();
                }
            }
        });
    }

    private void showifDelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示：")
                .setMessage("是否删除该图片")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (iv_insert!=null && iv_insert.getVisibility() == View.VISIBLE){
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

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(mEditor.getText().toString().trim()) && TextUtils.isEmpty(artTitle.getText().toString())) {//如果输入的内容为空
            super.onBackPressed();
        } else {//用户输入的内容不为空
            showifSaveDialog();
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

    //隐藏虚拟键盘
    private void hideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
    public void showSuccess() {
        Toast.makeText(this,getString(R.string.upload_success),Toast.LENGTH_SHORT).show();
        goArticleFragment();
    }

    private void goArticleFragment() {
        Intent intent = new Intent(EditorActivity.this, MainActivity.class);
        intent.putExtra(ArtConstant.SHOW_ARTICLEFRAGMENT,1);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError() {
        Toast.makeText(this,getString(R.string.retry_letter),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailure() {
        Toast.makeText(this,getString(R.string.retry_letter),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUploadClassfiy(UploadClassifyBean classifyBean) {
        if (classifyBean != null) {
            classifys.addAll(classifyBean.getData());
        }
        //类型选择框
        initSpinner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        photoSelectUtils.attachToActivityForResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }


    public String getBytesFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,baos);
        byte[] bytes = baos.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes,Base64.DEFAULT));
        return imgString;
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
//        pickImgfromPhoto();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1(){
        showRequestDialog();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2(){
        showRequestDialog();
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
                        intent.setData(Uri.parse("package:" + EditorActivity.this.getPackageName()));
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
