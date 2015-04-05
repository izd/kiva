package com.zackhsi.kiva.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.AlphaForegroundColorSpan;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.fragments.LoginDialogFragment;
import com.zackhsi.kiva.models.Loan;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoanDetailActivity extends ActionBarActivity implements LoginDialogFragment.LoginDialogFragmentListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.icon)
    ImageView icon;

    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.headerLogo)
    ImageView ivHeaderLogo;

    @InjectView(R.id.pbPercentFunded)
    ProgressBar pbPercentFunded;

    @InjectView(R.id.tvPercentFunded)
    TextView tvPercentFunded;

    @InjectView(R.id.tvOverview)
    TextView tvOverview;

    @InjectView(R.id.btnLend)
    Button btnLend;

    @InjectView(R.id.scrollview)
    ObservableScrollView scrollview;

    @InjectView(R.id.header)
    FrameLayout header;

    private Loan loan;
    private KivaClient client;
    private SpannableString titleString;
    private AlphaForegroundColorSpan alphaForegroundColorSpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);

        this.loan = (Loan) getIntent().getSerializableExtra("loan");
        this.client = KivaApplication.getRestClient();
        this.titleString = new SpannableString(this.loan.name);
        this.alphaForegroundColorSpan = new AlphaForegroundColorSpan(Color.WHITE);

        setupViews();
    }

    private void setupViews() {
        ButterKnife.inject(this);
        setupToolbar();
        populateInfo();
        setupScrollViewCallbacks();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Picasso.with(this).load(loan.imageUrl()).into(ivHeaderLogo);
    }

    private void populateInfo() {
        pbPercentFunded.setProgress(loan.percentFunded);
        tvPercentFunded.setText("" + loan.percentFunded);
        tvOverview.setText(loan.getOverview());
    }

    private void setupScrollViewCallbacks() {
        scrollview.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                header.setTranslationY(Math.max(-scrollY, minHeaderTranslation()));

                float ratio = clamp(header.getTranslationY() / minHeaderTranslation(), 0.0f, 1.0f);
                setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));

                AccelerateDecelerateInterpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator(LoanDetailActivity.this, null);
                float interpolation = mAccelerateDecelerateInterpolator.getInterpolation(ratio);

                RectF mRect1 = getOnScreenRect(ivHeaderLogo);
                RectF mRect2 = getOnScreenRect(icon);

                float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
                float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
                float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
                float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

                ivHeaderLogo.setTranslationX(translationX);
                ivHeaderLogo.setTranslationY(translationY - header.getTranslationY());
                ivHeaderLogo.setScaleX(scaleX);
                ivHeaderLogo.setScaleY(scaleY);
            }

            @Override
            public void onDownMotionEvent() {}

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {}
        });
    }

    private RectF getOnScreenRect(View view) {
        RectF rect = new RectF();
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    private int minHeaderTranslation() {
        return toolbar.getMinimumHeight() - header.getHeight();
    }

    private void setTitleAlpha(float alpha) {
        alphaForegroundColorSpan.setAlpha(alpha);
        this.titleString.setSpan(alphaForegroundColorSpan, 0, titleString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(this.titleString);
    }

    private float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    @OnClick(R.id.btnLend)
    public void lend(View view) {
        if (userIsLoggedIn()){
            launchLoanReviewActivity();
        } else {
            KivaApplication.getAuthenticatedRestClient(this);
            Toast.makeText(LoanDetailActivity.this, "Please Log In.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean userIsLoggedIn() {
        return client.checkAccessToken() != null;
    }

    private void launchLoanReviewActivity() {
        Intent i = new Intent(this, LoanReviewActivity.class);
        i.putExtra("loan", loan);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loan_detail, menu);
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
    public void onFinishLoginDialog() {
        launchLoanReviewActivity();
    }
}
