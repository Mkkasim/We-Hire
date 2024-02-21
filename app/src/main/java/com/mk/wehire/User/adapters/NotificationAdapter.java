package com.mk.wehire.User.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.wehire.R;
import com.mk.wehire.User.models.ApplicationModel;
import com.mk.wehire.User.models.IntershipModel;
import com.mk.wehire.User.models.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    ArrayList<Notification> list = new ArrayList<>();

    public NotificationAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }

    public void filterList(ArrayList<Notification> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.items_notifications,parent,false);
        return new NotificationViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.body.setText(list.get(position).getBody());
        Log.d("TAG2", "onBindViewHolder: List-----"+list.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView title,body;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);

        }
    }
}
