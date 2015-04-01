package com.zackhsi.kiva.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.zackhsi.kiva.fragments.LoanListViewFragment;
import com.zackhsi.kiva.fragments.UserInfoFragment;
import com.zackhsi.kiva.models.User;

/**
 * Created by zackhsi on 3/25/15.
 */
public class UserPagerAdapter extends CacheFragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private User user;
    private String tabTitles[] = new String[]{"About", "Mine", "Favorites"};

    public UserPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    protected Fragment createItem(int i) {
        if (i == 0) {
            return UserInfoFragment.newInstance(this.user);
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