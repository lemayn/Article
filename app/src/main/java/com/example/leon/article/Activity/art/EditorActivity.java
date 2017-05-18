package com.example.leon.article.Activity.art;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.sql.bean.Arts;
import com.example.leon.article.sql.dao.ArtDao;
import com.example.leon.article.utils.TimeUtils;

import jp.wasabeef.richeditor.RichEditor;

public class EditorActivity extends AppCompatActivity {

    //MySQL传递过来的数据
    private String art_title;
    private String art_content;

    private RichEditor mEditor;
    private EditText artTitle;
    //输入的内容
    private String editDate;
    //记录是否点击了保存
    private boolean isSave = false;

    Arts arts = new Arts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initView();
        //获取用户输入
        GetDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDate();
    }

    private void initDate() {
        art_title = getIntent().getStringExtra(ArtConstant.ART_TITLE);
        art_content = getIntent().getStringExtra(ArtConstant.ART_CONTENT);
        if (art_title != null) {
            artTitle.setText(art_title);
        }
        if (art_content != null) {
            mEditor.setHtml(art_content);
        }
    }

    private void GetDate() {
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.i("TAG", "onTextChange: " + text);
                editDate = text;
            }
        });
    }

    private void initView() {
        initEditor();
        initEvent();
    }

    private void initEditor() {
        artTitle = (EditText) findViewById(R.id.et_title);
        mEditor = (RichEditor) findViewById(R.id.richEditor);
        mEditor.setClickable(true);
        mEditor.setEditorHeight(200);
//        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("请开始你的表演...");
    }

    private void initEvent() {
        //点击发布后,做提交处理
        findViewById(R.id.tv_issue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击之后将软键盘隐藏
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditor.getWindowToken(), 0);
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
                        ArtDao.insertArts(arts);
                        goMySqlActivity();
                    }
                }
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
    }

    private void goMySqlActivity() {
        Intent intent = new Intent(this, MySqlActivity.class);
        startActivity(intent);
        finish();
    }

    private void goArticleActivity() {
        Intent intent = new Intent(this, ArticleActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (editDate == null) {//如果输入的内容为空
            super.onBackPressed();
        } else {//用户输入的内容不为空

            if (!isSave) {//没有点击保存
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("还没有保存哦，确定退出吗")
                        .setMessage("退出可能就白写咯")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing or what you want
                            }
                        })
                        .create()
                        .show();
            } else {  //点击了保存
                // do nothing
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
    }
}
