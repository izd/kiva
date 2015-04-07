package com.zackhsi.kiva.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zackhsi.kiva.R;
import com.zackhsi.kiva.fragments.SentenceOptionSelectorFragment;
import com.zackhsi.kiva.fragments.SentencePreviewFragment;

public class SentenceActivity extends ActionBarActivity implements SentencePreviewFragment.OnOptionEditListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flSentence, new SentencePreviewFragment());
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sentence, menu);
        return true;
    }

    @Override
    public void onOptionSelected(String link) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.flSentence, new SentenceOptionSelectorFragment());
        ft.addToBackStack("");

        ft.commit();
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
