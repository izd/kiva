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
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.LoanAdapter;
import com.zackhsi.kiva.models.Loan;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class LoanListViewFragment extends Fragment {

    @InjectView(R.id.olvLoans)
    ObservableListView olvLoans;

    private ArrayList<Loan> loans;
    private LoanAdapter adapterLoans;
    private OnItemSelectedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loans = new ArrayList<>();
        adapterLoans = new LoanAdapter(getActivity(), android.R.layout.simple_list_item_1, loans);
        getLoans();
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
                Toast.makeText(getActivity(), "Problem loading loans", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface OnItemSelectedListener {
        public void onLoanSelected(Loan loan);
    }

}
