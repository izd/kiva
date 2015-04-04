package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zackhsi.kiva.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoanViewHolder extends RecyclerView.ViewHolder {
    private LoanArrayAdapter arrAdapter;
    int position;

    @InjectView(R.id.tvActivity)
    TextView tvActivity;
    @InjectView(R.id.tvCountry)
    TextView tvCountry;
    @InjectView(R.id.ivImage)
    ImageView ivImage;
    @InjectView(R.id.tvName)
    TextView tvName;
    @InjectView(R.id.tvUse)
    TextView tvUse;
    @InjectView(R.id.tvFundedAmount)
    TextView tvFundedAmount;
    @InjectView(R.id.tvFundedCurrency)
    TextView tvFundedCurrency;
    @InjectView(R.id.pbPercentFunded)
    ProgressBar pbPercentFunded;
    @InjectView(R.id.tvPercentFunded)
    TextView tvPercentFunded;
    @InjectView(R.id.tvTimeRemaining)
    TextView tvTimeRemaining;
    public LoanViewHolder( LoanArrayAdapter arrAdapter, View  itemView, Context context) {
        super(itemView);
        ButterKnife.inject( context, itemView);
    }
    public void setItem(int i){
        this.position = i;
    }
}
