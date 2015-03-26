package com.zackhsi.kiva.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.zackhsi.kiva.fragments.UserInfoFragment;
import com.zackhsi.kiva.models.User;

/**
 * Created by zackhsi on 3/25/15.
 */
public class UserPagerAdapter extends FragmentPagerAdapter {
    private User user;
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "About", "Tab2", "Tab3" };

    public UserPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return UserInfoFragment.newInstance(this.user);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}