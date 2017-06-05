package com.example.leon.article.api.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */

public class BankConfigBean extends ApiBean{

    /**
     * code : 1
     * msg :
     * data : [{"bid":"1","bank":"中国银行"},{"bid":"2","bank":"招商银行"},{"bid":"3","bank":"农业银行"},{"bid":"4","bank":"工商银行"},{"bid":"5","bank":"建设银行"},{"bid":"6","bank":"交通银行"}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bid : 1
         * bank : 中国银行
         */

        private String bid;
        private String bank;
        private String bimg;

        public String getBimg() {
            return bimg;
        }

        public void setBimg(String bimg) {
            this.bimg = bimg;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }
    }
}
