package com.example.leon.article.api.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ImgTagBean {

    /**
     * code : 1
     * data : ["/upload/aimg/170711/50d488d86a78b357eee90961cce996b6.jpg","/upload/aimg/170711/a8fde8aa57b862b1104ddb1e44f505b8.jpg","/upload/aimg/170711/c0da66724c493cce5705be6183466b4b.jpg"]
     * msg : 上传成功
     */

    private String code;
    private String msg;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ImgTagBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
