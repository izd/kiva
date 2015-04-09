package com.zackhsi.kiva.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.KivaProxy;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.models.Loan;
import com.zackhsi.kiva.models.PaymentStub;
import com.zackhsi.kiva.models.User;

import org.json.JSONException;

import java.math.BigDecimal;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoanReviewActivity extends ActionBarActivity {
    @InjectView(R.id.ivProfile)
    ImageView ivProfile;

    @InjectView(R.id.tvLoanName)
    TextView tvLoanName;

    @InjectView(R.id.spinLoanAmount)
    Spinner spinLoanAmount;

    @InjectView(R.id.btnConfirmLend)
    Button btnConfirmLend;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

            .clientId("AcPFBo-G7KjP_ultRo2SyG_ph93ZU_TemQex-Gybj1XOCAKOfyr5N8wCCLjQV6VCG8wyj326E9G4tMXI");


    private Loan loan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_review);
        ButterKnife.inject(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.loan = (Loan) getIntent().getSerializableExtra("loan");
        tvLoanName.setText(loan.name);
        Picasso.with(this).load(loan.imageUrl()).into(ivProfile);

        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this, R.array.loan_increments, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLoanAmount.setAdapter(spinAdapter);

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);

        btnConfirmLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = spinLoanAmount.getSelectedItem().toString();
                String displayName = "Kiva Loan: " + loan.name;
                PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", displayName,
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);

                // send the same configuration for restart resiliency
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                startActivityForResult(intent, 0);
            }
        });
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
                    PaymentStub  payment = new PaymentStub().fromPaymentResponse(confirm.toJSONObject(), confirm.getPayment().toJSONObject());
                    payment.setLoanId((int) loan.id);
                    //TODO: @zackhsi help me out with a User Id here plz :+1:
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loan_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void launchProfileActivity() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_top, R.anim.hold);
    }
}
