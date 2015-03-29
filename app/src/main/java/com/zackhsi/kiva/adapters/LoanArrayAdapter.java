package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.models.Loan;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoanArrayAdapter extends ArrayAdapter<Loan> {
    public LoanArrayAdapter(Context context, List<Loan> objects) {
        super(context, 0, objects);
    }

    static class ViewHolder {
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

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_loan, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Loan loan = getItem(position);

        holder.tvActivity.setText(loan.activity);
        holder.tvCountry.setText(loan.country);
        holder.tvName.setText(loan.name);
        holder.tvUse.setText(loan.use);
        holder.tvFundedAmount.setText("$" + loan.fundedAmount);
        holder.tvFundedCurrency.setText("USD");
        holder.pbPercentFunded.setProgress(loan.percentFunded);
        holder.tvPercentFunded.setText(loan.percentFunded + "%");
        holder.tvTimeRemaining.setText(loan.getRelativePlannedExpiration());


        Picasso.with(getContext()).load(loan.imageThumbUrl())
                .noFade().fit().centerCrop().into(holder.ivImage);
//        holder.tvFundedAmount.setText("" + loan.fundedAmount);

        return convertView;
    }
}
