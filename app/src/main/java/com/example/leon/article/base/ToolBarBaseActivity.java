package com.example.leon.article.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.leon.article.R;
import com.example.leon.article.databinding.ActivityToolBarBaseBinding;

public class ToolBarBaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    protected T binding;
    protected ActivityToolBarBaseBinding baseBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        baseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_tool_bar_base, null, false);
        binding = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        binding.getRoot().setLayoutParams(params);
        FrameLayout mContainer = (FrameLayout) baseBinding.getRoot().findViewById(R.id.container);
        mContainer.addView(binding.getRoot());
        getWindow().setContentView(baseBinding.getRoot());

        setToolBar();
    }

    private void setToolBar() {
        setSupportActionBar(baseBinding.toolbar);
        //设置不显示自带的 Title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setTitle(CharSequence title) {
        baseBinding.tvToolbarTitle.setText(title);
    }

    public void setNavigationView() {
        baseBinding.ivToolbarBack.setVisibility(View.VISIBLE);
        baseBinding.ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void hideNavigationView() {
        baseBinding.ivToolbarBack.setVisibility(View.GONE);
    }

    public void hideHeaderInfo() {
        baseBinding.llHeaderInfo.setVisibility(View.GONE);
    }

    public void hideHeaderMoneyInfo(){
        baseBinding.llHeaderMoney.setVisibility(View.GONE);
    }

}
