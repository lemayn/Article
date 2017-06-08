package com.example.leon.article.utils;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leon.article.R;

public class DataBindingUtil {

    private static final String BASE_IMAGE_URL = "http://118.89.233.35:8989";

    @BindingAdapter("android:src")
    public static void setImageSrc(ImageView imageView, int res) {
        if (res != 0) {
            imageView.setImageResource(res);
        }
    }

    @BindingAdapter("image")
    public static void imageLoader(ImageView imageView, String imageUrls) {
        Glide.with(imageView.getContext()).load(BASE_IMAGE_URL + imageUrls)
                .crossFade()
                .placeholder(R.drawable.timg).error(R.drawable.timg)
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
        if(TextUtils.isEmpty(card)) {
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
}
