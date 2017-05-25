package com.example.leon.article.api.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.leon.article.BR;

import java.util.List;

public class BankApiBean extends ApiBean {

    private List<DataBean> data;

    @Bindable
    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }

    public static class DataBean extends BaseObservable {

        private String cid;
        private String bank;
        private String card;
        private String account_name;

        @Bindable
        public String getAccount_name() {
            return account_name;
        }

        public void setAccount_name(String account_name) {
            this.account_name = account_name;
            notifyPropertyChanged(BR.account_name);
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        @Bindable
        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
            notifyPropertyChanged(BR.bank);
        }

        @Bindable
        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
            notifyPropertyChanged(BR.card);
        }
    }
}
