package com.app.personalfinancial.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.personalfinancial.R;
import com.app.personalfinancial.databinding.ActivityMainBinding;
import com.app.personalfinancial.fragment.AddCostFragment;
import com.app.personalfinancial.fragment.AddIncomeFragment;
import com.app.personalfinancial.fragment.ReportFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private MyClickHandlers handlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        handlers = new MyClickHandlers(this);
        mBinding.setHandlers(handlers);
        mBinding.viewPager.setOffscreenPageLimit(1);
        setupViewPager(mBinding.viewPager);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int color = 0;
                if (mBinding.tabLayout.getSelectedTabPosition() == 0) color = R.color.green;
                if (mBinding.tabLayout.getSelectedTabPosition() == 1) color = R.color.red;
                if (mBinding.tabLayout.getSelectedTabPosition() == 2) color = R.color.blue;
                mBinding.appBarLayout.setBackgroundColor(getResources().getColor(color));
                mBinding.tabLayout.setBackgroundColor(getResources().getColor(color));
                mBinding.toolBar.setBackgroundColor(getResources().getColor(color));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class MyClickHandlers {
        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        public void btnLogout(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddIncomeFragment(), "درآمد");
        adapter.addFragment(new AddCostFragment(), "هزینه");
        adapter.addFragment(new ReportFragment(), "گزارش");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}