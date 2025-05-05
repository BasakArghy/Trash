package com.example.recyclabletrashclassificationapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CollectorChatAdapter extends RecyclerView.Adapter<CollectorChatAdapter.ViewHolder> {

    private Context context;
    private List<ProfileModel> collectors;

    public CollectorChatAdapter(Context context, List<ProfileModel> collectors) {
        this.context = context;
        this.collectors = collectors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileModel collector = collectors.get(position);
        holder.tvId.setText("ID: " + collector.getId());
        holder.tvEmail.setText("Email: " + collector.getEmail());

        // Use Glide or Picasso to load image
        Glide.with(context)
                .load(collector.getPhotoUrl())
                .placeholder(R.drawable.baseline_person_24)
                .into(holder.imgProfile);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("receiverId", collector.getUid());
            intent.putExtra("receiverName", collector.getName());// or dealer.getId()
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return collectors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvEmail;
        ImageView imgProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_collector_id);
            tvEmail = itemView.findViewById(R.id.tv_collector_email);
            imgProfile = itemView.findViewById(R.id.img_profile);
        }
    }
}

