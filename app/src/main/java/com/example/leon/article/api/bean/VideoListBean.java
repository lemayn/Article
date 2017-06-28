package com.example.leon.article.api.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */

public class VideoListBean extends ApiBean{

    /**
     * code : 1
     * msg :
     * data : {"video":[{"id":"4","title":"测试测试","review":"0","money":"0.00","reason":null,"addtime":"2017-06-27 21:17","img":"/upload/vimg/d1347191394ff1753d2d7b258f2f0bcd.jpg"},{"id":"3","title":"hahahah","review":"0","money":"0.00","reason":null,"addtime":"2017-06-27 16:50","img":"/upload/vimg/d4578ccec0c3312038ad4800e936378c.jpg"}],"totalpage":1,"page":"1"}
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
         * video : [{"id":"4","title":"测试测试","review":"0","money":"0.00","reason":null,"addtime":"2017-06-27 21:17","img":"/upload/vimg/d1347191394ff1753d2d7b258f2f0bcd.jpg"},{"id":"3","title":"hahahah","review":"0","money":"0.00","reason":null,"addtime":"2017-06-27 16:50","img":"/upload/vimg/d4578ccec0c3312038ad4800e936378c.jpg"}]
         * totalpage : 1
         * page : 1
         */

        private int totalpage;
        private String page;
        private List<VideoBean> video;

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

        public List<VideoBean> getVideo() {
            return video;
        }

        public void setVideo(List<VideoBean> video) {
            this.video = video;
        }

        public static class VideoBean {
            /**
             * id : 4
             * title : 测试测试
             * review : 0
             * money : 0.00
             * reason : null
             * addtime : 2017-06-27 21:17
             * img : /upload/vimg/d1347191394ff1753d2d7b258f2f0bcd.jpg
             */

            private String id;
            private String title;
            private String review;
            private String money;
            private Object reason;
            private String addtime;
            private String img;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getReview() {
                return review;
            }

            public void setReview(String review) {
                this.review = review;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public Object getReason() {
                return reason;
            }

            public void setReason(Object reason) {
                this.reason = reason;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
