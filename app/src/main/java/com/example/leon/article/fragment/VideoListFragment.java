package com.example.leon.article.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.leon.article.R;

/**
 * Created by Administrator on 2017/6/27.
 */

public class VideoListFragment extends Fragment{

    private SwipeRefreshLayout refreshVideo;
    private ListView lv_video;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videolist, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        lv_video = (ListView) view.findViewById(R.id.lv_video);
        refreshVideo = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_videolist);

    }
}
