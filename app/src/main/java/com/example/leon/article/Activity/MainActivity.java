package com.example.leon.article.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.leon.article.R;
import com.example.leon.article.fragment.ArticleFragment;
import com.example.leon.article.fragment.HomeFragment;
import com.example.leon.article.fragment.MoreFragment;
import com.example.leon.article.fragment.VipFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ViewPager viewpager;
    private BottomNavigationView navigationview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        hideHeaderInfo();
        //        hideHeaderMoneyInfo();
        initView();
    }

    private void initView() {

        initfragments();
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        navigationview = (BottomNavigationView) findViewById(R.id.navigationview);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        viewpager.setAdapter(mAdapter);

        //禁止ViewPager滑动
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        viewpager.setOffscreenPageLimit(4);
        navigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.menu_article:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.menu_vip:
                        viewpager.setCurrentItem(2);
                        break;
                    case R.id.menu_more:
                        viewpager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });


        //        getViewById(R.id.bt_zouni).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
        //                startActivity(intent);
        //            }
        //        });
    }

    private void initfragments() {

        mFragments.add(new HomeFragment());
        mFragments.add(new ArticleFragment());
        mFragments.add(new VipFragment());
        mFragments.add(new MoreFragment());

    }


    private <T extends View> T getViewById(int res) {
        return (T) findViewById(res);
    }

    public void gotoArticle() {
        viewpager.setCurrentItem(1);
        navigationview.setSelectedItemId(R.id.menu_article);
    }

}
