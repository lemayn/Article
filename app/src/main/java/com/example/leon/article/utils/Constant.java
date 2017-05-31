package com.example.leon.article.utils;

public class Constant {

    public static class Api {
        /**
         * url
         */
        public static final String BASE_URL = "http://118.89.233.35:8989/";

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

    }

    public static class Share_prf {
        /**
         * 登录返回数据，内包含用户昵称、ID、头像等信息
         */
        public static final String LOGIN_RESPONSE = "login_response";
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
    }

    public static class Intent_Extra {
        /**
         * 是否强制登录
         */
        public static final String IS_CONSTRAINT_LOIN = "is_constraint_loin";
    }
}
