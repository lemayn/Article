package com.example.leon.article.sql.dao;

import com.example.leon.article.app;
import com.example.leon.article.sql.bean.Arts;

import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ArtDao {

    /**
     * 增加数据
     * @param arts
     */
    public static void insertArts(Arts arts){
        app.getDaoSession().getArtsDao().insertOrReplace(arts);
    }

    /**
     * 删除数据
     * @param id
     */
    public static void deleteArtsById(Long id){
        app.getDaoSession().getArtsDao().deleteByKey(id);
    }

    public static void deleteArts(Arts arts){
        app.getDaoSession().getArtsDao().delete(arts);
    }

    /**
     * 更新数据
     * @param arts
     */
    public static void updateArts(Arts arts){
        app.getDaoSession().getArtsDao().update(arts);
    }

    /**
     * 查询数据，返回集合数据
     * @return
     */
    public static List<Arts> queryArts(){
        return app.getDaoSession().getArtsDao().queryBuilder().list();
    }
}
