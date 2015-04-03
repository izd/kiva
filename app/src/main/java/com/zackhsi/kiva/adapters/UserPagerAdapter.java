package com.zackhsi.kiva.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.zackhsi.kiva.fragments.LoanListViewFragment;
import com.zackhsi.kiva.fragments.UserInfoFragment;
import com.zackhsi.kiva.models.User;

public class UserPagerAdapter extends CacheFragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"About", "Mine", "Favorites"};

    public UserPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    protected Fragment createItem(int i) {
        if (i == 0) {
            return UserInfoFragment.newInstance();
        } else {
            return new LoanListViewFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
