package com.example.leon.article.api.bean;

/**
 * Created by Administrator on 2017/6/28.
 */

public class VideoInfoBean extends ApiBean{
    /**
     * code : 1
     * msg :
     * data : {"id":"4","title":"测试测试","img":"/upload/vimg/d1347191394ff1753d2d7b258f2f0bcd.jpg","content":"哈哈哈","video":"/upload/video/b54b6d45655bdf346e9b808f5e7932dc.mp4","addtime":"2017-06-27 21:17","review":"0","money":"0.00","reason":null,"mname":"Test01"}
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
         * id : 4
         * title : 测试测试
         * img : /upload/vimg/d1347191394ff1753d2d7b258f2f0bcd.jpg
         * content : 哈哈哈
         * video : /upload/video/b54b6d45655bdf346e9b808f5e7932dc.mp4
         * addtime : 2017-06-27 21:17
         * review : 0
         * money : 0.00
         * reason : null
         * mname : Test01
         */

        private String id;
        private String title;
        private String img;
        private String content;
        private String video;
        private String addtime;
        private String review;
        private String money;
        private Object reason;
        private String mname;

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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }
    }
}
