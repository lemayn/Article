package com.example.leon.article.bean;

import java.util.List;

/**
 *  首页推荐文章
 * Created by leonseven on 2017/5/27.
 */

public class RecomArtBean {


    /**
     * code : 1
     * msg :
     * data : {"tuijian":[{"aid":"21","atitle":"福利福利","aaddtime":"2017-05-26 15:49","aimg":"/upload/aimg/158d67b75105d12389603195d752796b.jpg"},{"aid":"20","atitle":"测试测试","aaddtime":"2017-05-25 15:40","aimg":"/upload/aimg/c422637bd57226665056fdbe813e4a80.jpg"}],"totalpage":1,"page":"1"}
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

    @Override
    public String toString() {
        return "RecomArtBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * tuijian : [{"aid":"21","atitle":"福利福利","aaddtime":"2017-05-26 15:49","aimg":"/upload/aimg/158d67b75105d12389603195d752796b.jpg"},{"aid":"20","atitle":"测试测试","aaddtime":"2017-05-25 15:40","aimg":"/upload/aimg/c422637bd57226665056fdbe813e4a80.jpg"}]
         * totalpage : 1
         * page : 1
         */

        private int totalpage;
        private String page;
        private List<TuijianBean> tuijian;

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

        public List<TuijianBean> getTuijian() {
            return tuijian;
        }

        public void setTuijian(List<TuijianBean> tuijian) {
            this.tuijian = tuijian;
        }

        public static class TuijianBean {
            /**
             * aid : 21
             * atitle : 福利福利
             * aaddtime : 2017-05-26 15:49
             * aimg : /upload/aimg/158d67b75105d12389603195d752796b.jpg
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
                return "TuijianBean{" +
                        "aid='" + aid + '\'' +
                        ", atitle='" + atitle + '\'' +
                        ", aaddtime='" + aaddtime + '\'' +
                        ", aimg='" + aimg + '\'' +
                        '}';
            }
        }

    }
}
