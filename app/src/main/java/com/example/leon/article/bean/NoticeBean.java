package com.example.leon.article.bean;

import java.util.List;

/**
 * Created by leonseven on 2017/5/31.
 */

public class NoticeBean {


    /**
     * code : 1
     * msg :
     */

    private String code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NoticeBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * nid : 7
         * ntitle : 这是 第五条
         * ncontent : <p>第五条&nbsp;</p><p>无敌的第五条</p><p><br/></p>
         * naddtime : 2017-05-27 13:24
         */

        private String nid;
        private String ntitle;
        private String ncontent;
        private String naddtime;

        public String getNid() {
            return nid;
        }

        public void setNid(String nid) {
            this.nid = nid;
        }

        public String getNtitle() {
            return ntitle;
        }

        public void setNtitle(String ntitle) {
            this.ntitle = ntitle;
        }

        public String getNcontent() {
            return ncontent;
        }

        public void setNcontent(String ncontent) {
            this.ncontent = ncontent;
        }

        public String getNaddtime() {
            return naddtime;
        }

        public void setNaddtime(String naddtime) {
            this.naddtime = naddtime;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "nid='" + nid + '\'' +
                    ", ntitle='" + ntitle + '\'' +
                    ", ncontent='" + ncontent + '\'' +
                    ", naddtime='" + naddtime + '\'' +
                    '}';
        }
    }
}
