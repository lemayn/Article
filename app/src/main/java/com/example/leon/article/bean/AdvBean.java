package com.example.leon.article.bean;

import java.util.List;

/**
 *  首页 广告
 * Created by leonseven on 2017/5/27.
 */

public class AdvBean {

    /**
     * code : 1
     * msg :
     * data : [{"id":"3","name":"双色球","url":"www.baidu.com","img":"/upload/ad/170526102947255.png"}]
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
        return "AdvBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * id : 3
         * name : 双色球
         * url : www.baidu.com
         * img : /upload/ad/170526102947255.png
         */

        private String id;
        private String name;
        private String url;
        private String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", img='" + img + '\'' +
                    '}';
        }
    }
}
