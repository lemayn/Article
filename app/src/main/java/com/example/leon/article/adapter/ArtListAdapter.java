package com.example.leon.article.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leon.article.Activity.art.ArtConstant;
import com.example.leon.article.Activity.art.ArtDetailActivity;
import com.example.leon.article.R;
import com.example.leon.article.api.bean.ArtListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ArtListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ArtListBean.DataBean.ArticleBean> items = new ArrayList<>();

    public ArtListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void addItems(List<ArtListBean.DataBean.ArticleBean> bean){
        items.addAll(bean);
        notifyDataSetChanged();
    }

    public void clearDate(){
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_issuelist, parent, false);
        }
        IssueItemHolder holder = getHolder(convertView);
        String review = items.get(position).getReview();
        switch (review) {
            case "0"://未审核 red
                holder.tv_issue_status.setTextColor(Color.RED);
                holder.tv_issue_time.setText(context.getString(R.string.in_review));
                break;
            case "1"://审核通过 green
                holder.tv_issue_status.setTextColor(Color.GREEN);
                holder.tv_issue_status.setText(context.getString(R.string.published));
                break;
            case "2"://审核未通过 gray
                holder.tv_issue_status.setTextColor(Color.GRAY);
                holder.tv_issue_status.setText(context.getString(R.string.notpass));
                break;
        }


        holder.tv_issue_content.setText(items.get(position).getAtitle());
        holder.tv_issue_time.setText(items.get(position).getAaddtime());
        /*holder.tv_issue_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goArtDetailActivity(position);
            }
        });
        holder.tv_issue_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goArtDetailActivity(position);
            }
        });*/
        holder.ll_artList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goArtDetailActivity(position);
            }
        });
        return convertView;
    }

    private void goArtDetailActivity(int position) {
        if (items.get(position).getAid() != null) {
            Intent intent = new Intent(context, ArtDetailActivity.class);
            intent.putExtra(ArtConstant.DETAIL_AID,items.get(position).getAid());
            context.startActivity(intent);
        }
    }

    private IssueItemHolder getHolder(View view) {
        IssueItemHolder holder = (IssueItemHolder) view.getTag();
        if (holder == null) {
            holder = new IssueItemHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    class IssueItemHolder {
        private LinearLayout ll_artList;
        private final TextView tv_issue_time;
        private final TextView tv_issue_content;
        private final TextView tv_issue_status;

        public IssueItemHolder(View view) {
            tv_issue_content = (TextView) view.findViewById(R.id.tv_issue_content);
            tv_issue_time = (TextView) view.findViewById(R.id.tv_issue_time);
            tv_issue_status = (TextView) view.findViewById(R.id.tv_issue_status);
            ll_artList = (LinearLayout) view.findViewById(R.id.ll_adapter_artlist);
        }
    }

}
