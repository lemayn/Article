package com.example.leon.article;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.leon.article.Activity.record.BaseApplication;
import com.example.leon.article.Activity.record.camer.commonutil.utils.CrashHandler;
import com.example.leon.article.Activity.record.camer.commonutil.utils.DeviceUtil;
import com.example.leon.article.Activity.record.camer.utils.VCameraUtils;
import com.example.leon.article.sql.bean.DaoMaster;
import com.example.leon.article.sql.bean.DaoSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class app extends BaseApplication {
    private static app mInstance;
    private static DaoSession daoSession;
    private Activity top_activity;
    private List<Activity> activityList = new ArrayList<>();
    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();
      /*  Toast.makeText(this, "install 0", Toast.LENGTH_SHORT).show();
        install(this);
        Toast.makeText(this, "install 3", Toast.LENGTH_SHORT).show();*/

        mInstance = this;

        setupDataBase();

        initGlobeActivity();

        //初始化VCamera
        VCameraUtils.initVitamioRecoder(this);
        //初始化Android设备信息
        DeviceUtil.init(this);
        //初始化CrashHandler
        CrashHandler.getInstance().init(this);
    }

    public static HttpProxyCacheServer getProxy() {
        return mInstance.proxy == null ? (mInstance.proxy = mInstance.newProxy()) : mInstance.proxy;
    }

    /**
     * @return 设置最大缓存200M，最大缓存文件20个
     */
    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(200 * 1024 * 1024)
                .maxCacheFilesCount(20)
                .build();
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

    /**
     * 通过隐式意图调用系统安装程序安装APK
     */

    public static void install(Context context) {
        Toast.makeText(context, "install 1", Toast.LENGTH_SHORT).show();
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "xiuwen.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Toast.makeText(context, "install 7.0", Toast.LENGTH_SHORT).show();
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.example.leon.article.fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            Toast.makeText(context, "install 6.0<=", Toast.LENGTH_SHORT).show();
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
