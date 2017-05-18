package com.example.leon.article.Activity.art;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.adapter.IssueListAdapter;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_myArt;
    private TextView tv_createArt;
    private ListView lv_article;
    private TextView lv_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initView();
        //从服务器获取数据
        initDate();

        initEvent();
    }

    private void initEvent() {
        tv_myArt.setOnClickListener(this);
        tv_createArt.setOnClickListener(this);
        lv_article.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ArticleActivity.this,"点击了第"+position+"个位置",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDate() {
        View headerView = getLayoutInflater().inflate(R.layout.listview_article_headerview, null);
        lv_article.addHeaderView(headerView);

        IssueListAdapter adapter = new IssueListAdapter(this);
        lv_article.setAdapter(adapter);


    }

    private void initView() {
        lv_empty = (TextView) findViewById(R.id.lv_article_empty);
        lv_article = (ListView) findViewById(R.id.lv_article);
        lv_article.setEmptyView(lv_empty);
        tv_myArt = (TextView) findViewById(R.id.tv_myArt);
        tv_createArt = (TextView) findViewById(R.id.tv_createArt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_myArt:
                goMySqlActivity();
                break;
            case R.id.tv_createArt:
                goEditorActivity();
                break;
        }
    }

    private void goEditorActivity() {
        Intent intent = new Intent(this,EditorActivity.class);
        startActivity(intent);
    }

    private void goMySqlActivity() {
        Intent intent = new Intent(this,MySqlActivity.class);
        startActivity(intent);
    }

}
