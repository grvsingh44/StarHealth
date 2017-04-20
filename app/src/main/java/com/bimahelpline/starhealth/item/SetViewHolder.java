package com.bimahelpline.starhealth.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bimahelpline.starhealth.R;


public class SetViewHolder extends RecyclerView.ViewHolder {

    public TextView mTvTitle, mTvBody;
    public TextView mTvDate;

    public SetViewHolder(View itemView){
        super(itemView);
        mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        mTvBody = (TextView) itemView.findViewById(R.id.tvBody);
        mTvDate = (TextView) itemView.findViewById(R.id.tvTime);

    }
}
