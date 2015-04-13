package com.zackhsi.kiva.fragments;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.R;

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

    @InjectView(R.id.linProgressContainer)
    LinearLayout linProgressContainer;

    @InjectView(R.id.ivCustomProgressAnimation)
    ImageView ivCustomProgressAnimation;

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
        setupLoadingAnimation();
        updateUserViews();
        return view;
    }

    private void setupLoadingAnimation() {
        ivCustomProgressAnimation.setBackgroundResource(R.drawable.custom_loading_anim);
        AnimationDrawable anim = (AnimationDrawable) ivCustomProgressAnimation.getBackground();
        anim.start();
    }

    public void updateUserViews() {
        if (!isAdded()) {
            return;
        }

        hideAnimationIfLoaded();

        tvLoanCount.setText(String.valueOf(KivaApplication.loggedInUser.lender_loan_count));
        tvLoanAmount.setText(String.valueOf(KivaApplication.loggedInUser.stats_amount_of_loans));
        tvOutstandingAmount.setText(String.valueOf(KivaApplication.loggedInUser.stats_amount_outstanding));

        tvWhereabouts.setText(KivaApplication.loggedInUser.lender_whereabouts);
        tvOccupation.setText(KivaApplication.loggedInUser.lender_occupation);
        tvWebsite.setText(KivaApplication.loggedInUser.lender_personal_url);
        tvJoinedAt.setText(joinedAt());
        tvLoanBecause.setText(KivaApplication.loggedInUser.lender_loan_because);
    }

    private void hideAnimationIfLoaded() {
        boolean isInfoLoaded = KivaApplication.loggedInUser.lender_whereabouts != null;
        if (isInfoLoaded) {
            Animation hideAnimation = new HeightAnim(linProgressContainer, 0);
            hideAnimation.setDuration(300);
            linProgressContainer.startAnimation(hideAnimation);
        }
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

    private class HeightAnim extends Animation {
        int targetHeight;
        int originalHeight;
        View view;

        public HeightAnim(View view, int targetHeight) {
            this.view = view;
            this.targetHeight = targetHeight;
            this.originalHeight = view.getHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            view.getLayoutParams().height = (int) (originalHeight + (targetHeight - originalHeight) * interpolatedTime);
            view.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
