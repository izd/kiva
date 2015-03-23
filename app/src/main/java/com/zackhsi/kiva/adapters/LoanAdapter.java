package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.models.Loan;

import java.util.List;

public class LoanAdapter extends ArrayAdapter<Loan>{
    public LoanAdapter(Context context, int resource, List<Loan> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

  private static class ViewHolder {
    ImageView ivLoanBackground;
    TextView loanName;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Loan loan = getItem(position);
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_loan, parent, false);
      holder.ivLoanBackground = (ImageView) convertView.findViewById(R.id.ivLoanBackground);
      holder.loanName = (TextView) convertView.findViewById(R.id.tvLoanName);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.loanName.setText(loan.getName());
    Picasso.with(getContext()).load(loan.imageUrl()).into(holder.ivLoanBackground);

    return convertView;
  }
}
