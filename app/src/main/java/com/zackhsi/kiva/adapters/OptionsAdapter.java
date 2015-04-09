package com.zackhsi.kiva.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        String itemText = getItem(position);
        if (itemText.contains(",")) {
            itemText = itemText.split(",")[1];
        }

        holder.tvOption.setText(itemText);
        holder.ivOptionIcon.setVisibility(View.GONE);

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.tvOption)
        TextView tvOption;

        @InjectView(R.id.ivOptionIcon)
        ImageView ivOptionIcon;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
