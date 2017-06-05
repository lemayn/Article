package com.example.leon.article.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

public class DialogHelper {
    private static boolean isShowing;

    /**
     * 提问框的 Listener
     *
     * @author Lei
     */
    // 因为本类不是activity所以通过继承接口的方法获取到点击的事件
    public interface OnClickYesListener {
        abstract void onClickYes(DialogInterface dialog, int which);
    }

    /**
     * 提问框的 Listener
     */
    public interface OnClickNoListener {
        abstract void onClickNo(DialogInterface dialog, int which);
    }

    public static void showDialog(Context context, String title,
                                  String message, final OnClickYesListener listenerYes,
                                  final OnClickNoListener listenerNo) {
        synchronized (DialogHelper.class) {
            if (isShowing) {
                return;
            }
        }

        isShowing = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(title)) {
            // 设置title
            builder.setTitle(title);
        }
        if (listenerYes != null) {
            // 设置确定按钮，固定用法声明一个按钮用这个setPositiveButton
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 如果确定被电击
                            listenerYes.onClickYes(dialog, which);
                            isShowing = false;
                        }
                    });
        }

        if (listenerNo != null) {
            // 设置取消按钮，固定用法声明第二个按钮要用setNegativeButton
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 如果取消被点击
                            listenerNo.onClickNo(dialog, which);
                            isShowing = false;
                        }
                    });
        }

        // 控制这个dialog可不可以按返回键，true为可以，false为不可以
        builder.setCancelable(false);
        // 显示dialog
        builder.create().show();

    }

    /**
     * 处理字符的方法
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
}