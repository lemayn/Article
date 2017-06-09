package com.example.leon.article.Activity.bank;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.adapter.ListViewDialogAdapter;
import com.example.leon.article.api.bean.BankConfigBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityAddCardBinding;
import com.example.leon.article.presenter.bankpresenter.bankpresenterImp.BankPresenterImp;
import com.example.leon.article.utils.BankUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.view.IAddCardActivity;
import com.example.leon.article.widget.EditTextWithClear;

import java.util.ArrayList;
import java.util.List;

public class AddCardActivity extends ToolBarBaseActivity<ActivityAddCardBinding> implements IAddCardActivity,View.OnClickListener {

    private EditTextWithClear et_cardNum;
    private EditText et_userbankName;
    private EditText et_userAdress;
    private EditText et_withdrawPwd;
    private TextView tv_choose_bank;
    private Button bt_addConfirm;
    private List<BankConfigBean.DataBean> bankList = new ArrayList<>();
    private BankPresenterImp presenterImp;
    private String sid;
    private String cookie;
    private ListViewDialogAdapter dialogAdapter;
    private int bid;
    private ImageView iv_chooseBank;
    private LinearLayout ll_chooseBank;
    private ImageView iv_chooseBank2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        initView();
        initDate();
        initEvent();
    }

    private void initDate() {
        //get cookie sid
        cookie = (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
        sid = (String) SPUtil.get(Constant.Share_prf.SID, "");
        presenterImp = new BankPresenterImp(this,this);
        presenterImp.getBankConfig(cookie,sid);
    }

    private void initEvent() {
        loadUserData();
        bt_addConfirm.setOnClickListener(this);
        ll_chooseBank.setOnClickListener(this);
    }

    private void initView() {
        setTitle(getString(R.string.add_bankcard));
        setNavigationView();
        ll_chooseBank = (LinearLayout) findViewById(R.id.ll_chooseBank);
        iv_chooseBank = (ImageView) findViewById(R.id.iv_chooseBank);
        iv_chooseBank2 = (ImageView) findViewById(R.id.iv_chooseBank02);
        et_cardNum = (EditTextWithClear) findViewById(R.id.et_card_numb);
        et_userbankName = (EditText) findViewById(R.id.et_userbankName);
        et_userAdress = (EditText) findViewById(R.id.et_userAdress);
        et_withdrawPwd = (EditText) findViewById(R.id.et_withdrawPwd);
        bankCardNumAddSpace(et_cardNum);
        tv_choose_bank = getViewById(R.id.tv_choose_bank);
        bt_addConfirm = getViewById(R.id.bt_addConfirm);
    }

    //银行卡号码的格式
    public void bankCardNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    private <T extends View> T getViewById(int res){
        return (T) findViewById(res);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_chooseBank:
                //显示列表dialog?
                /*iv_chooseBank.setVisibility(View.GONE);
                iv_chooseBank2.setVisibility(View.VISIBLE);*/
                iv_chooseBank.setImageResource(R.mipmap.bank_set02);
                showListDialog();
                break;
            case R.id.bt_addConfirm:
                String accountName = et_userbankName.getText().toString().trim();
                String address = et_userAdress.getText().toString();
                //提现密码
                String withdrawPwd = et_withdrawPwd.getText().toString();
                //加密后的密码
//                String md5Pwd = CommonUtils.getMD5Str(withdrawPwd);
                String cardNumWithClear = et_cardNum.getText().toString().trim();
                String cardNumb = cardNumWithClear.replace(" ", "");
                String nameOfBank = BankUtils.getNameOfBank(cardNumb);
                if (!TextUtils.isEmpty(cardNumb) && !TextUtils.isEmpty(withdrawPwd) && !TextUtils.isEmpty(accountName) && !TextUtils.isEmpty(address)) {
                    checkBank(nameOfBank,cardNumWithClear,accountName,address,withdrawPwd);
                }else{
                    Toast.makeText(this,getString(R.string.please_fill_in),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void checkBank(String nameOfBank, final String cardNumWithClear, final String accountName, final String address,final String withdrawPwd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_your_bans))
                .setMessage(nameOfBank)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenterImp.bindBankCard(cookie,String.valueOf(bid),cardNumWithClear,sid,accountName,address,withdrawPwd);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }


    private void showListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.please_choose_yourbank))
                .setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bid = which + 1;
                        tv_choose_bank.setTextColor(Color.BLACK);
                        tv_choose_bank.setText(bankList.get(which).getBank());
                        iv_chooseBank.setImageResource(R.mipmap.bank_set01);
                    }
                })
                .create().show();
    }

    @Override
    public void setBankConfig(BankConfigBean bankConfig) {
        bankList.addAll(bankConfig.getData());
        dialogAdapter = new ListViewDialogAdapter(this, bankList);
    }

    @Override
    public void showResult() {
        goBankSettingActivity();
    }

    private void goBankSettingActivity() {
        Intent intent = new Intent(AddCardActivity.this, BankSettingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterImp.unsubcrible();
    }
}
