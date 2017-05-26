package com.example.leon.article.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leon.article.R;

/**
 * Created by leonseven on 2017/5/25.
 */

public class VipFragment extends Fragment {

    View view = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_vipcenter, container, false);

        return view;
    }
}
