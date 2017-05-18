package com.example.leon.article.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.leon.article.R;
import com.example.leon.article.utils.TimeUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class IssueListAdapter extends BaseAdapter{

    private Context context;
    private List<String> arts;
    private LayoutInflater layoutInflater;

    public IssueListAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_issuelist, parent, false);
        }
        IssueItemHolder holder = getHolder(convertView);
        holder.tv_issue_status.setTextColor(Color.RED);
        holder.tv_issue_time.setText(TimeUtils.getStringDateShort());
        return convertView;
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

        private final TextView tv_issue_time;
        private final TextView tv_issue_content;
        private final TextView tv_issue_status;

        public IssueItemHolder(View view) {
            tv_issue_content = (TextView) view.findViewById(R.id.tv_issue_content);
            tv_issue_time = (TextView) view.findViewById(R.id.tv_issue_time);
            tv_issue_status = (TextView) view.findViewById(R.id.tv_issue_status);
        }
    }

}
