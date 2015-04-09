package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zackhsi.kiva.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OptionsAdapter extends ArrayAdapter<String> {

    public OptionsAdapter(Context context, ArrayList<String> options) {
        super(context, 0, options);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_option, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.tvOption.setText("John Doe");

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.tvOption)
        TextView tvOption;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
