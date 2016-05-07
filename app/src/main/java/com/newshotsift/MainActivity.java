package com.newshotsift;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    PagerSlidingTab indicator;
    ViewPager viewpager;

    private List<FragmentNews> Fragments = new ArrayList<>();
    String[] titles = {"热文", "段子手", "八卦党", "科技宅", "观天下", "铁血谜", "财经谜", "萌宠圈", "正能量"};
    String[] urls = {
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=WyDgzBA#onetab/wxtab1/%E7%83%AD%E6%96%87/001203",
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=Uus25zA#onetab/wxtab3/%E6%AE%B5%E5%AD%90%E6%89%8B/001203",
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=BZ5n0AA#onetab/wxtab2/%E5%85%AB%E5%8D%A6%E5%85%9A/001203",
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=MPKkiDA#onetab/wxtab6/%E7%A7%91%E6%8A%80%E5%AE%85/001203",
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=6xVUCqA#onetab/wxtab10/%E8%A7%82%E5%A4%A9%E4%B8%8B/001203",
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=6CyRjBA#onetab/wxtab11/%E9%93%81%E8%A1%80%E8%BF%B7/001203",
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=fBPszyA#onetab/wxtab7/%E8%B4%A2%E8%BF%B7%E5%9B%A2/001203",
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=iHTsBFA#onetab/wxtab8/%E8%90%8C%E5%AE%A0%E5%9C%88/001203",
            "http://zixun.html5.qq.com/wechathot/?ch=001203&sc_id=3S5EstA#onetab/wxtab4/%E6%AD%A3%E8%83%BD%E9%87%8F/001203"

    };
    FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicator = (PagerSlidingTab) findViewById(R.id.indicator);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        initViewPager();
    }

    private void initViewPager() {
        for (int i = 0; i < titles.length; i++) {
            FragmentNews pager = new FragmentNews(urls[i]);
            Fragments.add(pager);
        }
        viewpager.setOffscreenPageLimit(4);
        //1.给ViewPager填充数据
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mFragmentAdapter);
        //         让指针控件绑定ViewPager
        indicator.setViewPager(viewpager);
    }


    public class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return Fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

    }


}
