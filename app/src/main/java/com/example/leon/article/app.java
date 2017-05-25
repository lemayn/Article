package com.example.leon.article;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.leon.article.sql.bean.DaoMaster;
import com.example.leon.article.sql.bean.DaoSession;


public class app extends Application {
    private static app mInstance;
    private static DaoSession daoSession;
    private Activity top_activity;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        setupDataBase();

        initGlobeActivity();
    }

    public static app getInstance() {
        return mInstance;
    }

    private void setupDataBase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "art-db");
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster master = new DaoMaster(database);
        daoSession = master.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                top_activity = activity;
                Log.e("onActivityCreated===", top_activity + "");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("onActivityDestroyed===", top_activity + "");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                top_activity = activity;
                Log.e("onActivityStarted===", top_activity + "");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                top_activity = activity;
                Log.e("onActivityResumed===", top_activity + "");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("onActivityPaused===", top_activity + "");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("onActivityStopped===", top_activity + "");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    public Activity getCurrentActivity() {
        return top_activity;
    }
}
