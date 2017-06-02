package com.example.leon.article.Activity.art;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import com.example.leon.article.presenter.artpresenter.artpresenterImp.ArtPresenterImp;
import com.example.leon.article.sql.bean.Arts;
import com.example.leon.article.sql.dao.ArtDao;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.ImageCompress;
import com.example.leon.article.utils.PhotoSelectUtils;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.utils.TimeUtils;
import com.example.leon.article.view.IEditorActivity;
import com.example.leon.article.widget.SelectPicturePopupWindow;
import com.example.leon.article.widget.SpinnerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;

import jp.wasabeef.richeditor.RichEditor;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class EditorActivity extends AppCompatActivity implements IEditorActivity,SelectPicturePopupWindow.OnSelectedListener{

    //MySQL传递过来的数据
    private String art_title;
    private String art_content;
    private String art_imgPath;

    private RichEditor mEditor;
    private EditText artTitle;
    //输入的内容
    private String editDate;
    private TextView tv_clear;
    //记录是否点击了保存
    private boolean isSave = false;

    Arts arts = new Arts();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initView();

        //获取数据库数据
        initDate();

        //获取用户输入，cookie，sid
        GetDate();

        initPic();
    }

    private void initPic() {
        photoSelectUtils = new PhotoSelectUtils(this, new PhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                if (outputUri != null) {
                    imgpath = outputFile.getAbsolutePath();
//                    insertBitmap = BitmapFactory.decodeFile(outputFile.getAbsolutePath());
                    iv_insert.setVisibility(View.VISIBLE);
                    Glide.with(EditorActivity.this)
                            .load(outputUri)
                            .into(iv_insert);

                    //压缩图片
                    ImageCompress imageCompress = new ImageCompress();
                    ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                    options.uri = Uri.fromFile(outputFile);
                    options.maxHeight = 250;
                    options.maxWidth = 250;
                    insertBitmap = imageCompress.compressFromUri(EditorActivity.this, options);
                }
            }
        },false);
    }

    private void initDate() {
        artPresenter = new ArtPresenterImp(this,this);

        art_title = getIntent().getStringExtra(ArtConstant.ART_TITLE);
        art_content = getIntent().getStringExtra(ArtConstant.ART_CONTENT);
        art_imgPath = getIntent().getStringExtra(ArtConstant.ART_IMGPATH);
        if (art_title != null) {
            artTitle.setText(art_title);
        }
        if (art_content != null) {
            mEditor.setHtml(art_content);
        }
        if (art_imgPath != null) {
            Log.i("HT", "数据库的 art_imgPath"+art_imgPath);
            iv_insert.setVisibility(View.VISIBLE);
//            iv_insert.setImageBitmap(BitmapFactory.decodeFile(art_imgPath));
            //使用Glide加载图片
            Glide.with(this).load(art_imgPath).into(iv_insert);
            if (imgpath != null) {
                art_imgPath = imgpath;
                Glide.with(this)
                        .load(art_imgPath)
                        .centerCrop()
                        .into(iv_insert);
            }
        }
    }

    private void GetDate() {
        cookie = (String) SPUtil.get(Constant.Share_prf.COOKIE,"");
        sid = (String) SPUtil.get(Constant.Share_prf.SID,"");
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                editDate = text;
                if (!TextUtils.isEmpty(text)) {
                    tv_clear.setVisibility(View.VISIBLE);
                    clearEditor();
                }else{
                    tv_clear.setVisibility(View.GONE);
                }
            }
        });
    }

    private void clearEditor() {
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHtml("");
            }
        });
    }

    private void initView() {
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        iv_insert = (ImageView) findViewById(R.id.iv_editor_insert);
        dialog = SpinnerDialog.createSpinnerDialog(EditorActivity.this, "文章上传中...");
        //图片选择的popWindown
        picturePopupWindow = new SelectPicturePopupWindow(this);
        picturePopupWindow.setOnSelectedListener(this);

        initEditor();
        initEvent();
    }

    private void initEditor() {
        artTitle = (EditText) findViewById(R.id.et_title);
        mEditor = (RichEditor) findViewById(R.id.richEditor);
        mEditor.setClickable(true);
        mEditor.setEditorHeight(100);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("请输入内容...");
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
                if (insertBitmap != null) {
                    bytesFromBitmap = getBytesFromBitmap(insertBitmap);
                }
                artPresenter.uploadUserArt(cookie,title,editDate,sid,bytesFromBitmap);
            }
        });

        //点击保存后
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave = true;
                String title = artTitle.getText().toString().trim();
                String editorContent = mEditor.getHtml();
                if (TextUtils.isEmpty(title)){
                    Toast.makeText(EditorActivity.this,getString(R.string.titlenotnull),Toast.LENGTH_SHORT).show();
                    artTitle.requestFocus();
                }else {
                    if (TextUtils.isEmpty(editorContent)) {
                        Toast.makeText(EditorActivity.this,getString(R.string.contentnotbull),Toast.LENGTH_SHORT).show();
                    } else {    //不为空存入数据库
                        arts.setContent(editorContent);
                        arts.setTitle(title);
                        arts.setTime(TimeUtils.getStringDateShort());
                        if (imgpath != null) {//用户更改图片后使用新地址
                            arts.setImgPath(imgpath);
                        }else{//没有更换图片则用之前图片地址
                            arts.setImgPath(art_imgPath);

                        }
                        ArtDao.insertArts(arts);
                        goMySqlActivity();
                    }
                }
            }
        });

        iv_insert.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showifDelDialog();
                return true;
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
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
                if (mEditor != null) {
                    showifSaveDialog();
                }else{
                    startActivity(new Intent(EditorActivity.this, MainActivity.class));
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

    private void goMySqlActivity() {
        Intent intent = new Intent(this, MySqlActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(editDate)) {//如果输入的内容为空
            super.onBackPressed();
        } else {//用户输入的内容不为空
            if (!isSave) {//没有点击保存
                showifSaveDialog();
            } else {  //点击了保存
                super.onBackPressed();
            }
        }
    }

    private void showifSaveDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您还没有保存，确定退出吗？")
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
        mEditor.destroy();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {
                imgpath = ImagePathUtils.getImageAbsolutePath(this, data.getData());
                // 如下方法可以在编辑界面添加图片,会把图片路径一起上传
                //  mEditor.insertImage(imgpath,"userImg");

                //压缩图片
                ImageCompress imageCompress = new ImageCompress();
                ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                options.uri = Uri.fromFile(new File(imgpath));
                options.maxHeight = 250;
                options.maxWidth = 250;
                insertBitmap = imageCompress.compressFromUri(EditorActivity.this, options);

//              insertBitmap = BitmapFactory.decodeFile(imgpath);
                if (insertBitmap != null) {
                    iv_insert.setVisibility(View.VISIBLE);
                    iv_insert.setImageBitmap(insertBitmap);
                }
            }
        }*/
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
                .setMessage("在设置-应用-RichText-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能")
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
