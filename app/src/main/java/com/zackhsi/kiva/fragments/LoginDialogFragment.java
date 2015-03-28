package com.zackhsi.kiva.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.zackhsi.kiva.R;

/**
 * Created by zackhsi on 3/28/15.
 */
public class LoginDialogFragment extends DialogFragment {

    public LoginDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static LoginDialogFragment newInstance(String title) {
        LoginDialogFragment frag = new LoginDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container);
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        return view;
    }
}