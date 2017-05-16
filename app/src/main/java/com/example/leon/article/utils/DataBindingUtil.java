package com.example.leon.article.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

public class DataBindingUtil {

    @BindingAdapter("android:src")
    public static void setImageSrc(ImageView imageView, int res) {
        if (res != 0) {
            imageView.setImageResource(res);
        }
    }
}
