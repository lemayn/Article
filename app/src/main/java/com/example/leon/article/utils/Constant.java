package com.example.leon.article.utils;

public class Constant {

    public static class Api {
        /**
         * url
         */
        //        public static final String BASE_URL = "http://118.89.233.35:8989/"; 测试服务器
        public static final String BASE_URL = "http://121.127.226.160/";//正式服务器

        /**
         * 接口验证
         */
        public static final String URL_KEY = "55a50c1a06f9c1032014112cbd68f34b";

        /**
         * 注册接口
         */
        public static final String REG = "Reg";
        /**
         * 登录接口
         */
        public static final String LOGIN = "Login";
        /**
         * 修改密码
         */
        public static final String EDIT_PASSWORD = "EditPassword";
        /**
         * 退出登录
         */
        public static final String LOGOUT = "Logout";
        /**
         * 获取用户信息
         */
        public static final String USER_DATA = "UserData";
        /**
         * 修改基本资料
         */
        public static final String USER_INFO_EDIT = "UserInfoEdit";
        /**
         * 文章上传
         */
        public static final String ART_ADD = "ArtAdd";
        /**
         * 获取用户自己上传所有文章
         */
        public static final String ARTICLE = "Article";
        /**
         * 获取单篇文章详情
         */
        public static final String ART_INFO = "ArtInfo";
        /**
         * 获取银行列表
         */
        public static final String BANK_CONFIG = "BankConfig";
        /**
         * 绑定银行卡
         */
        public static final String BANS_SET_ADD = "BanksSetAdd";
        /**
         * 获取绑定银行卡信息
         */
        public static final String USER_BANK = "UserBank";
        /**
         * 提现
         */
        public static final String WITHDRAW_MONEY = "WithdrawMoney";
        /**
         * 修改提现密码
         */
        public static final String EDIT_MONEY_PASSWORD = "EditMoneyPassword";
        /**
         * 广告接口
         */
        public static final String AD = "Ad";
        /**
         * 手机验证接口
         */
        public static final String TELL_TEST = "TellTest";


        /**
         * 验证结果接口
         */
        public static final String TELL_RES = "TellRes";

        /**
         * 我的账单接口
         */
        public static final String MONEY_CONFIG = "MoneyConfig";

        /**
         * 在线客服接口
         */
        public static final String KEFU = "Kefu";

        /**
         * 软件说明接口
         */
        public static final String ABOOUT_IUS = BASE_URL + "api/about.php";

        /**
         * 软件更新
         */
        public static final String UPGRADE = "Upgrade";

    }

    public static class Share_prf {
        /**
         * cookie
         */
        public static final String COOKIE = "cookie";
        /**
         * sid
         */
        public static final String SID = "sid";
        /**
         * pwd
         */
        public static final String PWD = "pwd";
        /**
         * name
         */
        public static final String NAME = "name";
        /**
         * 用户信息
         */
        public static final String USER_DATA = "user_data";

        /**
         * 上次使用app的时间
         */
        public static final String LAST_USING_TIME = "last_using_time";
    }

    public static class Intent_Extra {
        /**
         * 是否强制登录
         */
        public static final String IS_CONSTRAINT_LOIN = "is_constraint_loin";

        /**
         * 跳转标识
         */
        public static final String MORE_INFO_TYPE = "more_info_type";

        /**
         * 关于我们
         */
        public static final int INTRO = 0;

        /**
         * 在线客服
         */
        public static final int SERVICE = 1;
        /**
         * apk更新链接
         */
        public static final String VERSION_URL = "version_url";
    }
}
