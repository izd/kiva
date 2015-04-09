package com.zackhsi.kiva.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.fragments.LoanListViewFragment;
import com.zackhsi.kiva.fragments.UserInfoFragment;
import com.zackhsi.kiva.helpers.SentenceManager;
import com.zackhsi.kiva.models.User;

import java.util.HashMap;

/**
 * Created by zackhsi on 3/25/15.
 */
public class UserPagerAdapter extends CacheFragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"About", "My Loans"};
    private HashMap<Integer, Fragment> mPageReferenceMap = new HashMap<>();

    public UserPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    protected Fragment createItem(int i) {
        Fragment fragment;
        if (i == 0) {
            fragment = UserInfoFragment.newInstance();
        } else {
            fragment = LoanListViewFragment.newInstance();
        }
        mPageReferenceMap.put(i, fragment);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }
}