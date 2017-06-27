package com.example.leon.article.api.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */

public class UploadClassifyBean {
    /**
     * code : 1
     * msg :
     * data : [{"class_id":"1","class_name":"搞笑"},{"class_id":"2","class_name":"娱乐"},{"class_id":"3","class_name":"其他"}]
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

    public static class DataBean {
        /**
         * class_id : 1
         * class_name : 搞笑
         */

        private String class_id;
        private String class_name;

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "class_id='" + class_id + '\'' +
                    ", class_name='" + class_name + '\'' +
                    '}';
        }
    }
}
