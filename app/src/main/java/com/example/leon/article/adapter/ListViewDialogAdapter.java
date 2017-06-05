package com.example.leon.article.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leon.article.Activity.bank.BankConstant;
import com.example.leon.article.R;
import com.example.leon.article.api.bean.BankConfigBean;

import java.util.List;


/**
 * Created by Administrator on 2017/6/5.
 */

public class ListViewDialogAdapter extends BaseAdapter{

    private Context context;
    private List<BankConfigBean.DataBean> banks;
    private LayoutInflater layoutInflater;

    public ListViewDialogAdapter(Context context,List<BankConfigBean.DataBean> banks) {
        this.context = context;
        this.banks = banks;
        this.layoutInflater = LayoutInflater.from(context);
    }

    //添加数据到集合中
    public void addItems(BankConfigBean.DataBean date){
        banks.add(date);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return banks.size();
    }

    @Override
    public Object getItem(int position) {
        return banks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_listview_dialog,parent,false);
        }
        ListDialogHolder holder = getHolder(convertView);
        holder.tv_name.setText(banks.get(position).getBank());
        Glide.with(context)
                .load(BankConstant.BankIconUrl+banks.get(position).getBimg())
                .into(holder.ivIcon);
        return convertView;
    }

    private ListDialogHolder getHolder(View view) {
        ListDialogHolder holder = (ListDialogHolder) view.getTag();
        if (holder == null) {
            holder = new ListDialogHolder(view);
            view.setTag(holder);
        }
        return holder;
    }


    private class ListDialogHolder{
        private final TextView tv_name;
        private final ImageView ivIcon;

        public ListDialogHolder(View view) {
            ivIcon = (ImageView) view.findViewById(R.id.iv_lvDialog_bankIcon);
            tv_name = (TextView) view.findViewById(R.id.tv_lvDialog_bankName);
        }
    }

}
