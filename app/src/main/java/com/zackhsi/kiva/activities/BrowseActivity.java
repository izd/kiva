package com.zackhsi.kiva.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.LoanAdapter;
import com.zackhsi.kiva.models.Loan;
import com.zackhsi.kiva.models.User;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class BrowseActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private ArrayList<Loan> loans;
    private LoanAdapter adapterLoans;

    @InjectView(R.id.lvBrowse) ListView lvBrowse;
    @InjectView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.browse_list_header, null);
        initializeSpinners(header);

        loans = new ArrayList<>();
        adapterLoans = new LoanAdapter(this, android.R.layout.simple_list_item_1, loans);
        lvBrowse.setAdapter(adapterLoans);
        lvBrowse.addHeaderView(header);

        getLoans();
    }

    private void initializeSpinners(View header){
        Spinner spinRegion = (Spinner) header.findViewById(R.id.spinRegion);
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this, R.array.kiva_regions_displaynames,
                android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRegion.setAdapter(regionAdapter);
        Spinner spinThemes = (Spinner) header.findViewById(R.id.spinTheme);
        ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(this, R.array.kiva_themes,
                android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinThemes.setAdapter(themeAdapter);
        spinThemes.setOnItemSelectedListener(this);
        spinRegion.setOnItemSelectedListener(this);

    }

    private void getLoans() {
        KivaClient client = new KivaClient();
        client.searchUnfundedLoans("sa", "Green", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<Loan> responseLoans = new Loan().fromJson(response);
                loans.addAll(responseLoans);
                adapterLoans.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Problem loading loans", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnItemClick(R.id.lvBrowse)
    public void launchDetailActivity(int position) {
        Intent i = new Intent(BrowseActivity.this, LoanDetailActivity.class);
        i.putExtra("loan", loans.get(position - lvBrowse.getHeaderViewsCount()));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miProfile) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("user", User.getStubUser());
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(this, "ITEMSELECTED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
