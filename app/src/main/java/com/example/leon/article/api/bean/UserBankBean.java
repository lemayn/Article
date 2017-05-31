package com.example.leon.article.api.bean;

import java.util.List;

/**
 * Created by Hu on 2017/5/30.
 */

public class UserBankBean extends ApiBean{

    /**
     * code : 1
     * msg :
     * data : [{"cid":"4","bank":"农业银行","card":"6228481698729890079","account_name":"啦啦啦"},{"cid":"5","bank":"农业银行","card":"6228481698729890078","account_name":"啦啦啦"}]
     */

    /**
     * cid : 4
     * bank : 农业银行
     * card : 6228481698729890079
     * account_name : 啦啦啦
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String cid;
        private String bank;
        private String card;
        private String account_name;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getAccount_name() {
            return account_name;
        }

        public void setAccount_name(String account_name) {
            this.account_name = account_name;
        }
    }
}
