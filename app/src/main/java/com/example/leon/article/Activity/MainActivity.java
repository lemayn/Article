package com.example.leon.article.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.leon.article.Activity.art.ArtConstant;
import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.databinding.ActivityMainBinding;
import com.example.leon.article.fragment.ArticleFragment;
import com.example.leon.article.fragment.HomeFragment;
import com.example.leon.article.fragment.MoreFragment;
import com.example.leon.article.fragment.PublishFragment;
import com.example.leon.article.fragment.VipFragment;
import com.example.leon.article.upgrade.DownLoadService;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.SPUtil;

import java.util.ArrayList;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ViewPager viewpager;
    private String version_url;
    private ActivityMainBinding binding;
    private ArticleFragment articleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Long lastUsingTime = (Long) SPUtil.get(Constant.Share_prf.LAST_USING_TIME, 0L);
        long currentTimeMillis = System.currentTimeMillis();
        if (lastUsingTime != 0 && currentTimeMillis - lastUsingTime > 24 * 60 * 60 * 1000) {
            SPUtil.clear();
        }
        //记录上次使用时间
        SPUtil.put(Constant.Share_prf.LAST_USING_TIME, currentTimeMillis);

        initView();
        ifShowArticle();
        initEvent();
        isUpgrade();
    }

    private void initEvent() {
        binding.home.setOnClickListener(this);
        binding.article.setOnClickListener(this);
        binding.vip.setOnClickListener(this);
        binding.more.setOnClickListener(this);
        binding.fabu.setOnClickListener(this);
        binding.setState(R.id.home);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.setState(R.id.home);
                        break;
                    case 1:
                        binding.setState(R.id.article);
                        break;
                    case 2:
                        binding.setState(R.id.fabu);
                        break;
                    case 3:
                        binding.setState(R.id.vip);
                        break;
                    case 4:
                        binding.setState(R.id.more);
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
        Log.i("HT", "ifShowArticle: " + position);
        viewpager.setCurrentItem(position);
        if (position == 0) {
            binding.setState(R.id.home);
        }
        if (position == 1) {
            binding.setState(R.id.article);
        }
    }


    private void initView() {
        initfragments();
        viewpager = (ViewPager) findViewById(R.id.viewpager);

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

        viewpager.setOffscreenPageLimit(3);
    }

    private void initfragments() {
        articleFragment = new ArticleFragment();
        mFragments.add(new HomeFragment());
        mFragments.add(articleFragment);
        mFragments.add(new PublishFragment());
        mFragments.add(new VipFragment());
        mFragments.add(new MoreFragment());

    }

    public void gotoArticle(int index) {
        viewpager.setCurrentItem(1);
        binding.setState(R.id.article);
        if (index == 4) {
            articleFragment.switchTag(0);
            articleFragment.switchFragment(0);
        } else if (index == 5) {
            articleFragment.switchTag(1);
            articleFragment.switchFragment(1);
        }
    }

    public void gotoFabu() {
        viewpager.setCurrentItem(2);
        binding.setState(R.id.fabu);
    }

    public void gotoHomePage() {
        viewpager.setCurrentItem(0);
        binding.setState(R.id.home);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记录上次使用时间
        SPUtil.put(Constant.Share_prf.LAST_USING_TIME, System.currentTimeMillis());
    }

    private void isUpgrade() {
        ApiFactory.getApi().upgrade(Constant.Api.UPGRADE)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        version_url = apiBean.getData().getVersion_url();
                        whetherUpgrade(Integer.parseInt(apiBean.getData().getVersion_code()),
                                apiBean.getData().getVersion_explain());
                    }
                });
    }

    private void whetherUpgrade(int remoteCode, String version_explain) {
        if (remoteCode > CommonUtils.getVersionCode(this)) {
            showNoticeDialog(version_explain);
        }
    }

    /**
     * 显示更新对话框
     *
     * @param version_explain
     */
    private void showNoticeDialog(String version_explain) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提示");
        builder.setMessage(version_explain);
        // 更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PermissionGen.needPermission(MainActivity.this,
                        654321,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }
        });
        // 稍后更新
        builder.setNegativeButton("以后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 654321)
    private void downloadImage() {
        Intent downloadService = new Intent(MainActivity.this, DownLoadService.class);
        downloadService.putExtra(Constant.Intent_Extra.VERSION_URL, version_url);
        MainActivity.this.startService(downloadService);
    }

    @PermissionFail(requestCode = 654321)
    private void showTip() {
        Toast.makeText(MainActivity.this, "未获取权限，升级失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        binding.setState(v.getId());
        switch (v.getId()) {
            case R.id.home:
                viewpager.setCurrentItem(0, false);
                break;
            case R.id.article:
                viewpager.setCurrentItem(1, false);
                break;
            case R.id.fabu:
                viewpager.setCurrentItem(2, false);
                break;
            case R.id.vip:
                viewpager.setCurrentItem(3, false);
                break;
            case R.id.more:
                viewpager.setCurrentItem(4, false);
                break;
        }
    }
}
