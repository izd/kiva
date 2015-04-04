package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zackhsi.kiva.R;
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
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

}
