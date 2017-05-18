package com.example.leon.article.Activity.art;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.leon.article.R;
import com.example.leon.article.adapter.ArtListAdapter;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityMySqlBinding;
import com.example.leon.article.sql.bean.Arts;
import com.example.leon.article.sql.dao.ArtDao;

import java.util.Collections;
import java.util.List;

public class MySqlActivity extends ToolBarBaseActivity<ActivityMySqlBinding> {

    private ListView mListView;
    private List<Arts> artsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (artsList.size() > 0 && artsList != null) {
                    String title = artsList.get(position).getTitle();
                    String content = artsList.get(position).getContent();
                    Intent intent = new Intent(MySqlActivity.this,EditorActivity.class);
                    intent.putExtra(ArtConstant.ART_TITLE,title);
                    intent.putExtra(ArtConstant.ART_CONTENT,content);
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {
        hideHeaderMoneyInfo();//隐藏钱包布局
        setTitle(getString(R.string.myarticle));
        mListView = getViewById(R.id.lv_sql);
        mListView.setEmptyView(getViewById(R.id.lv_sql_empty));
    }

    private void initData() {
        artsList = ArtDao.queryArts();
        Collections.reverse(artsList);//将集合中的元素倒叙，实现最新编写的排列在最上面
        if (!artsList.isEmpty()) {
            mListView.setAdapter(new ArtListAdapter(MySqlActivity.this, artsList));
        }
    }

    private <T extends View> T getViewById(int res){
        return (T) findViewById(res);
    }
}
