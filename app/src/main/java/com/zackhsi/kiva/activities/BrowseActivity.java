package com.zackhsi.kiva.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.LoanAdapter;
import com.zackhsi.kiva.fragments.SearchSpinnerFragment;
import com.zackhsi.kiva.models.Loan;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;


public class BrowseActivity extends ActionBarActivity implements SearchSpinnerFragment.OnFragmentInteractionListener {
    private ListView lvBrowse;
    private ArrayList<Loan> loans;
    private LoanAdapter adapterLoans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.browse_list_header, null);
        loans = new ArrayList<>();
        lvBrowse = (ListView) findViewById(R.id.lvBrowse);
        adapterLoans = new LoanAdapter(this, android.R.layout.simple_list_item_1, loans);
        lvBrowse.setAdapter(adapterLoans);
        lvBrowse.addHeaderView(header);

        getLoans();
    }

    private void getLoans() {
        KivaClient client = new KivaClient();
        client.searchUnfundedLoans("sa", "Green", new JsonHttpResponseHandler(){
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
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
