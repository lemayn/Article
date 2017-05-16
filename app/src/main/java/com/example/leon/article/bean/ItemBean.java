package com.example.leon.article.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.leon.article.BR;

public class ItemBean extends BaseObservable {

    public ItemBean(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public ItemBean(int icon, String title, Class clazz) {
        this.icon = icon;
        this.title = title;
        this.clazz = clazz;
    }

    private int icon;
    private String title;
    private Class clazz;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    @Bindable
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }
}
