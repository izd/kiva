package com.zackhsi.kiva.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.OAuthTask;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.UserPagerAdapter;
import com.zackhsi.kiva.fragments.LoanListViewFragment;
import com.zackhsi.kiva.models.Loan;
import com.zackhsi.kiva.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ProfileActivity extends ActionBarActivity implements LoanListViewFragment.OnItemSelectedListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.ivUser)
    ImageView ivUser;

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabsStrip;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.user = (User) getIntent().getSerializableExtra("user");

        setupViews();
    }

    private void setupViews() {
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(user.getName());
        Picasso.with(this).load(user.getImageUrl()).into(ivUser);

        viewPager.setAdapter(new UserPagerAdapter(getSupportFragmentManager(), user));
        tabsStrip.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoanSelected(Loan loan) {

    }
}
