package com.zackhsi.kiva.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.activities.LoanDetailActivity;
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

    @InjectView(R.id.scroll)
    ObservableRecyclerView orvLoans;

    private KivaClient client;
    private ArrayList<Loan> loans;
    private LoanArrayAdapter adapterLoans;
    private OnItemSelectedListener listener;
    private LinearLayoutManager manager;

    public static LoanListViewFragment newInstance() {

        // take in arguments here and set them as class variables

        return new LoanListViewFragment();
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
                        startActivity(i);
                    }
                })
        );
        // TODO: progress bar

        return view;
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
        // TODO: read class variables here from newInstance()
        getLoans(null, null);
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

    public void getLoans(String region, String sector) {
        client.searchUnfundedLoans(region, sector, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ArrayList<Loan> responseLoans = Loan.fromJson(response.getJSONArray("loans"));
                    loans.clear();
                    loans.addAll(responseLoans);
                    adapterLoans.notifyDataSetChanged();
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

    public interface OnItemSelectedListener {
        public void onLoanSelected(Loan loan);
    }
}
