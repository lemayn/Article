package com.example.leon.article.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.leon.article.BR;

public class ItemBean extends BaseObservable {

    public ItemBean(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    private int icon;
    private String title;

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
