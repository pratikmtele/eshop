package com.practice.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.e_commerce_app.Models.NotificationModel;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{

    ArrayList<NotificationModel> list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_notification_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationModel notificationModel = list.get(position);
        holder.heading.setText(notificationModel.getHeading());
        holder.desc.setText(notificationModel.getDesc());
        holder.time.setText(notificationModel.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView heading, desc, time;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.notification_heading);
            desc = itemView.findViewById(R.id.notification_desc);
            time = itemView.findViewById(R.id.notification_time);
        }
    }
}
