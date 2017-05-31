package com.example.leon.article.bean;

import java.util.List;

/**
 * Created by leonseven on 2017/5/27.
 */

public class ExcellentBean {


    /**
     * code : 1
     * msg :
     */

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

    public DataBean getData() {
        return data;
    }


    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ExcellentBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * totalpage : 1
         * page : 1
         */

        private int totalpage;
        private String page;
        private List<GoodBean> good;

        public int getTotalpage() {
            return totalpage;
        }

        public void setTotalpage(int totalpage) {
            this.totalpage = totalpage;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public List<GoodBean> getGood() {
            return good;
        }

        public void setGood(List<GoodBean> good) {
            this.good = good;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "totalpage=" + totalpage +
                    ", page='" + page + '\'' +
                    ", good=" + good +
                    '}';
        }

        public static class GoodBean {
            /**
             * aid : 28
             * atitle : 哈佛大学的高材生研究出了治愈拖延症有效可行的简单办法
             * aaddtime : 2017-05-27 11:13
             * aimg :
             */

            private String aid;
            private String atitle;
            private String aaddtime;
            private String aimg;

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

            public String getAaddtime() {
                return aaddtime;
            }

            public void setAaddtime(String aaddtime) {
                this.aaddtime = aaddtime;
            }

            public String getAimg() {
                return aimg;
            }

            public void setAimg(String aimg) {
                this.aimg = aimg;
            }

            @Override
            public String toString() {
                return "GoodBean{" +
                        "aid='" + aid + '\'' +
                        ", atitle='" + atitle + '\'' +
                        ", aaddtime='" + aaddtime + '\'' +
                        ", aimg='" + aimg + '\'' +
                        '}';
            }
        }
    }
}
