package com.zackhsi.kiva.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.models.User;


public class ProfileActivity extends ActionBarActivity {
    private User user;

    private ImageView ivUser;
    private TextView tvName;
    private TextView tvWhereabouts;
    private TextView tvLoanBecause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.user = (User) getIntent().getSerializableExtra("user");

        setupViews();
    }

    private void setupViews() {
        this.ivUser = (ImageView) findViewById(R.id.ivUser);
        Picasso.with(this).load(user.getImageUrl()).into(ivUser);

        this.tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(user.getName());

        this.tvWhereabouts = (TextView) findViewById(R.id.tvWhereabouts);
        tvWhereabouts.setText(user.getWhereabouts());

        this.tvLoanBecause = (TextView) findViewById(R.id.tvLoanBecause);
        tvLoanBecause.setText(user.getLoanBecause());
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
}
