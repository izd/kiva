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
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SentenceOptionSelectorFragment extends Fragment {

    @InjectView(R.id.lvOptions)
    ListView lvOptions;
    OptionsAdapter optionsAdapter;
    SingleAnimationAdapter animationAdapter;

    public static SentenceOptionSelectorFragment newInstance(SentencePreviewFragment.OptionType itemBeingEdited, int currentSelectionIndex) {
        SentenceOptionSelectorFragment fragmentSentenceOptionSelector = new SentenceOptionSelectorFragment();
        Bundle args = new Bundle();
        args.putInt("itemBeingEdited", itemBeingEdited.ordinal());
//        args.putString("someTitle", someTitle);
        fragmentSentenceOptionSelector.setArguments(args);
        return fragmentSentenceOptionSelector;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SentencePreviewFragment.OptionType itemBeingEdited = SentencePreviewFragment.OptionType.values()[getArguments().getInt("itemBeingEdited", 0)];


        ArrayList<String> options = new ArrayList<>();
        if (itemBeingEdited == SentencePreviewFragment.OptionType.COUNTRY) {
            options.addAll(Arrays.asList(getResources().getStringArray(R.array.sentence_country)));
        } else if (itemBeingEdited == SentencePreviewFragment.OptionType.GROUP) {
            options.addAll(Arrays.asList(getResources().getStringArray(R.array.sentence_gender)));
        } else if (itemBeingEdited == SentencePreviewFragment.OptionType.SECTOR) {
            options.addAll(Arrays.asList(getResources().getStringArray(R.array.sentence_sector)));
        }

        optionsAdapter = new OptionsAdapter(getActivity(), options);

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
