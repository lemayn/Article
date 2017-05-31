package com.example.leon.article.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leon.article.R;

/**
 *  首页 子fragment 推荐文章
 * Created by leonseven on 2017/5/26.
 */

public class HomeFragment_item_RecommendFragment extends Fragment{

    View view = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.item_recommendfragment, container, false);

        return view;
    }
}
