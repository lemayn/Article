package com.example.leon.article.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityBasicinformationBinding;
import com.example.leon.article.databinding.ActivityVipCenterBinding;
import com.example.leon.article.utils.CommonUtils;

/**
 * Basic Information
 * Created by leonseven on 2017/5/16.
 */

public class BasicinformationActivity extends ToolBarBaseActivity<ActivityBasicinformationBinding> {


    private TextInputLayout mTextinput_layout1;
    private EditText mEdit_useraccount;
    private TextInputLayout mTextinput_layout2;
    private EditText mEdit_number;
    private TextInputLayout mTextinput_layout3;
    private EditText mEdit_qq;
    private TextInputLayout mTextinput_layout4;
    private EditText mEdit_weixin;
    private TextInputLayout mTextinput_layout5;
    private EditText mEdit_email;
    private Button mBtn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinformation);

        bindViews();
    }

    private void bindViews() {

        setTitle(CommonUtils.getString(R.string.basic_info));
        setNavigationView();

//        mTextinput_layout1 = (TextInputLayout) findViewById(R.id.textinput_layout1);
//        mEdit_useraccount = (EditText) findViewById(R.id.edit_useraccount);
//        mTextinput_layout2 = (TextInputLayout) findViewById(R.id.textinput_layout2);
//        mEdit_number = (EditText) findViewById(R.id.edit_number);
//        mTextinput_layout3 = (TextInputLayout) findViewById(R.id.textinput_layout3);
//        mEdit_qq = (EditText) findViewById(R.id.edit_qq);
//        mTextinput_layout4 = (TextInputLayout) findViewById(R.id.textinput_layout4);
//        mEdit_weixin = (EditText) findViewById(R.id.edit_weixin);
//        mTextinput_layout5 = (TextInputLayout) findViewById(R.id.textinput_layout5);
//        mEdit_email = (EditText) findViewById(R.id.edit_email);
//        mBtn_register = (Button) findViewById(R.id.btn_register);

    }

}
