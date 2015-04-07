package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoanViewHolder extends RecyclerView.ViewHolder {
    private LoanArrayAdapter arrAdapter;
    int position;
    TextView tvActivity;

    TextView tvCountry;
    ImageView ivImage;
    TextView tvName;
    TextView tvUse;
    TextView tvFundedAmount;
    TextView tvFundedCurrency;
    ProgressBar pbPercentFunded;
    TextView tvPercentFunded;
    TextView tvTimeRemaining;

    public LoanViewHolder( LoanArrayAdapter arrAdapter, View  itemView, Context context) {
        super(itemView);
        tvActivity = (TextView) itemView.findViewById(R.id.tvActivity);
        tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvUse = (TextView) itemView.findViewById(R.id.tvUse);
        tvFundedAmount = (TextView) itemView.findViewById(R.id.tvFundedAmount);
        tvFundedCurrency = (TextView) itemView.findViewById(R.id.tvFundedCurrency);
        tvPercentFunded = (TextView) itemView.findViewById(R.id.tvPercentFunded);
        tvTimeRemaining = (TextView) itemView.findViewById(R.id.tvTimeRemaining);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        pbPercentFunded = (ProgressBar) itemView.findViewById(R.id.pbPercentFunded);

    }
    public void setItem(int i){
        this.position = i;
    }
}
