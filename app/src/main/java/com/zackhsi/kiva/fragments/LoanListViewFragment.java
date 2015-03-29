package com.zackhsi.kiva.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.LoanArrayAdapter;
import com.zackhsi.kiva.models.Loan;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class LoanListViewFragment extends Fragment {

    @InjectView(R.id.olvLoans)
    ObservableListView olvLoans;

    private KivaClient client;
    private ArrayList<Loan> loans;
    private LoanArrayAdapter adapterLoans;
    private OnItemSelectedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = KivaApplication.getRestClient();
        loans = new ArrayList<>();
        adapterLoans = new LoanArrayAdapter(getActivity(), loans);
        getLoans(null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view_loan, container, false);
        ButterKnife.inject(this, view);

        Activity activity = getActivity();
        if (activity instanceof ObservableScrollViewCallbacks) {
            olvLoans.setScrollViewCallbacks((ObservableScrollViewCallbacks) activity);
        }
        olvLoans.setAdapter(adapterLoans);

        return view;
    }

    public View getListView() {
        return olvLoans;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement listener");
        }
    }

    @OnItemClick(R.id.olvLoans)
    public void launchDetailActivity(int position) {
        listener.onLoanSelected((Loan) olvLoans.getItemAtPosition(position));
    }

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
