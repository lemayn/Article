package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.leon.article.Activity.art.ArticleActivity;

import com.example.leon.article.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        getViewById(R.id.bt_zouni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                startActivity(intent);
            }
        });
    }

    private <T extends View> T getViewById(int res){
        return (T) findViewById(res);
    }

}
