package com.zackhsi.kiva.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.models.User;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zackhsi on 3/25/15.
 */
public class UserInfoFragment extends Fragment {

    @InjectView(R.id.tvLoanCount)
    TextView tvLoanCount;

    @InjectView(R.id.tvLoanAmount)
    TextView tvLoanAmount;

    @InjectView(R.id.tvOutstandingAmount)
    TextView tvOutstandingAmount;

    @InjectView(R.id.tvWhereabouts)
    TextView tvWhereabouts;

    @InjectView(R.id.tvOccupation)
    TextView tvOccupation;

    @InjectView(R.id.tvWebsite)
    TextView tvWebsite;

    @InjectView(R.id.tvJoinedAt)
    TextView tvJoinedAt;

    @InjectView(R.id.tvLoanBecause)
    TextView tvLoanBecause;

    public static UserInfoFragment newInstance() {
        Bundle args = new Bundle();
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.inject(this, view);
        updateUserViews();
        return view;
    }

    public void updateUserViews() {
        if (!isAdded()) {
            return;
        }

        tvLoanCount.setText(String.valueOf(KivaApplication.loggedInUser.lender_loan_count));
        tvLoanAmount.setText(String.valueOf(KivaApplication.loggedInUser.stats_amount_of_loans));
        tvOutstandingAmount.setText(String.valueOf(KivaApplication.loggedInUser.stats_amount_outstanding));

        tvWhereabouts.setText(KivaApplication.loggedInUser.lender_whereabouts);
        tvOccupation.setText(KivaApplication.loggedInUser.lender_occupation);
        tvWebsite.setText(KivaApplication.loggedInUser.lender_personal_url);
        tvJoinedAt.setText(joinedAt());
        tvLoanBecause.setText(KivaApplication.loggedInUser.lender_loan_because);
    }

    private String joinedAt() {
        if (KivaApplication.loggedInUser.lender_member_since == null) {
            return "";
        }
        Date memberSince = KivaApplication.loggedInUser.lender_member_since;
        return "Joined " + DateUtils.getRelativeTimeSpanString(memberSince.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
