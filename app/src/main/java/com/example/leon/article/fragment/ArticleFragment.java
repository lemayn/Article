package com.example.leon.article.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leon.article.Activity.art.EditorActivity;
import com.example.leon.article.Activity.art.MySqlActivity;
import com.example.leon.article.R;
import com.example.leon.article.adapter.IssueListAdapter;

public class ArticleFragment extends Fragment implements View.OnClickListener{

    View view = null;

    private TextView tv_myArt;
    private TextView tv_createArt;
    private ListView lv_article;
    private TextView lv_empty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_article, container, false);
        initView();
        //从服务器获取数据
        initDate();

        initEvent();

        return view;
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_article);
//
//        initView();
//        //从服务器获取数据
//        initDate();
//
//        initEvent();
//    }

    private void initEvent() {
        tv_myArt.setOnClickListener(this);
        tv_createArt.setOnClickListener(this);
        lv_article.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"点击了第"+position+"个位置",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDate() {
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.listview_article_headerview, null);
        lv_article.addHeaderView(headerView);

        IssueListAdapter adapter = new IssueListAdapter(getActivity());
        lv_article.setAdapter(adapter);


    }

    private void initView() {
        lv_empty = (TextView) view.findViewById(R.id.lv_article_empty);
        lv_article = (ListView) view.findViewById(R.id.lv_article);
        lv_article.setEmptyView(lv_empty);
        tv_myArt = (TextView) view.findViewById(R.id.tv_myArt);
        tv_createArt = (TextView) view.findViewById(R.id.tv_createArt);
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
        Intent intent = new Intent(getActivity(),EditorActivity.class);
        startActivity(intent);
    }

    private void goMySqlActivity() {
        Intent intent = new Intent(getActivity(),MySqlActivity.class);
        startActivity(intent);
    }

}
