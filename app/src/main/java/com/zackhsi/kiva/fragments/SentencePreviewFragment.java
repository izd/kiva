package com.zackhsi.kiva.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.zackhsi.kiva.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SentencePreviewFragment extends Fragment {
    @InjectView(R.id.tvSector)
    TextView tvSector;

    @InjectView(R.id.tvCountry)
    TextView tvCountry;

    @InjectView(R.id.tvIn)
    TextView tvIn;

    @InjectView(R.id.tvGroup)
    TextView tvGroup;

    @InjectView(R.id.tvNeed)
    TextView tvNeed;

    @InjectView(R.id.tvSentenceStart)
    TextView tvSentenceStart;


    @InjectView(R.id.btnResults)
    Button btnResults;

    private OnOptionEditListener optionEditListener;
    private OnAdvanceToResultsListener advanceToResultsListener;

    // Define the events that the fragment will use to communicate
    public interface OnOptionEditListener {
        public void onOptionEdit(String link);
    }

    public interface OnAdvanceToResultsListener {
        public void onAdvanceToResults();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        optionEditListener = (OnOptionEditListener) getActivity();
        advanceToResultsListener = (OnAdvanceToResultsListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sentence_preview, container, false);
        ButterKnife.inject(this, view);

        tvSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int baseDuration = 500;

                AnimatorSet setBtnDisappear = new AnimatorSet();
                ObjectAnimator oaBtnSlideDown = ObjectAnimator.ofFloat(btnResults, "translationY", 0f, (float) btnResults.getHeight());
                oaBtnSlideDown.setDuration(baseDuration);
                oaBtnSlideDown.setRepeatMode(ObjectAnimator.REVERSE);
                oaBtnSlideDown.setRepeatCount(0);
                oaBtnSlideDown.setInterpolator(new OvershootInterpolator());

                ObjectAnimator oaBtnFade = ObjectAnimator.ofFloat(btnResults, "alpha", 1f, 0f);
                oaBtnFade.setDuration(baseDuration);
                oaBtnFade.setRepeatCount(0);
                oaBtnFade.setInterpolator(new OvershootInterpolator());
                setBtnDisappear.playTogether(oaBtnSlideDown, oaBtnFade);

                ObjectAnimator oaTvSentenceStartFade = ObjectAnimator.ofFloat(tvSentenceStart, "alpha", 1f, 0f);

                ObjectAnimator oaTvGroupFade = ObjectAnimator.ofFloat(tvGroup, "alpha", 1f, 0f);
//                oaTvGroupFade.setStartDelay(baseSeparation * 2);

                ObjectAnimator oaTvInFade = ObjectAnimator.ofFloat(tvIn, "alpha", 1f, 0f);
//                oaTvInFade.setStartDelay(baseSeparation * 2);

                ObjectAnimator oaTvCountryFade = ObjectAnimator.ofFloat(tvCountry, "alpha", 1f, 0f);
//                oaTvCountryFade.setStartDelay(baseSeparation * 2);

                ObjectAnimator oaTvNeedFade = ObjectAnimator.ofFloat(tvNeed, "alpha", 1f, 0f);
//                oaTvNeedFade.setStartDelay(baseSeparation * 2);

                ObjectAnimator oaTvSectorFade = ObjectAnimator.ofFloat(tvSector, "alpha", 1f, 0f);

                // enlarge selected
                ObjectAnimator oaTvSectorEnlargeX = ObjectAnimator.ofFloat(tvSector, "scaleX", 1f, 1.5f);
                ObjectAnimator oaTvSectorEnlargeY = ObjectAnimator.ofFloat(tvSector, "scaleY", 1f, 1.5f);

                AnimatorSet setAll = new AnimatorSet();
                setAll.playTogether(
                        setBtnDisappear, oaTvSentenceStartFade, oaTvGroupFade, oaTvInFade,
                        oaTvCountryFade, oaTvNeedFade,
                        oaTvSectorFade,
                        oaTvSectorEnlargeX, oaTvSectorEnlargeY
                        );
                setAll.start();

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        optionEditListener.onOptionEdit("haha");
                        btnResults.clearAnimation();
                        tvSector.clearAnimation();
                    }
                }, baseDuration);
            }
        });

        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceToResultsListener.onAdvanceToResults();
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

