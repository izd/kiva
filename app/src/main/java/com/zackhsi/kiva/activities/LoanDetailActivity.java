package com.zackhsi.kiva.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.KivaProxy;
import com.zackhsi.kiva.helpers.AlphaForegroundColorSpan;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.fragments.LoginDialogFragment;
import com.zackhsi.kiva.helpers.HtmlHelper;
import com.zackhsi.kiva.helpers.ReadableTransform;
import com.zackhsi.kiva.helpers.RoundedCornerTransformation;
import com.zackhsi.kiva.helpers.SentenceManager;
import com.zackhsi.kiva.helpers.ViewHelper;
import com.zackhsi.kiva.models.Loan;
import com.zackhsi.kiva.models.PaymentStub;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

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

    @InjectView(R.id.headerPicture)
    ImageView ivHeaderPicture;

    @InjectView(R.id.tvOverview)
    TextView tvOverview;

    @InjectView(R.id.tvDescription)
    TextView tvDescription;

    @InjectView(R.id.tvStatus)
    TextView tvStatus;

    @InjectView(R.id.tvLenders)
    TextView tvLenders;

    @InjectView(R.id.tvFunded)
    TextView tvFunded;

    @InjectView(R.id.tvTime)
    TextView tvTime;

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
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) //When ready, switch to prod (ENVIRONMENT_PRODUCTION)
            .clientId("AcPFBo-G7KjP_ultRo2SyG_ph93ZU_TemQex-Gybj1XOCAKOfyr5N8wCCLjQV6VCG8wyj326E9G4tMXI");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);
        ButterKnife.inject(this);

        this.loan = (Loan) getIntent().getSerializableExtra("loan");
        setupScrollViewCallbacks();
        setupToolbar();

        this.client = KivaApplication.getRestClient();
        this.titleString = new SpannableString(this.loan.name);
        this.alphaForegroundColorSpan = new AlphaForegroundColorSpan(Color.WHITE);

        getCurrentLoanDetails();
        populateInfo();
    }

    private void getCurrentLoanDetails() {
        client.getLoan(this.loan.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ArrayList<Loan> responseLoans = Loan.fromJson(response.getJSONArray("loans"));
                    loan = responseLoans.get(0);
                    populateInfo();
                } catch (JSONException e) {
                    Toast.makeText(LoanDetailActivity.this, "Problem loading loans", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // postpone transition to fix element size bug
            postponeEnterTransition();
        }
        Picasso.with(this).load(loan.imageThumbUrl()).noFade().transform(new RoundedCornerTransformation(false)).into(ivHeaderLogo, new Callback() {
            @Override
            public void onSuccess() {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startPostponedEnterTransition();
                }
            }

            @Override
            public void onError() {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startPostponedEnterTransition();
                }
            }
        });
        ivHeaderLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoanDetailActivity.this, FullscreenImagePreviewActivity.class);
                i.putExtra("mediaUrl", loan.imageUrl());
                startActivity(i);
            }
        });

        Picasso.with(this).load(SentenceManager.getImageForSector(this, loan.sector)).fit().centerCrop()
                .transform(new ReadableTransform(this)).noFade().into(ivHeaderPicture);
    }

    private void populateInfo() {
        tvOverview.setText(loan.getOverview());
        tvStatus.setText(loan.status.substring(0,1).toUpperCase() + loan.status.substring(1).toLowerCase());
        tvLenders.setText(loan.lenderCount + " lenders");
        tvFunded.setText(fundedText());
        tvTime.setText(loan.getRelativePlannedExpiration());
        tvDescription.setText(loan.description);
    }

    private Spanned descriptionText() {
        if (loan.description == null) {
            return new SpannableString("");
        }
        return Html.fromHtml(loan.description);
    }

    private Spanned fundedText() {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return Html.fromHtml(HtmlHelper.greenText("$" + formatter.format(loan.fundedAmount)) + " raised");
    }

    private void setupScrollViewCallbacks() {
        scrollview.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                header.setTranslationY(Math.max(-scrollY, minHeaderTranslation()));

                float ratio = ViewHelper.clamp(header.getTranslationY() / minHeaderTranslation(), 0.0f, 1.0f);
                setTitleAlpha(ViewHelper.clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));

                LinearInterpolator interpolator = new LinearInterpolator(LoanDetailActivity.this, null);
                float interpolation = interpolator.getInterpolation(ratio);

                RectF mRect1 = ViewHelper.getOnScreenRect(ivHeaderLogo);
                RectF mRect2 = ViewHelper.getOnScreenRect(icon);

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

    private int minHeaderTranslation() {
        return toolbar.getMinimumHeight() - header.getHeight();
    }

    private void setTitleAlpha(float alpha) {
        alphaForegroundColorSpan.setAlpha(alpha);
        this.titleString.setSpan(alphaForegroundColorSpan, 0, titleString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(this.titleString);
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
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        new MaterialDialog.Builder(this)
                .title("Loan amount:")
                .items(R.array.loan_increments_titles)
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.primary)
                .iconRes(R.drawable.ic_heart)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String[] loanValues = getResources().getStringArray(R.array.loan_increments_dollar_values);
                        int amount = Integer.valueOf(loanValues[which]);
                        String displayName = "Kiva Loan: " + loan.name;
                        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", displayName,
                                PayPalPayment.PAYMENT_INTENT_SALE);

                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                        startActivityForResult(intent, 0);
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                    PaymentStub payment = new PaymentStub().fromPaymentResponse(confirm.toJSONObject(), confirm.getPayment().toJSONObject());
                    payment.setLoanId((int) loan.id);
                    String accountId = new KivaProxy().getKivaProxyId();
                    payment.setUserId(accountId);
                    payment.saveEventually();

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
            launchProfileActivity();
            finish();
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void launchProfileActivity() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_top, R.anim.hold);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loan_detail, menu);
        return true;
    }

    @Override
    public void onFinishLoginDialog() {
        launchLoanReviewActivity();
    }
}
