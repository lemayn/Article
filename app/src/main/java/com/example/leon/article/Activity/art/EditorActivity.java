package com.example.leon.article.Activity.art;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.fragment.ArticleFragment;
import com.example.leon.article.presenter.artpresenter.artpresenterImp.ArtPresenterImp;
import com.example.leon.article.sql.bean.Arts;
import com.example.leon.article.sql.dao.ArtDao;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.ImagePathUtils;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.utils.TimeUtils;
import com.example.leon.article.view.IEditorActivity;
import com.example.leon.article.widget.SpinnerDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class EditorActivity extends AppCompatActivity implements IEditorActivity{

    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 100;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initView();
        //获取用户输入，cookie，sid
        GetDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取数据库数据
        initDate();
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
            iv_insert.setVisibility(View.VISIBLE);
            iv_insert.setImageBitmap(BitmapFactory.decodeFile(art_imgPath));
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
        //点击发布后,做提交处理
        findViewById(R.id.bt_editor_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditor.getWindowToken(), 0);*/
                //隐藏软键盘
                hideKeyboard();
                String title = artTitle.getText().toString();
                String bytesFromBitmap = getBytesFromBitmap(insertBitmap);
                artPresenter.uploadUserArt(cookie,title,editDate,sid,bytesFromBitmap);
            }
        });

        //点击保存后
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave = true;
                String title = artTitle.getText().toString().trim();
                if (TextUtils.isEmpty(title)){
                    Toast.makeText(EditorActivity.this,getString(R.string.titlenotnull),Toast.LENGTH_SHORT).show();
                    artTitle.setFocusable(true);
                }else {
                    if (editDate == null) {
                        goArticleActivity();
                    } else {    //不为空存入数据库
                        arts.setContent(editDate);
                        arts.setTitle(title);
                        arts.setTime(TimeUtils.getStringDateShort());
                        arts.setImgPath(imgpath);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    List<String> permissions = new ArrayList<String>();
                    if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    } else {
                        //从相册中选择图片
                        pickImgfromPhoto();
                    }
                    if (!permissions.isEmpty()) {
                        requestPermissions(permissions.toArray(new String[permissions.size()]),REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
                    }
                }else{
                    pickImgfromPhoto();
                }

            }
        });

    }

    private void pickImgfromPhoto() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //根据版本号不同使用不同的Action
        if (Build.VERSION.SDK_INT <19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent,REQUEST_CODE_PICK_IMAGE);
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

    private void goArticleActivity() {
        Intent intent = new Intent(this, ArticleFragment.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(editDate)) {//如果输入的内容为空
            super.onBackPressed();
        } else {//用户输入的内容不为空

            if (!isSave) {//没有点击保存
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
            } else {  //点击了保存
                super.onBackPressed();
            }
        }
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
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {
                imgpath = ImagePathUtils.getImageAbsolutePath(this, data.getData());
                // 如下方法可以在编辑界面添加图片,会把图片路径一起上传
                //  mEditor.insertImage(imgpath,"userImg");
                insertBitmap = BitmapFactory.decodeFile(imgpath);
                if (insertBitmap != null) {
                    iv_insert.setVisibility(View.VISIBLE);
                    iv_insert.setImageBitmap(insertBitmap);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public String getBytesFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,baos);
        byte[] bytes = baos.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes,Base64.DEFAULT));
        return imgString;
    }

}
