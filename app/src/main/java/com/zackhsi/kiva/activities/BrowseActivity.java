package com.zackhsi.kiva.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.fragments.LoanListViewFragment;
import com.zackhsi.kiva.fragments.LoginDialogFragment;
import com.zackhsi.kiva.models.Loan;
import com.zackhsi.kiva.models.PaymentStub;
import com.zackhsi.kiva.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class BrowseActivity extends ActionBarActivity implements ObservableScrollViewCallbacks,
        LoanListViewFragment.OnItemSelectedListener, AdapterView.OnItemSelectedListener,
        LoginDialogFragment.LoginDialogFragmentListener {

    private static final boolean TOOLBAR_IS_STICKY = true;

    @InjectView(R.id.toolbar)
    View mToolbar;

    @InjectView(R.id.overlay)
    View mOverlayView;

    @InjectView(R.id.image)
    ImageView mImageView;

    @InjectView(R.id.list_background)
    View mListBackgroundView;

//    @InjectView(R.id.spin_sector)
//    Spinner spinSector;

    private int mFlexibleSpaceImageHeight;
    private int mToolbarColor;
    private TransitionDrawable td;
    private int mActionBarSize;
    private ObservableRecyclerView orvBrowse;

    private Drawable currentBackground;

    private KivaClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
//        ButterKnife.inject(this);
//
//
//        client = KivaApplication.getRestClient();
//
//        td = new TransitionDrawable(new Drawable[]{
//
//                getResources().getDrawable(R.drawable.alt_energy),
//                getResources().getDrawable(R.drawable.education)
//        });
//        mImageView.setImageDrawable(td);
//        initializeSpinners();
//        orvBrowse = (ObservableRecyclerView) ((LoanListViewFragment) getSupportFragmentManager().findFragmentById(R.id.loan_list_view_fragment)).getListView();
//        LoanListViewFragment lvFragment = (LoanListViewFragment) getSupportFragmentManager().findFragmentById(R.id.loan_list_view_fragment);
//        lvFragment.getLoans(null, "Green");
//
//        setSupportActionBar((Toolbar) mToolbar);
//        setTitle(null);
//
//        // Set image height to full screen height
//        mFlexibleSpaceImageHeight = getScreenHeight();
//
//        mToolbarColor = getResources().getColor(R.color.primary);
//        mToolbar.setBackgroundColor(Color.TRANSPARENT);
//
//        mActionBarSize = getActionBarSize();
//
//        // Set padding view for ListView. This is the flexible space.
//        View paddingView = new View(this);
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
//                mFlexibleSpaceImageHeight);
//        paddingView.setLayoutParams(lp);
//        paddingView.setClickable(true);
//        lvBrowse.addHeaderView(paddingView);
//        orvBrowse.
//
//        final View contentView = getWindow().getDecorView().findViewById(android.R.id.content);
//        contentView.post(new Runnable() {
//            @Override
//            public void run() {
//                // mListBackgroundView's should fill its parent vertically
//                // but the height of the content view is 0 on 'onCreate'.
//                // So we should get it with post().
//                mListBackgroundView.getLayoutParams().height = contentView.getHeight();
//            }
//        });
    }

    private void initializeSpinners(){
//        spinSector.setOnItemSelectedListener(this);
//        ArrayAdapter<String> sectorAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_browse, getResources().getStringArray(R.array.kiva_themes));
//        sectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinSector.setAdapter(sectorAdapter);
//        spinGeography.setOnItemSelectedListener(this);
//        ArrayAdapter<String> geoAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_browse, getResources().getStringArray(R.array.kiva_regions_displaynames));
//        geoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinGeography.setAdapter(geoAdapter);
    }

    @Override
    public void onLoanSelected(Loan loan) {
        Intent i = new Intent(this, LoanDetailActivity.class);
        i.putExtra("loan", loan);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            td.startTransition(1000);
        }
//        if (parent.getId() == R.id.spin_sector){
//            String sector = getResources().getStringArray(R.array.kiva_themes)[position];
//            Toast.makeText(this, sector, Toast.LENGTH_SHORT).show();
//            LoanListViewFragment lvFragment = (LoanListViewFragment) getSupportFragmentManager().findFragmentById(R.id.loan_list_view_fragment);
//            lvFragment.getLoans(null, sector);
//
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // pass
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miProfile) {
            if (client.checkAccessToken() == null) {
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
        i.putExtra("user", User.getStubUser());
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_top, R.anim.hold);
    }

    // Animation helpers
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//
//        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
//        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
//
//        mOverlayView.setTranslationY(ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
//        mImageView.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
//
//        // Translate list background
//        mListBackgroundView.setTranslationY(Math.max(0, -scrollY + mFlexibleSpaceImageHeight));
//
//        // Change alpha of overlay
//        mOverlayView.setAlpha(ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
//
//        // Translate spinner
//        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, 0.3f);
//        int maxSectorSpinnerTranslationY = (int) (((mFlexibleSpaceImageHeight / 2) - 20) - spinSector.getHeight() * scale);
//        int sectorSpinnerTranslationY = maxSectorSpinnerTranslationY - (int) (scrollY / 1.5f);
//        if (TOOLBAR_IS_STICKY) {
//            sectorSpinnerTranslationY = Math.max(0, sectorSpinnerTranslationY);
//        }
//        spinSector.setTranslationY(sectorSpinnerTranslationY);
//
//        if (TOOLBAR_IS_STICKY) {
//            // Change alpha of toolbar background
//            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
//                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
//            } else {
//                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
//            }
//        } else {
//            // Translate Toolbar
//            if (scrollY < mFlexibleSpaceImageHeight) {
//                mToolbar.setTranslationY(0);
//            } else {
//                mToolbar.setTranslationY(-scrollY);
//            }
//        }
    }

    @Override
    public void onDownMotionEvent() {
        // nothing to implement
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        // nothing to implement
    }

    protected int getActionBarSize() {
//        TypedValue typedValue = new TypedValue();
//        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
//        int indexOfAttrTextSize = 0;
//        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
//        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
//        a.recycle();
//        return actionBarSize;
        return 0;
    }

    protected int getScreenHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y - 50;
    }

    private void transitionImages(int oldImageId, int newImageId){

    }

    @Override
    public void onFinishLoginDialog() {
        launchProfileActivity();
    }
}
