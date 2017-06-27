package com.example.leon.article.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leon.article.Activity.art.EditorActivity;
import com.example.leon.article.Activity.video.UpVideoActivity;
import com.example.leon.article.R;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment implements View.OnClickListener{

    private MaterialSpinner mSpinner;
    private List<Fragment> fragments = new ArrayList<>();
    private int currentTabIndex = 0;
    private int currentIndex = -1;
    private TextView tv_articleList,tv_videoList;
    private List<TextView> tvList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article, container, false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    private void initEvent() {
        tv_articleList.setOnClickListener(this);
        tv_videoList.setOnClickListener(this);
    }

    private void initView(View view) {
        tv_articleList = (TextView) view.findViewById(R.id.tv_articleList);
        tv_videoList = (TextView) view.findViewById(R.id.tv_videolist);
        initSpinner(view);
        initfragment();
        tvList.add(tv_articleList);
        tvList.add(tv_videoList);
        switchTag(0);
        //初始化presenter
    }

    private void initfragment() {
        fragments.add(new ArticleListFragment());
        fragments.add(new VideoListFragment());
        //默认加载第一个Fragment
        getChildFragmentManager().beginTransaction().add(R.id.fragment_contain_classify,fragments.get(0)).commit();
    }

    private void initSpinner(View view) {
        mSpinner = (MaterialSpinner) view.findViewById(R.id.spinner_classify);
        mSpinner.setItems("发表", "发表文章","发表视频");
        mSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                switch (position) {
                    case 0:

                        break;
                    case 1:  //点击发表文章
                        GoEditorActivity();
                        break;
                    case 2: //点击发表视频
                        GoVideoActivity();
                        break;
                }
            }
        });
    }
    public static String getCookie(){
        return (String) SPUtil.get(Constant.Share_prf.COOKIE, "");
    }

    public static String getSid(){
        return (String) SPUtil.get(Constant.Share_prf.SID, "");
    }

    private void initData() {

    }

    private void GoVideoActivity() {
            Intent intent = new Intent(getContext(), UpVideoActivity.class);
            startActivity(intent);
    }

    private void GoEditorActivity() {
        Intent intent = new Intent(getContext(), EditorActivity.class);
        startActivity(intent);
    }

    public void switchTag(int newIndex){
        if(currentIndex==newIndex){
            return;
        }
        for (int i = 0; i <tvList.size(); i++) {
            tvList.get(i).setTextColor(getResources().getColor(R.color.colorBlack));
        }
        tvList.get(newIndex).setTextColor(getResources().getColor(R.color.toolbar_back));
        currentIndex=newIndex;
    }

    private void switchFragment(int targetTabIndex) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment currentFragment = fragments.get(currentTabIndex);
        Fragment targetFragment = fragments.get(targetTabIndex);
        if (!targetFragment.isAdded()) {
            transaction.hide(currentFragment).add(R.id.fragment_contain_classify, targetFragment);
        }else{
            transaction.hide(currentFragment).show(targetFragment);
        }
        transaction.commit();
        currentTabIndex = targetTabIndex;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_articleList:
                switchTag(0);
                switchFragment(0);
                break;
            case R.id.tv_videolist:
                switchTag(1);
                switchFragment(1);
                break;
        }
    }
}
