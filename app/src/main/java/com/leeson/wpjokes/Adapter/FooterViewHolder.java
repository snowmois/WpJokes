package com.leeson.wpjokes.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.leeson.wpjokes.R;

/**
 * Created by MyComputer on 2016/8/27 0027.
 */
public class FooterViewHolder extends RecyclerView.ViewHolder {
    public TextView tvRefresh;

    public FooterViewHolder(View itemView) {
        super(itemView);
        tvRefresh = (TextView) itemView.findViewById(R.id.tvRfresh);
    }
}
