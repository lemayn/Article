package com.example.leon.article.api.bean;

import java.util.List;

public class StatementBean extends ApiBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int totalpage;
        private int page;
        private List<MoneyConfig> moneyConfig;

        public int getTotalpage() {
            return totalpage;
        }

        public void setTotalpage(int totalpage) {
            this.totalpage = totalpage;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<MoneyConfig> getMoneyConfig() {
            return moneyConfig;
        }

        public void setMoneyConfig(List<MoneyConfig> moneyConfig) {
            this.moneyConfig = moneyConfig;
        }

        public static class MoneyConfig {
            private String money;
            private int type;
            private String time;
            private String handle;

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getHandle() {
                return handle;
            }

            public void setHandle(String handle) {
                this.handle = handle;
            }
        }
    }
}
