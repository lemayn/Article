package com.example.leon.article.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leon.article.Activity.CustomerServiceActivity;
import com.example.leon.article.Activity.MoreInfoActivity;
import com.example.leon.article.Activity.NoticeActivity;
import com.example.leon.article.R;

/**
 * Created by leonseven on 2017/5/25.
 */

public class MoreFragment extends Fragment implements View.OnClickListener {

    View view = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_more, container, false);

        RelativeLayout rl_notice = (RelativeLayout) view.findViewById(R.id.rl_notice);
        TextView notice_title = (TextView) rl_notice.findViewById(R.id.tv_title);
        ImageView notice_icon = (ImageView) rl_notice.findViewById(R.id.iv_icon);
        notice_title.setText(R.string.system_notice);
        notice_icon.setVisibility(View.GONE);
        rl_notice.setOnClickListener(this);

        RelativeLayout rl_intro = (RelativeLayout) view.findViewById(R.id.rl_intro);
        TextView intro_title = (TextView) rl_intro.findViewById(R.id.tv_title);
        ImageView intro_icon = (ImageView) rl_intro.findViewById(R.id.iv_icon);
        intro_title.setText(R.string.app_intro);
        intro_icon.setVisibility(View.GONE);
        rl_intro.setOnClickListener(this);

        RelativeLayout rl_service = (RelativeLayout) view.findViewById(R.id.rl_service);
        TextView service_title = (TextView) rl_service.findViewById(R.id.tv_title);
        ImageView service_icon = (ImageView) rl_service.findViewById(R.id.iv_icon);
        service_title.setText(R.string.customer_service);
        service_icon.setVisibility(View.GONE);
        rl_service.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_notice:
                startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
            case R.id.rl_intro:
                startActivity(new Intent(getActivity(), MoreInfoActivity.class));
                break;
            case R.id.rl_service:
                startActivity(new Intent(getActivity(), CustomerServiceActivity.class));
                break;
        }
    }
}
