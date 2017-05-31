package com.example.leon.article.Activity.bank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leon.article.R;
import com.example.leon.article.api.bean.UserBankBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hu on 2017/5/30.
 */

public class BankSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<UserBankBean.DataBean> banks = new ArrayList<>();

    public BankSettingAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void addBanks(UserBankBean dataBean) {
        banks.addAll(dataBean.getData());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ICardHolder(layoutInflater.inflate(R.layout.adapter_banksetting_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindCardHolder((ICardHolder) holder, position);
    }

    private void bindCardHolder(ICardHolder holder, int position) {

        if (banks.size() > 0 && banks!= null) {
            String bank = banks.get(position).getBank();
            String bankCard = banks.get(position).getCard();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bankCard.length(); i++) {
                char c = bankCard.charAt(i);
                if (i <= bankCard.length() - 7) {
                    sb.append("*");
                } else {
                    sb.append(c);
                }
            }
            String s = sb.toString();
            holder.tv_bankName.setText(bank);
            holder.tv_bankCard.setText(s);
            //设置银行图片
            switch (bank) {
                case BankConstant.CHINESE_BANK:
                    holder.iv_bankIcon.setImageResource(R.drawable.chinesebank);
                    break;
                case BankConstant.Merchants_BANK:
                    holder.iv_bankIcon.setImageResource(R.drawable.merchantsbank);
                    break;
                case BankConstant.Agricultural_BANK:
                    holder.iv_bankIcon.setImageResource(R.drawable.agriculturalbank);
                    break;
                case BankConstant.ICBC:
                    holder.iv_bankIcon.setImageResource(R.drawable.icbc);
                    break;
                case BankConstant.Construction_BANK:
                    holder.iv_bankIcon.setImageResource(R.drawable.constructionbank);
                    break;
                case BankConstant.Communications_BANK:
                    holder.iv_bankIcon.setImageResource(R.drawable.communicationsbank);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return banks.size();
    }


    private class ICardHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_bankIcon;
        private final TextView tv_bankName;
        private final TextView tv_bankCard;

        public ICardHolder(View itemView) {
            super(itemView);
            iv_bankIcon = (ImageView) itemView.findViewById(R.id.iv_banksetting_bankIcon);
            tv_bankName = (TextView) itemView.findViewById(R.id.tv_banksetting_bankName);
            tv_bankCard = (TextView) itemView.findViewById(R.id.tv_banksetting_bankCard);
        }
    }
}
