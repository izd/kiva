package com.zackhsi.kiva.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.squareup.picasso.Picasso;
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

    private Loan loan;
    private KivaClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);

        this.loan = (Loan) getIntent().getSerializableExtra("loan");
        client = KivaApplication.getRestClient();

        setupViews();
    }

    private void setupViews() {
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(loan.name);
        Picasso.with(this).load(loan.imageUrl()).into(ivHeaderLogo);
        pbPercentFunded.setProgress(loan.percentFunded);
        tvPercentFunded.setText("" + loan.percentFunded);
        tvOverview.setText(loan.getOverview());
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
