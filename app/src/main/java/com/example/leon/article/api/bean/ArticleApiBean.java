package com.example.leon.article.api.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.leon.article.BR;

public class ArticleApiBean extends ApiBean {

    private DataBean data;

    @Bindable
    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }

    @Override
    public String toString() {
        return "ArticleApiBean{" +
                "data=" + data +
                '}';
    }

    public static class DataBean extends BaseObservable {

        private String aid;
        private String atitle;
        private String acontent;
        private String aaddtime;
        private String review;
        private String amoney;
        private String reason;
        private String aimg;

        private String mid;
        private String mname;
        private String nickname;
        private String mimg;

        private String cookie;
        private String sid;

        private String mtell;
        private String mqq;
        private String mmoney;
        private String weixin;
        private String email;

        private String use_money;
        private String get_money;

        private String explain;
        /**
         * 是否已验证手机
         */
        private String tell_test;

        private String verify;

        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }

        public String getTell_test() {
            return tell_test;
        }

        public void setTell_test(String tell_test) {
            this.tell_test = tell_test;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        @Bindable
        public String getUse_money() {
            return use_money;
        }

        public void setUse_money(String use_money) {
            this.use_money = use_money;
            notifyPropertyChanged(BR.use_money);
        }

        @Bindable
        public String getGet_money() {
            return get_money;
        }

        public void setGet_money(String get_money) {
            this.get_money = get_money;
            notifyPropertyChanged(BR.get_money);
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getAtitle() {
            return atitle;
        }

        public void setAtitle(String atitle) {
            this.atitle = atitle;
        }

        public String getAcontent() {
            return acontent;
        }

        public void setAcontent(String acontent) {
            this.acontent = acontent;
        }

        public String getAaddtime() {
            return aaddtime;
        }

        public void setAaddtime(String aaddtime) {
            this.aaddtime = aaddtime;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getAmoney() {
            return amoney;
        }

        public void setAmoney(String amoney) {
            this.amoney = amoney;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getAimg() {
            return aimg;
        }

        public void setAimg(String aimg) {
            this.aimg = aimg;
        }

        public String getMimg() {
            return mimg;
        }

        public void setMimg(String mimg) {
            this.mimg = mimg;
        }

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

        @Bindable
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
            notifyPropertyChanged(BR.nickname);
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "aid='" + aid + '\'' +
                    ", atitle='" + atitle + '\'' +
                    ", acontent='" + acontent + '\'' +
                    ", aaddtime='" + aaddtime + '\'' +
                    ", review='" + review + '\'' +
                    ", amoney='" + amoney + '\'' +
                    ", reason='" + reason + '\'' +
                    ", aimg='" + aimg + '\'' +
                    ", mid='" + mid + '\'' +
                    ", mname='" + mname + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", mimg='" + mimg + '\'' +
                    ", cookie='" + cookie + '\'' +
                    ", sid='" + sid + '\'' +
                    ", mtell='" + mtell + '\'' +
                    ", mqq='" + mqq + '\'' +
                    ", mmoney='" + mmoney + '\'' +
                    ", weixin='" + weixin + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}
