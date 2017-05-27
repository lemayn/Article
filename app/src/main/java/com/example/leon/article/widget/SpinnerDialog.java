package com.example.leon.article.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leon.article.R;

/**
 * Created by Administrator on 2017/5/23.
 */

public class SpinnerDialog {

    public static Dialog createSpinnerDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.spinner_style_dialog, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_spinner_dialog);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_spinner);
        TextView textView = (TextView) view.findViewById(R.id.tv_spinner);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        imageView.startAnimation(animation);

        textView.setText(msg);

        Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);         // 设置按返回键时取消对话框
        dialog.setCanceledOnTouchOutside(false);    // 设置点击对话框外部时不取消对话框，默认为true
        dialog.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        return dialog;
    }

}
