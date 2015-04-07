package com.zackhsi.kiva.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.loopj.android.http.JsonHttpResponseHandler;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.UserPagerAdapter;
import com.zackhsi.kiva.fragments.LoanListViewFragment;
import com.zackhsi.kiva.helpers.AlphaForegroundColorSpan;
import com.zackhsi.kiva.helpers.ViewHelper;
import com.zackhsi.kiva.models.Loan;
import com.zackhsi.kiva.models.User;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends ActionBarActivity implements LoanListViewFragment.OnItemSelectedListener, ObservableScrollViewCallbacks {
    @InjectView(R.id.container)
    TouchInterceptionFrameLayout container;

    @InjectView(R.id.flPagerWrapper)
    FrameLayout flPagerWrapper;

    @InjectView(R.id.header)
    FrameLayout header;

    @InjectView(R.id.headerPicture)
    ImageView headerPicture;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.icon)
    ImageView icon;

    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.ivUser)
    CircleImageView ivUser;

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabsStrip;

    private User user;
    private KivaClient client;
    private UserPagerAdapter userPagerAdapter;
    private SpannableString titleString;
    private AlphaForegroundColorSpan alphaForegroundColorSpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = KivaApplication.getRestClient();

        this.user = (User) getIntent().getSerializableExtra("user");
        this.titleString = new SpannableString(this.user.getName());
        this.alphaForegroundColorSpan = new AlphaForegroundColorSpan(Color.WHITE);

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
        container.setScrollInterceptionListener(mInterceptionListener);
    }

    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent motionEvent, boolean moving, float diffX, float diffY) {
            boolean scrollingDown = 0 < diffY;
            boolean scrollingUp = diffY < 0;

            boolean isHeaderFullySquished = header.getTranslationY() <= minHeaderTranslation();
            boolean isHeaderFullyExpanded = header.getTranslationY() >= 0;

            boolean isFragmentScrolledToTop = getCurrentScrollable().getCurrentScrollY() <= 40;

            if (scrollingUp && !isHeaderFullySquished) {
                return true;
            }

            if (scrollingDown && !isHeaderFullyExpanded && isFragmentScrolledToTop) {
                return true;
            }

            return false;
        }

        @Override
        public void onDownMotionEvent(MotionEvent motionEvent) {
        }

        @Override
        public void onMoveMotionEvent(MotionEvent motionEvent, float diffX, float diffY) {

            // Adjust header translation y
            float newTranslationY = header.getTranslationY() + diffY;
            float newConstrainedTranslationY = Math.min(Math.max(newTranslationY, minHeaderTranslation()), 0);
            header.setTranslationY(newConstrainedTranslationY);

            float ratio = ViewHelper.clamp(header.getTranslationY() / minHeaderTranslation(), 0.0f, 1.0f);
            setTitleAlpha(ViewHelper.clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));

            AccelerateDecelerateInterpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator(ProfileActivity.this, null);
            float interpolation = mAccelerateDecelerateInterpolator.getInterpolation(ratio);

            RectF mRect1 = ViewHelper.getOnScreenRect(ivUser);
            RectF mRect2 = ViewHelper.getOnScreenRect(icon);

            float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
            float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
            float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
            float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

            ivUser.setTranslationX(translationX);
            ivUser.setTranslationY(translationY - header.getTranslationY());
            ivUser.setScaleX(scaleX);
            ivUser.setScaleY(scaleY);

            // Adjust view pager top padding
            int topPadding = Math.round(header.getHeight() + header.getTranslationY());
            flPagerWrapper.setPadding(0, topPadding, 0, 0);
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent motionEvent) {
        }
    };

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    private void setTitleAlpha(float alpha) {
        alphaForegroundColorSpan.setAlpha(alpha);
        this.titleString.setSpan(alphaForegroundColorSpan, 0, titleString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(this.titleString);
    }

    private int minHeaderTranslation() {
        return toolbar.getMinimumHeight() - headerPicture.getHeight();
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
        Intent i = new Intent(this, SentenceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.hold, R.anim.slide_out_top);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miLogout) {
            new MaterialDialog.Builder(this)
                    .title("Are you sure?")
                    .positiveText("Log out")
                    .positiveColor(Color.RED)
                    .negativeText("Cancel")
                    .negativeColor(Color.BLACK)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            client.clearAccessToken();
                            onBackPressed();
                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
