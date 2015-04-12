package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.helpers.CountryIconResource;
import com.zackhsi.kiva.helpers.RoundedCornerTransformation;
import com.zackhsi.kiva.models.Loan;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LoanArrayAdapter extends RecyclerView.Adapter<LoanViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    public ArrayList<Loan> loans;
    private DecimalFormat formatter;

    public LoanArrayAdapter(Context context, ArrayList<Loan> loans){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.loans = loans;
        formatter = new DecimalFormat("#,###,###");
    }

    @Override
    public LoanViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=inflater.inflate(R.layout.item_loan, viewGroup, false);

        LoanViewHolder holder = new LoanViewHolder(this, view, this.context);
        Drawable d = context.getResources().getDrawable(R.drawable.custom_progress_bar);
        holder.pbPercentFunded.setProgressDrawable(d);
        return holder;
    }

    @Override
    public void onBindViewHolder(LoanViewHolder holder, int i) {
        Loan loan = loans.get(i);
        holder.setItem(i);
        if (loan.town != null) {
            holder.tvLocation.setText(loan.town + ", " + loan.country);
        } else {
            holder.tvLocation.setText(loan.country);
        }
        holder.tvName.setText(loan.name);
        holder.tvUse.setText(loan.longUse());
        holder.tvFundedAmount.setText("$" + formatter.format(loan.fundedAmount));
        holder.tvLoanAmount.setText(" of $" + formatter.format(loan.loanAmount));
        holder.pbPercentFunded.setProgress(loan.percentFunded);
        holder.tvPercentFunded.setText(loan.percentFunded + "% funded");
        holder.tvTimeRemaining.setText("Ends " + loan.getRelativePlannedExpiration());

        int iconId = new CountryIconResource(loan.countryCode.toLowerCase(), context).getIconId();
        Drawable flagIcon = (Drawable) context.getResources().getDrawable(iconId);
        Picasso.with(context).load(loan.imageThumbUrl())
                .transform(new RoundedCornerTransformation()).noFade().fit().centerCrop().into(holder.ivImage);
        Picasso.with(context).load(iconId).noFade().into(holder.ivCountryFlag);

    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

}
