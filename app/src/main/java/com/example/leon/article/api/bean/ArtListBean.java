package com.example.leon.article.api.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public class ArtListBean extends ApiBean{

    /**
     * code : 1
     * msg :
     * data : {"article":[{"aid":"20","atitle":"测试测试","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-25 15:40"},{"aid":"19","atitle":"接着测试","review":"1","amoney":"0","reason":"","aaddtime":"2017-05-24 09:06"},{"aid":"18","atitle":"又来测试啦","review":"2","amoney":"0","reason":"","aaddtime":"2017-05-23 14:15"},{"aid":"17","atitle":"测试测试","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 14:10"},{"aid":"16","atitle":"123132","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 13:24"},{"aid":"15","atitle":"123456","review":"1","amoney":"0","reason":"","aaddtime":"2017-05-23 13:19"},{"aid":"14","atitle":"asdasda","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 12:03"},{"aid":"13","atitle":"吃饭吃饭","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 12:01"},{"aid":"12","atitle":"《欢乐颂2》（五）：其实，人活着不必向世俗妥协","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:57"},{"aid":"11","atitle":"那些永远记不住的单词｜Collusion                 【大大大】","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:52"},{"aid":"9","atitle":"那些永远记不住的单词｜Collusion\t勾结共谋","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:48"},{"aid":"10","atitle":"江湖最后一位笑星（二十二）","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:48"},{"aid":"8","atitle":"12313213456465","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:45"},{"aid":"7","atitle":"1123114654654","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:44"},{"aid":"6","atitle":"6666666666666666666666","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:38"},{"aid":"5","atitle":"测试标题","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:02"},{"aid":"4","atitle":"【出土文物】·\"quot;爱的快递\"quot;-","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 10:59"},{"aid":"3","atitle":"test\ttitle","review":"1","amoney":"10","reason":"","aaddtime":"2017-05-22 09:51"},{"aid":"2","atitle":"我是一个Title","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-20 14:05"}],"totalpage":1,"page":"1"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * article : [{"aid":"20","atitle":"测试测试","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-25 15:40"},{"aid":"19","atitle":"接着测试","review":"1","amoney":"0","reason":"","aaddtime":"2017-05-24 09:06"},{"aid":"18","atitle":"又来测试啦","review":"2","amoney":"0","reason":"","aaddtime":"2017-05-23 14:15"},{"aid":"17","atitle":"测试测试","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 14:10"},{"aid":"16","atitle":"123132","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 13:24"},{"aid":"15","atitle":"123456","review":"1","amoney":"0","reason":"","aaddtime":"2017-05-23 13:19"},{"aid":"14","atitle":"asdasda","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 12:03"},{"aid":"13","atitle":"吃饭吃饭","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 12:01"},{"aid":"12","atitle":"《欢乐颂2》（五）：其实，人活着不必向世俗妥协","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:57"},{"aid":"11","atitle":"那些永远记不住的单词｜Collusion                 【大大大】","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:52"},{"aid":"9","atitle":"那些永远记不住的单词｜Collusion\t勾结共谋","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:48"},{"aid":"10","atitle":"江湖最后一位笑星（二十二）","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:48"},{"aid":"8","atitle":"12313213456465","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:45"},{"aid":"7","atitle":"1123114654654","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:44"},{"aid":"6","atitle":"6666666666666666666666","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:38"},{"aid":"5","atitle":"测试标题","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 11:02"},{"aid":"4","atitle":"【出土文物】·\"quot;爱的快递\"quot;-","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-23 10:59"},{"aid":"3","atitle":"test\ttitle","review":"1","amoney":"10","reason":"","aaddtime":"2017-05-22 09:51"},{"aid":"2","atitle":"我是一个Title","review":"0","amoney":"0","reason":null,"aaddtime":"2017-05-20 14:05"}]
         * totalpage : 1
         * page : 1
         */

        private int totalpage;
        private String page;
        private List<ArticleBean> article;

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

        public List<ArticleBean> getArticle() {
            return article;
        }

        public void setArticle(List<ArticleBean> article) {
            this.article = article;
        }

        public static class ArticleBean {
            /**
             * aid : 20
             * atitle : 测试测试
             * review : 0
             * amoney : 0
             * reason : null
             * aaddtime : 2017-05-25 15:40
             */

            private String aid;
            private String atitle;
            private String review;
            private String amoney;
            private Object reason;
            private String aaddtime;

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

            public String getReview() {
                return review;
            }

            public void setReview(String review) {
                this.review = review;
            }

            public String getAmoney() {
                return amoney;
            }

            public void setAmoney(String amoney) {
                this.amoney = amoney;
            }

            public Object getReason() {
                return reason;
            }

            public void setReason(Object reason) {
                this.reason = reason;
            }

            public String getAaddtime() {
                return aaddtime;
            }

            public void setAaddtime(String aaddtime) {
                this.aaddtime = aaddtime;
            }
        }
    }
}
