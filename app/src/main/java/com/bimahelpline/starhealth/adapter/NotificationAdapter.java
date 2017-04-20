package com.bimahelpline.starhealth.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimahelpline.starhealth.R;
import com.bimahelpline.starhealth.item.Item;
import com.bimahelpline.starhealth.item.SetViewHolder;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<SetViewHolder> {

    private List<Item> items = Collections.emptyList();
    private Context c;
    private Activity activity;

    public NotificationAdapter(Activity activity, List<Item> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        holder.mTvTitle.setText(items.get(position).getTitle());
        holder.mTvBody.setText(items.get(position).getBody());
        long _unixTime = items.get(position).getTime();
        String _time = new SimpleDateFormat("hh:mm a").format(new Date(_unixTime * 1000L));
        holder.mTvDate.setText(_time);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
