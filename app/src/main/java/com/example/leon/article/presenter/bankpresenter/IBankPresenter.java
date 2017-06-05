package com.example.leon.article.presenter.bankpresenter;

/**
 * Created by Hu on 2017/5/30.
 */

public interface IBankPresenter {

    //获取银行列表信息
    void getBankConfig(String cookie, String sid);

    //绑定用户银行卡
    void bindBankCard(String cookie, String bid, String card, String sid,
                      String account_name, String address,String password);

    //获取用户绑定的银行卡信息
    void getUserBankInfo(String cookie, String sid);
}
