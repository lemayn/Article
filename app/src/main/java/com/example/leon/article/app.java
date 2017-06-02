package com.example.leon.article;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.leon.article.sql.bean.DaoMaster;
import com.example.leon.article.sql.bean.DaoSession;

import java.util.ArrayList;
import java.util.List;


public class app extends Application {
    private static app mInstance;
    private static DaoSession daoSession;
    private Activity top_activity;
    private List<Activity> activityList = new ArrayList<>();

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
                activityList.add(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityList.remove(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                top_activity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                top_activity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    public Activity getCurrentActivity() {
        return top_activity;
    }

    public void exit() {
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
