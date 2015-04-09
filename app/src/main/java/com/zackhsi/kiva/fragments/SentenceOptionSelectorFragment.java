package com.zackhsi.kiva.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.SingleAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.paypal.android.sdk.i;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.OptionsAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SentenceOptionSelectorFragment extends Fragment {

    @InjectView(R.id.lvOptions)
    ListView lvOptions;
    OptionsAdapter optionsAdapter;
    SingleAnimationAdapter animationAdapter;

    public static SentenceOptionSelectorFragment newInstance(int currentSelectionIndex, String table) {
        SentenceOptionSelectorFragment fragmentSentenceOptionSelector = new SentenceOptionSelectorFragment();
        Bundle args = new Bundle();
//        args.putInt("someInt", someInt);
//        args.putString("someTitle", someTitle);
        fragmentSentenceOptionSelector.setArguments(args);
        return fragmentSentenceOptionSelector;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> li = new ArrayList<String>();
        li.add("List 1");
        li.add("List 2");
        li.add("List 3");
        li.add("List 4");
        li.add("List 5");
        li.add("List 5");
        li.add("List 5");
        li.add("List 5");
        li.add("List 5");

        optionsAdapter = new OptionsAdapter(getActivity(), li);

        animationAdapter = new SwingLeftInAnimationAdapter(optionsAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sentence_option_selector, container, false);
        ButterKnife.inject(this, view);

        animationAdapter.setAbsListView(lvOptions);
        lvOptions.setAdapter(animationAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
