package com.example.leon.article.Activity.art;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.leon.article.R;
import com.example.leon.article.adapter.SQLListAdapter;
import com.example.leon.article.sql.bean.Arts;
import com.example.leon.article.sql.dao.ArtDao;

import java.util.Collections;
import java.util.List;

public class MySqlActivity extends AppCompatActivity {

    private ListView mListView;
    private List<Arts> artsList;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (artsList.size() > 0 && artsList != null) {
                    String title = artsList.get(position).getTitle();
                    String content = artsList.get(position).getContent();
                    String imgPath = artsList.get(position).getImgPath();
                    Long artid = artsList.get(position).getId();
                    Intent intent = new Intent(MySqlActivity.this,EditorActivity.class);
                    intent.putExtra(ArtConstant.ART_TITLE,title);
                    intent.putExtra(ArtConstant.ART_CONTENT,content);
                    intent.putExtra(ArtConstant.ART_IMGPATH,imgPath);
                    intent.putExtra(ArtConstant.ART_ID,artid);
                    startActivity(intent);
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        setTitle(getString(R.string.myarticle));
        mListView = getViewById(R.id.lv_sql);
        mListView.setEmptyView(getViewById(R.id.lv_sql_empty));
        iv_back = getViewById(R.id.iv_sql_back);
    }

    private void initData() {
        artsList = ArtDao.queryArts();
        Collections.reverse(artsList);//将集合中的元素倒叙，实现最新编写的排列在最上面
        if (!artsList.isEmpty()) {
            mListView.setAdapter(new SQLListAdapter(MySqlActivity.this, artsList));
        }
    }

    private <T extends View> T getViewById(int res){
        return (T) findViewById(res);
    }
}
