package com.zackhsi.kiva.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.zackhsi.kiva.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SentencePreviewFragment extends Fragment {
    @InjectView(R.id.tvSector)
    TextView tvSector;

    private OnOptionEditListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnOptionEditListener {
        public void onOptionSelected(String link);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        listener = (OnOptionEditListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sentence_preview, container, false);
        ButterKnife.inject(this, view);

        tvSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(tvSector, "rotationX", 0f, 90f);
                animation.setDuration(500);
                animation.setRepeatMode(ObjectAnimator.REVERSE);
                animation.setRepeatCount(0);
                animation.setInterpolator(new LinearInterpolator());
                animation.start();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.onOptionSelected("haha");
                    }
                }, 500);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        AnimatorSet set = new AnimatorSet();
//
//        ObjectAnimator animation = ObjectAnimator.ofFloat(tvSector, "rotationX", 0f, 90f);
//        animation.setDuration(1200);
//        animation.setRepeatMode(ObjectAnimator.REVERSE);
//        animation.setRepeatCount(ObjectAnimator.INFINITE);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.start();
    }
}

