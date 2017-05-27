package com.example.leon.article.api.bean;

/**
 * Created by Administrator on 2017/5/20.
 */

public class ArtInfoBean extends ApiBean{
    /**
     * code : 1
     * msg :
     * data : {"aid":"19","atitle":"接着测试","aimg":null,"acontent":"长江长江，一条长江向东流。黄河啊黄河，黄河大合唱","aaddtime":"2017-05-24 09:06","review":"1","amoney":"0","reason":"","mname":"Test01"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * aid : 19
         * atitle : 接着测试
         * aimg : null
         * acontent : 长江长江，一条长江向东流。黄河啊黄河，黄河大合唱
         * aaddtime : 2017-05-24 09:06
         * review : 1
         * amoney : 0
         * reason :
         * mname : Test01
         */

        private String aid;
        private String atitle;
        private Object aimg;
        private String acontent;
        private String aaddtime;
        private String review;
        private String amoney;
        private String reason;
        private String mname;

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

        public Object getAimg() {
            return aimg;
        }

        public void setAimg(Object aimg) {
            this.aimg = aimg;
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

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }
    }
}