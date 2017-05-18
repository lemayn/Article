package com.example.leon.article.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.app;
import com.example.leon.article.base.BaseRecyclerViewAdapter;
import com.example.leon.article.base.BaseRecyclerViewHolder;
import com.example.leon.article.bean.ItemBean;
import com.example.leon.article.databinding.ItemVipCenterBinding;
import com.example.leon.article.utils.PerfectClickListener;

import java.util.List;


public class VIPCenterAdapter extends BaseRecyclerViewAdapter<ItemBean> implements View.OnClickListener{

    public VIPCenterAdapter(List<ItemBean> list) {
        data.addAll(list);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder" + data.size());
        return new VIPCenterViewHolder(parent, R.layout.item_vip_center);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(data.get((Integer) v.getTag()), (Integer) v.getTag());
        }
    }


    public static class VIPCenterViewHolder extends BaseRecyclerViewHolder<ItemBean, ItemVipCenterBinding> {

        public VIPCenterViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
            System.out.println("VIPCenterViewHolder");
        }

        @Override
        public void onBindViewHolder(final ItemBean itemBean, final int position) {
            System.out.println("onBindViewHolder");
            binding.setItem(itemBean);
            binding.rlItem.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    Intent intent = new Intent(app.getInstance(), itemBean.getClazz());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.getInstance().startActivity(intent);
                    Toast.makeText(app.getInstance(), itemBean.getTitle(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
