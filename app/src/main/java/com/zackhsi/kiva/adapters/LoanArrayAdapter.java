package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.helpers.CountryIconResource;
import com.zackhsi.kiva.models.Loan;
import java.util.ArrayList;

public class LoanArrayAdapter extends RecyclerView.Adapter<LoanViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    public ArrayList<Loan> loans;

    public LoanArrayAdapter(Context context, ArrayList<Loan> loans){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.loans = loans;
    }

    @Override
    public LoanViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=inflater.inflate(R.layout.item_loan, viewGroup, false);

        LoanViewHolder holder = new LoanViewHolder(this, view, this.context);
        return holder;
    }

    @Override
    public void onBindViewHolder(LoanViewHolder holder, int i) {
        Loan loan = loans.get(i);
        holder.setItem(i);
        holder.tvCountry.setText(loan.country);
        holder.tvName.setText(loan.name);
        holder.tvUse.setText(loan.use);
        holder.tvFundedAmount.setText("$" + loan.fundedAmount);
        holder.tvFundedCurrency.setText("USD");
        holder.pbPercentFunded.setProgress(loan.percentFunded);
        holder.tvPercentFunded.setText(loan.percentFunded + "%");
        holder.tvTimeRemaining.setText(loan.getRelativePlannedExpiration());

        int iconId = new CountryIconResource(loan.countryCode.toLowerCase(), context).getIconId();

        Picasso.with(context).load(loan.imageThumbUrl())
                .noFade().fit().centerCrop().into(holder.ivImage);
        Picasso.with(context).load(iconId).into(holder.ivCountryFlag);

    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

}
