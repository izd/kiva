package com.zackhsi.kiva.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.activities.LoanDetailActivity;
import com.zackhsi.kiva.adapters.EndlessRecyclerOnScrollListener;
import com.zackhsi.kiva.adapters.LoanArrayAdapter;
import com.zackhsi.kiva.adapters.LoanListViewListener;
import com.zackhsi.kiva.models.Loan;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class LoanListViewFragment extends Fragment {

    private static String fragmentType;
    @InjectView(R.id.scroll)
    ObservableRecyclerView orvLoans;
    @InjectView(R.id.ivCustomProgressAnimation)
    ImageView ivCustomProgressAnimation;
    @InjectView(R.id.linProgressContainer)
    LinearLayout linProgressContainer;
    @InjectView(R.id.tvNoResults)
    TextView tvNoResults;

    private KivaClient client;
    private ArrayList<Loan> loans;
    private LoanArrayAdapter adapterLoans;
    private OnItemSelectedListener listener;
    private LinearLayoutManager manager;
    int currentResultsPage;

    public static LoanListViewFragment newInstance(String sector, String gender, String borrowerType, String countryCode) {
        fragmentType = "searchResult";
        LoanListViewFragment loanListViewFragment = new LoanListViewFragment();
        Bundle args = new Bundle();
        if (sector != null && !sector.startsWith("Any")) {
            args.putString("sector", sector);
        }
        if (gender != null && !gender.startsWith("Any")) {
            args.putString("gender", gender);
        }
        if (borrowerType != null && !borrowerType.startsWith("Any")) {
            args.putString("borrowerType", borrowerType);
        }
        if (countryCode != null && !countryCode.startsWith("Any")) {
            args.putString("countryCode", countryCode);
        }
        loanListViewFragment.setArguments(args);

        return loanListViewFragment;
    }

    public static LoanListViewFragment newInstance(){
        fragmentType = "myLoans";
        LoanListViewFragment fragment = new LoanListViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new LinearLayoutManager(getActivity());
        client = KivaApplication.getRestClient();
        loans = new ArrayList<>();
        adapterLoans = new LoanArrayAdapter(getActivity(), loans);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view_loan, container, false);
        ButterKnife.inject(this, view);

        ivCustomProgressAnimation.setBackgroundResource(R.drawable.custom_loading_anim);
        AnimationDrawable anim = (AnimationDrawable) ivCustomProgressAnimation.getBackground();
        anim.start();

        tvNoResults.setVisibility(View.GONE);

        Activity activity = getActivity();
        if (activity instanceof ObservableScrollViewCallbacks) {
            orvLoans.setScrollViewCallbacks((ObservableScrollViewCallbacks) activity);
        }
        orvLoans.setLayoutManager(manager);
        orvLoans.setAdapter(adapterLoans);
        orvLoans.addOnItemTouchListener(
                new LoanListViewListener(getActivity(), new LoanListViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Loan loan = (Loan) loans.get(position);
                        Intent i = new Intent(getActivity(), LoanDetailActivity.class);
                        i.putExtra("loan", loan);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.ivImage), "profile");
                        getActivity().startActivity(i, options.toBundle());
                    }
                })
        );

        orvLoans.setOnScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                // TODO: fix crash, no arguments
               loanAdditionalLoans(
                       currentResultsPage,
                       getArguments().getString("sector", null),
                       getArguments().getString("borrowerType", null),
                       getArguments().getString("countryCode", null),
                       getArguments().getString("gender", null)
               );
            }
        });
        // TODO: progress bar

        return view;
    }

    private void loanAdditionalLoans(final int oldResultsPage, String sector, String borrowerType, String countryCode, String gender) {
        client.searchUnfundedLoansWithPage(sector,gender,borrowerType,countryCode, oldResultsPage, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ArrayList<Loan> responseLoans = Loan.fromJson(response.getJSONArray("loans"));
                    loans.addAll(responseLoans);
                    adapterLoans.notifyDataSetChanged();
                    currentResultsPage++;
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Problem loading loans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), "Problem loading loans", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public View getListView() {
        return orvLoans;
    }

    public ArrayList getLoansArrayList() {
        return loans;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (fragmentType == "searchResult") {
            getLoans(
                    getArguments().getString("sector", null),
                    getArguments().getString("borrowerType", null),
                    getArguments().getString("countryCode", null),
                    getArguments().getString("gender", null)
            );
        } else if (fragmentType == "myLoans") {
            getMyLoans("userid");

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


//        if (activity instanceof OnItemSelectedListener) {
//            listener = (OnItemSelectedListener) activity;
//        } else {
//            throw new ClassCastException(activity.toString() + " must implement listener");
//        }
    }

//    @OnItemClick(R.id.scroll)
//    public void launchDetailActivity(int position) {
//        listener.onLoanSelected((Loan) orvLoans.getItemAtPosition(position));
//    }

    public void getLoans(String sector, String borrowerType, String countryCode, String gender) {
        client.searchUnfundedLoans(sector, gender, borrowerType, countryCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ArrayList<Loan> responseLoans = Loan.fromJson(response.getJSONArray("loans"));
                    if (responseLoans.size() == 0) {
                        tvNoResults.setVisibility(View.VISIBLE);
                    }

                    loans.clear();
                    loans.addAll(responseLoans);
                    adapterLoans.notifyDataSetChanged();
                    currentResultsPage = 1;
                    showResultsRecyclerView();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Problem loading loans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("LOANS", "Problem loading loans", throwable);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void getMyLoans(String userId) {
        int[] thingies = new int[]{850896,863776};
        client.getLoans( thingies, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ArrayList<Loan> responseLoans = Loan.fromJson(response.getJSONArray("loans"));
                    loans.clear();
                    loans.addAll(responseLoans);
                    adapterLoans.notifyDataSetChanged();
                    showResultsRecyclerView();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Problem loading loans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), "Problem loading loans", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Problem loading loans", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void showResultsRecyclerView(){
        linProgressContainer.setVisibility(View.GONE);
        orvLoans.setVisibility(View.VISIBLE);
    }

    public interface OnItemSelectedListener {
        public void onLoanSelected(Loan loan);
    }
}
