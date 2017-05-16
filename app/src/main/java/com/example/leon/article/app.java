package com.example.leon.article;

import android.app.Application;


public class app extends Application {
    private static app mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static app getInstance() {
        return mInstance;
    }
}
