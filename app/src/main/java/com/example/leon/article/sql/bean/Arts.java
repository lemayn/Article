package com.example.leon.article.sql.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/5/15.
 */

@Entity
public class Arts {

    //不可以用int
    @Id(autoincrement = true)
    private Long id;
    //唯一值
    @Unique
    private String author;

    @Property(nameInDb = "userArts")
    private String title;
    private String content;
    private String time;

    @Generated(hash = 1575552889)
    public Arts(Long id, String author, String title, String content, String time) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.time = time;
    }
    @Generated(hash = 1692251737)
    public Arts() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
