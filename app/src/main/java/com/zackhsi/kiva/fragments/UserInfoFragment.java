package com.zackhsi.kiva.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zackhsi.kiva.R;
import com.zackhsi.kiva.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zackhsi on 3/25/15.
 */
public class UserInfoFragment extends Fragment {

    @InjectView(R.id.tvWhereabouts)
    TextView tvWhereabouts;

    @InjectView(R.id.tvLoanCount)
    TextView tvLoanCount;

    @InjectView(R.id.tvLoanBecause)
    TextView tvLoanBecause;

    private User user;

    public static UserInfoFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getSerializable("user");
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.inject(this, view);
        tvWhereabouts.setText(user.getWhereabouts());
        tvLoanCount.setText(user.getLoanCount() + (user.getLoanCount() == 1 ? " loan" : " loans"));
        tvLoanBecause.setText(user.getLoanBecause());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}