package com.example.leon.article.api.bean;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UpLoadArtBean extends ApiBean{

    /**
     * code : 1
     * msg : 上传成功
     * data : null
     */

    private Object data;

  /*  public String getCode() {
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
    }*/

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
