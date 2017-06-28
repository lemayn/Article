package com.example.leon.article.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.leon.article.Activity.art.EditorActivity;
import com.example.leon.article.Activity.video.UpVideoActivity;
import com.example.leon.article.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

/**
 * Created by Administrator on 2017/6/28.
 */

public class PublishFragment extends Fragment implements View.OnClickListener {

    private MaterialSpinner mSpinner;
    private ImageView iv_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        iv_back = (ImageView) view.findViewById(R.id.iv_publish_back);
        initSpinner(view);
    }

    private void initSpinner(View view) {
        mSpinner = (MaterialSpinner) view.findViewById(R.id.spinner_publish);
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

    private void initEvent() {
        iv_back.setOnClickListener(this);
    }

    private void GoVideoActivity() {
        Intent intent = new Intent(getContext(), UpVideoActivity.class);
        startActivity(intent);
    }

    private void GoEditorActivity() {
        Intent intent = new Intent(getContext(), EditorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_publish_back:

                break;
        }
    }
}
