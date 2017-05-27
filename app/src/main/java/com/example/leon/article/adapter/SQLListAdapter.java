package com.example.leon.article.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.leon.article.R;
import com.example.leon.article.sql.bean.Arts;
import com.example.leon.article.sql.dao.ArtDao;
import com.zzhoujay.richtext.RichText;

import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */

public class SQLListAdapter extends BaseSwipeAdapter{

    private Context context;
    private List<Arts> arts;
    private LayoutInflater layoutInflater;

    public SQLListAdapter(Context context, List<Arts> arts) {
        this.context = context;
        this.arts = arts;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.adapter_artlist, null);
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(position));
        //点击删除图片
        view.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtDao.deleteArts(arts.get(position));
                arts.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_art_list_content);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_art_list_title);
        TextView tv_createTime = (TextView) convertView.findViewById(R.id.tv_art_list_createTime);
        Arts arts = this.arts.get(position);
        if (arts.getTitle() != null) {
            tv_title.setText(arts.getTitle());
        }
        if (arts.getTime() != null) {
            tv_createTime.setText(arts.getTime());
        }
        RichText.fromHtml(arts.getContent())
                .into(tv_content);
    }

    @Override
    public int getCount() {
        return arts.size();
    }

    @Override
    public Object getItem(int position) {
        return arts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
