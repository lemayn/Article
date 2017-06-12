package com.example.leon.article.adapter;

import android.view.ViewGroup;

import com.example.leon.article.R;
import com.example.leon.article.api.bean.StatementBean;
import com.example.leon.article.base.BaseRecyclerViewAdapter;
import com.example.leon.article.base.BaseRecyclerViewHolder;
import com.example.leon.article.databinding.ItemAccountStatementBinding;

import java.util.List;

public class AccountStatementAdapter extends BaseRecyclerViewAdapter<StatementBean.DataBean.MoneyConfig> {

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AccountStatementViewHolder(parent, R.layout.item_account_statement);
    }

    public static class AccountStatementViewHolder extends BaseRecyclerViewHolder<StatementBean.DataBean.MoneyConfig,
            ItemAccountStatementBinding> {

        public AccountStatementViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(StatementBean.DataBean.MoneyConfig config, int position) {
            binding.setConfig(config);
        }
    }

    public void addStatement(List<StatementBean.DataBean.MoneyConfig> configs, boolean isClear) {
        if (isClear) {
            data.clear();
        }
        data.addAll(configs);
        notifyDataSetChanged();
    }
}
