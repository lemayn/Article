package com.example.leon.article;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.leon.article.sql.bean.DaoMaster;
import com.example.leon.article.sql.bean.DaoSession;


public class app extends Application {
    private static app mInstance;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        setupDataBase();
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

    public static DaoSession getDaoSession(){
        return daoSession;
    }
}
