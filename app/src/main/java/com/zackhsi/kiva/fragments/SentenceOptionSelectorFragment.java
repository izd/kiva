package com.zackhsi.kiva.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.SingleAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.adapters.OptionsAdapter;
import com.zackhsi.kiva.helpers.SentenceManager;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SentenceOptionSelectorFragment extends Fragment {

    @InjectView(R.id.lvOptions)
    ListView lvOptions;
    OptionsAdapter optionsAdapter;
    SingleAnimationAdapter animationAdapter;
    OnFinishOptionEditListener finishOptionEditListener;
    SentenceManager.OptionType itemBeingEdited;

    // Define the events that the fragment will use to communicate
    public interface OnFinishOptionEditListener {
        public void onFinishOptionEdit(SentenceManager.OptionType itemBeingEdited, int selectedPosition);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        finishOptionEditListener = (OnFinishOptionEditListener) getActivity();
    }

    public static SentenceOptionSelectorFragment newInstance(SentenceManager.OptionType itemBeingEdited, int currentSelectionIndex) {
        SentenceOptionSelectorFragment fragmentSentenceOptionSelector = new SentenceOptionSelectorFragment();
        Bundle args = new Bundle();
        args.putInt("itemBeingEdited", itemBeingEdited.ordinal());
        fragmentSentenceOptionSelector.setArguments(args);
        return fragmentSentenceOptionSelector;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemBeingEdited = SentenceManager.OptionType.values()[getArguments().getInt("itemBeingEdited", 0)];

        ArrayList<String> options = new ArrayList<>();
        if (itemBeingEdited == SentenceManager.OptionType.COUNTRY) {
            options.addAll(Arrays.asList(getResources().getStringArray(R.array.sentence_country)));
        } else if (itemBeingEdited == SentenceManager.OptionType.GROUP) {
            options.addAll(Arrays.asList(getResources().getStringArray(R.array.sentence_gender)));
            options.addAll(Arrays.asList(getResources().getStringArray(R.array.sentence_borrower_type)));
        } else if (itemBeingEdited == SentenceManager.OptionType.SECTOR) {
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
        lvOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvOptions.getItemAtPosition(position);
                finishOptionEditListener.onFinishOptionEdit(itemBeingEdited, position);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
