package com.zackhsi.kiva.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.fragments.LoanListViewFragment;

public class BrowseResultsActivity extends ActionBarActivity {
    FrameLayout flBrowseResults;
    ObservableRecyclerView orvBrowse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_results);
        orvBrowse = (ObservableRecyclerView) ((LoanListViewFragment) getSupportFragmentManager().findFragmentById(R.id.loan_list_view_fragment)).getListView();
        LoanListViewFragment lvFragment = (LoanListViewFragment) getSupportFragmentManager().findFragmentById(R.id.loan_list_view_fragment);
        lvFragment.getLoans(null, "Green");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_results, menu);
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
}
