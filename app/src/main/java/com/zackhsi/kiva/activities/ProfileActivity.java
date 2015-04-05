package com.zackhsi.kiva.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.UserPagerAdapter;
import com.zackhsi.kiva.fragments.LoanListViewFragment;
import com.zackhsi.kiva.models.Loan;
import com.zackhsi.kiva.models.User;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends ActionBarActivity implements LoanListViewFragment.OnItemSelectedListener, ObservableScrollViewCallbacks {
    @InjectView(R.id.header)
    FrameLayout header;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.ivUser)
    CircleImageView ivUser;

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabsStrip;

    @InjectView(R.id.flPagerWrapper)
    FrameLayout flPagerWrapper;

    private User user;
    private KivaClient client;
    private UserPagerAdapter userPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = KivaApplication.getRestClient();

        this.user = (User) getIntent().getSerializableExtra("user");

        setupViews();

        getMyAccount();
    }

    private void getMyAccount() {
        client.getMyAccount(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("ACCOUNT", "Success!");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("ACCOUNT", "Failure! " + errorResponse.toString(), throwable);
            }
        });
    }

    private void setupViews() {
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        Picasso.with(this).load(user.getImageUrl()).into(ivUser);

        userPagerAdapter = new UserPagerAdapter(getSupportFragmentManager(), user);
        viewPager.setAdapter(userPagerAdapter);
        tabsStrip.setViewPager(viewPager);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        Log.d("SCROLL", "" + scrollY);
        header.setTranslationY(Math.max(-scrollY, minHeaderTranslation()));
    }

    private int minHeaderTranslation() {
        return -header.getHeight();
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private Scrollable getCurrentScrollable() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return null;
        }
        View view = fragment.getView();
        if (view == null) {
            return null;
        }
        return (Scrollable) view.findViewById(R.id.scroll);
    }

    private Fragment getCurrentFragment() {
        return (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
    }

    @Override
    public void onLoanSelected(Loan loan) {
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.hold, R.anim.slide_out_top);
    }
}
