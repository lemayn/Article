package com.example.leon.article.utils;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leon.article.Activity.CustomerServiceActivity;
import com.example.leon.article.R;
import com.example.leon.article.api.bean.StatementBean;
import com.example.leon.article.widget.AlignTextView;
import com.example.leon.article.widget.RadiusBackgroundSpan;

public class DataBindingUtil {

    private static final String BASE_IMAGE_URL = "http://118.89.233.35:8989";

    @BindingAdapter("android:src")
    public static void setImageSrc(ImageView imageView, int res) {
        if (res != 0) {
            imageView.setImageResource(res);
        }
    }

    @BindingAdapter("android:visibility")
    public static void visibility(View view, String res) {
        if (TextUtils.isEmpty(res)) {
            return;
        }
        if ("1".equals(res)) {
            view.setVisibility(View.GONE);
        } else if ("2".equals(res)) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("image")
    public static void imageLoader(ImageView imageView, String imageUrls) {
        if (TextUtils.isEmpty(imageUrls)) {
            return;
        }
        Glide.with(imageView.getContext()).load(BASE_IMAGE_URL + imageUrls)
                .crossFade()
                //                .placeholder(R.drawable.timg)  不添加占位图，CircleImageView有问题，添加后第一次加载只显示占位图
                .error(R.drawable.timg)
                .into(imageView);
    }

    @BindingAdapter("data")
    public static void loadUserData(EditText et, String data) {
        if (!TextUtils.isEmpty(data)) {
            et.setText(data);
            et.setKeyListener(null);
        }
    }

    @BindingAdapter("bankcard")
    public static void setBankCard(TextView editText, String card) {
        if (TextUtils.isEmpty(card)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < card.length(); i++) {
            char c = card.charAt(i);
            if (i <= card.length() - 5) {
                if (String.valueOf(c).equals("\t")) {
                    sb.append("\t");
                } else {
                    sb.append("*");
                }
            } else {
                sb.append(c);
            }
        }
        editText.setText(sb.toString());
    }

    @BindingAdapter("statement_type")
    public static void setStatementType(TextView textView, int type) {
        if (type == 0) {
            textView.setText("提现");
        } else if (type == 1) {
            textView.setText("佣金");
        }
    }

    @BindingAdapter("statement_handle")
    public static void setStatementHandle(AlignTextView textView, String handle) {
        if (TextUtils.isEmpty(handle)) {
            return;
        }
        StringBuffer handleState = new StringBuffer();
        int color = 0;
        if ("0".equals(handle)) {
            handleState.append(CommonUtils.getString(R.string.statement_pending));
            color = CommonUtils.getColor(R.color.statement_handle_pending);
        } else if ("1".equals(handle)) {
            handleState.append(CommonUtils.getString(R.string.statement_success));
            color = CommonUtils.getColor(R.color.statement_handle_success);
        } else if ("2".equals(handle)) {
            handleState.append(CommonUtils.getString(R.string.statement_fail));
            color = CommonUtils.getColor(R.color.statement_handle_fail);
        }
        textView.setAlingTextColor(color);
        textView.setAlingText(handleState.toString());
    }

    @BindingAdapter("statement_money")
    public static void setStatementMoney(TextView textView, StatementBean.DataBean.MoneyConfig config) {
        if (config == null) {
            return;
        }
        StringBuffer money = new StringBuffer();
        if (config.getType() == 0) {
            textView.setText(money.append("-").append(config.getMoney()).toString());
        } else if (config.getType() == 1) {
            textView.setText(money.append("+").append(config.getMoney()).toString());
        }
    }

    @BindingAdapter("servicetext")
    public static void servicetext(TextView textView, String content) {
        SpannableString spannableString = new SpannableString(content);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#737373"));
        spannableString.setSpan(colorSpan, 0, spannableString.length() - 6,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#737373"));
        spannableString.setSpan(colorSpan1, spannableString.length() - 2, spannableString.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RadiusBackgroundSpan radiusBackgroundSpan = new RadiusBackgroundSpan(Color.parseColor("#E11B03"),
                CommonUtils.getDimension(R.dimen.x9));
        spannableString.setSpan(radiusBackgroundSpan, spannableString.length() - 6, spannableString.length() - 2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        MyClickableSpan clickableSpan = new MyClickableSpan(textView.getContext());
        spannableString.setSpan(clickableSpan, spannableString.length() - 6, spannableString.length() - 2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }

    /***************************************************************/

    static class MyClickableSpan extends ClickableSpan {

        private Context context;

        public MyClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            Intent intent1 = new Intent(context, CustomerServiceActivity.class);
            context.startActivity(intent1);
        }
    }
}
