package com.example.leon.article.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leon.article.R;
import com.example.leon.article.bean.NoticeBean;
import com.zzhoujay.richtext.RichText;

import java.util.List;

/**
 * Created by leonseven on 2017/5/31.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private Context context;
    List<NoticeBean.DataBean> beanlsit;

    public NoticeAdapter(List<NoticeBean.DataBean> beanlsit, Context context) {
        this.beanlsit = beanlsit;
        this.context = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView notice_title;
        public TextView notice_date;
        public TextView notice_contect;
        public View line;

        public ViewHolder(View itemView) {
            super(itemView);
            notice_title = (TextView) itemView.findViewById(R.id.notice_title);
            notice_date = (TextView) itemView.findViewById(R.id.notice_date);
            notice_contect = (TextView) itemView.findViewById(R.id.notice_contect);
            line = (View) itemView.findViewById(R.id.view_line);
        }
    }

    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoticeAdapter.ViewHolder holder, int position) {
        if (beanlsit.size()>0){

            String a = beanlsit.get(position).getNtitle();

            holder.notice_title.setText(beanlsit.get(position).getNtitle()+a);
            holder.notice_date.setText(beanlsit.get(position).getNaddtime());
            String contect = beanlsit.get(position).getNcontent();
            RichText.fromHtml(contect)
                    .into(holder.notice_contect);

            if (position==0){
                holder.line.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return beanlsit.size();
    }
}
