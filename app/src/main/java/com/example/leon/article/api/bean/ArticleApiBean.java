package com.example.leon.article.api.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.leon.article.BR;

public class ArticleApiBean extends BaseObservable {

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Bindable
    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }

    public static class DataBean extends BaseObservable {

        private String mid;
        private String mname;
        private String nickname;
        private String cookie;
        private String sid;

        private String mtell;
        private String mqq;
        private String mmoney;
        private String weixin;
        private String email;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getMtell() {
            return mtell;
        }

        public void setMtell(String mtell) {
            this.mtell = mtell;
        }

        public String getMqq() {
            return mqq;
        }

        public void setMqq(String mqq) {
            this.mqq = mqq;
        }

        @Bindable
        public String getMmoney() {
            return mmoney;
        }

        public void setMmoney(String mmoney) {
            this.mmoney = mmoney;
            notifyPropertyChanged(BR.mmoney);
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

    }
}
