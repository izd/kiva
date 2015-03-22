package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.zackhsi.kiva.models.Loan;

import java.util.List;

public class LoanAdapter extends ArrayAdapter{
  public LoanAdapter(Context context, int resource, List<Loan> objects) {
    super(context, android.R.layout.simple_list_item_1, objects);
  }


}
