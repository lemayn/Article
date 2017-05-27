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

import com.example.leon.article.Activity.art.ArtConstant;
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
        ifShowArticle();
        initEvent();
    }

    private void initEvent() {
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationview.setSelectedItemId(R.id.menu_home);
                        break;
                    case 1:
                        navigationview.setSelectedItemId(R.id.menu_article);
                        break;
                    case 2:
                        navigationview.setSelectedItemId(R.id.menu_vip);
                        break;
                    case 3:
                        navigationview.setSelectedItemId(R.id.menu_more);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //发表文章后跳转到当前页面
    private void ifShowArticle() {
        int position = getIntent().getIntExtra(ArtConstant.SHOW_ARTICLEFRAGMENT, 0);
        viewpager.setCurrentItem(position);
        if (position == 0) {
            navigationview.setSelectedItemId(R.id.menu_home);
        }
        if (position == 1){
            navigationview.setSelectedItemId(R.id.menu_article);
        }
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
                return false;
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

    }

    private void initfragments() {

        mFragments.add(new HomeFragment());
        mFragments.add(new ArticleFragment());
        mFragments.add(new VipFragment());
        mFragments.add(new MoreFragment());

    }

    public void gotoArticle() {
        viewpager.setCurrentItem(1);
        navigationview.setSelectedItemId(R.id.menu_article);
    }

}
