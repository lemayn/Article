package com.example.leon.article.utils;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

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
}
