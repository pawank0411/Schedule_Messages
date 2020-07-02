package com.example.schedule_message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule_message.MainActivity;
import com.example.schedule_message.R;
import com.example.schedule_message.model.MessageData;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<MessageData> messageDataArrayList;

    public ListAdapter(ArrayList<MessageData> messageDataArrayList, Context mContext) {
        this.mContext = mContext;
        this.messageDataArrayList = messageDataArrayList;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_layout,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.phoneNumber.setText("+91 " + messageDataArrayList.get(position).getPhoneNumber());
        holder.message.setText(messageDataArrayList.get(position).getMessage());
        holder.dateTime.setText(messageDataArrayList.get(position).getDateTime());
    }

    @Override
    public int getItemCount() {
        Toast.makeText(mContext, String.valueOf(messageDataArrayList.size()), Toast.LENGTH_SHORT).show();
        return messageDataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView phoneNumber, message, dateTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phone_number);
            message = itemView.findViewById(R.id.message);
            dateTime = itemView.findViewById(R.id.date_time);
        }
    }
}
