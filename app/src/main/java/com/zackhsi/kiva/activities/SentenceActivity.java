package com.zackhsi.kiva.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaProxy;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.fragments.LoanListViewFragment;
import com.zackhsi.kiva.fragments.LoginDialogFragment;
import com.zackhsi.kiva.fragments.SentenceOptionSelectorFragment;
import com.zackhsi.kiva.fragments.SentencePreviewFragment;
import com.zackhsi.kiva.helpers.ReadableTransform;
import com.zackhsi.kiva.helpers.SentenceManager;
import com.zackhsi.kiva.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SentenceActivity extends ActionBarActivity implements
        SentencePreviewFragment.OnOptionEditListener,
        SentencePreviewFragment.OnBackgroundChangedListener,
        SentencePreviewFragment.OnAdvanceToResultsListener,
        SentenceOptionSelectorFragment.OnFinishOptionEditListener, 
        LoginDialogFragment.LoginDialogFragmentListener {

    @InjectView(R.id.switcher)
    ViewSwitcher switcher;


    int selectedGroup;
    int selectedCountry;
    int selectedSector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_kiva_white);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flSentence, new SentencePreviewFragment());
        ft.commit();
    }

    @Override
    public void onBackgroundChanged(int resId) {
        if (switcher.getDisplayedChild() == 0) {
            Picasso.with(this).load(resId).transform(new ReadableTransform(this)).noFade().into((ImageView) switcher.getNextView(), new Callback() {
                @Override
                public void onSuccess() {
                    switcher.showNext();
                }

                @Override
                public void onError() {

                }
            });

        } else {
            Picasso.with(this).load(resId).transform(new ReadableTransform(this)).noFade().into((ImageView) switcher.getChildAt(switcher.getDisplayedChild() - 1), new Callback() {
                @Override
                public void onSuccess() {
                    switcher.showPrevious();
                }

                @Override
                public void onError() {

                }
            });

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sentence, menu);
        return true;
    }

    @Override
    public void onOptionEdit(SentenceManager.OptionType itemBeingEdited, int previouslySelectedIndex) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // TODO: broken
                ft.setCustomAnimations(
                        R.anim.hold, R.anim.hold, R.anim.hold_slide_out_left_fade_in, R.anim.slide_out_left);

        ft.replace(R.id.flSentence, SentenceOptionSelectorFragment.newInstance(itemBeingEdited, previouslySelectedIndex));
        ft.addToBackStack(null);

        ft.commit();
    }

    @Override
    public void onAdvanceToResults() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // TODO: args here for which results to fetch
        ft.add(R.id.flSentence, LoanListViewFragment.newInstance(
                SentenceManager.readPreferenceCodeString(getApplicationContext(), SentenceManager.OptionType.SECTOR),
                SentenceManager.readPreferenceCodeString(getApplicationContext(), SentenceManager.OptionType.GROUP, true),
                SentenceManager.readPreferenceCodeString(getApplicationContext(), SentenceManager.OptionType.GROUP, false),
                SentenceManager.readPreferenceCodeString(getApplicationContext(), SentenceManager.OptionType.COUNTRY)
        ));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onFinishOptionEdit(SentenceManager.OptionType itemBeingEdited, int selectedPosition) {
        SentenceManager.updatePreferences(this, itemBeingEdited, selectedPosition);
        getSupportFragmentManager().popBackStack();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miProfile) {
            if (!KivaProxy.isAuthenticated()) {
                // Launch OAuth dialog fragment
                KivaApplication.getAuthenticatedRestClient(this);
                return true;
            }

            launchProfileActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchProfileActivity() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_top, R.anim.hold);
    }

    @Override
    public void onFinishLoginDialog() {
        launchProfileActivity();
    }
}
