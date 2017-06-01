package com.example.leon.article.Activity.bank;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leon.article.R;
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
    private TextView tv_choose_bank;
    private TextInputLayout textInput_withdrawPwd;
    private Button bt_addConfirm;
    private List<BankConfigBean.DataBean> bankList = new ArrayList<>();
    private List<String> banks = new ArrayList<>();
    private BankPresenterImp presenterImp;
    private int bid;
    private TextInputLayout textInput_account_name;
    private TextInputLayout textInput_cardNub;
    private TextInputLayout textInput_address;
    private String sid;
    private String cookie;

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
        tv_choose_bank.setOnClickListener(this);
        bt_addConfirm.setOnClickListener(this);
    }

    private void initView() {
        setTitle(getString(R.string.add_bankcard));
        et_cardNum = (EditTextWithClear) findViewById(R.id.et_card_numb);
        bankCardNumAddSpace(et_cardNum);
        textInput_withdrawPwd = getViewById(R.id.textInput_withdrawPwd);
        textInput_account_name = getViewById(R.id.textInput_account_name);
        textInput_cardNub = getViewById(R.id.textInput_cardNub);
        textInput_address = getViewById(R.id.textInput_address);
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
            case R.id.tv_choose_bank:
                //显示列表dialog?
                if (banks.size() > 0) {
                    showListDialog();
                }
                break;
            case R.id.bt_addConfirm:
                //提现密码
                String withdrawPwd = textInput_withdrawPwd.getEditText().getText().toString();
                String accountName = textInput_account_name.getEditText().getText().toString();
                String address = textInput_address.getEditText().getText().toString();
                String cardNumWithClear = textInput_cardNub.getEditText().getText().toString().trim();
                String cardNumb = cardNumWithClear.replace(" ", "");
                String nameOfBank = BankUtils.getNameOfBank(cardNumb);
                checkBank(nameOfBank,cardNumWithClear,accountName,address);
               /* presenterImp.bindBankCard(cookie,String.valueOf(bid),
                        cardNum,sid,accountName,address);*/
                break;
        }
    }

    private void checkBank(String nameOfBank, final String cardNumWithClear, final String accountName, final String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请确定你银行卡的所属银行")
                .setMessage(nameOfBank)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenterImp.bindBankCard(cookie,String.valueOf(bid),cardNumWithClear,sid,accountName,address);
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
        final String[] Ibanks = banks.toArray(new String[banks.size()]);
        final int[] index = new int[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择开户银行")
                .setIcon(R.mipmap.ic_launcher)
                .setSingleChoiceItems(Ibanks, 0 , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                        index[0] = which;
                        tv_choose_bank.setText(Ibanks[which]);
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        bid = index[0]+1;
                        tv_choose_bank.setText(Ibanks[index[0]]);
                    }
                })
                .create().show();
    }

    @Override
    public void setBankConfig(BankConfigBean bankConfig) {
        bankList.addAll(bankConfig.getData());
        for (BankConfigBean.DataBean dataBean : bankList) {
            banks.add(dataBean.getBank());
        }
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
