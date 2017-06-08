package com.example.leon.article.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.leon.article.R;

/**
 * Created by leonseven on 2017/6/8.
 */

public class ForgetPwdDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    Activity context;

    private Button btn_submit;
    private Button btn_login;

    public EditText edittext_account;
    public EditText edittext_phone;


    private View.OnClickListener mClickListener;

    public ForgetPwdDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public ForgetPwdDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.forgetpwd_dialog);

        edittext_phone = (EditText) findViewById(R.id.edittext_phone);
        edittext_account = (EditText) findViewById(R.id.edittext_account);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        // 为按钮绑定点击事件监听器
        btn_submit.setOnClickListener(mClickListener);
        btn_login.setOnClickListener(mClickListener);


        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
         p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);


        this.setCancelable(true);
    }
}
