package com.example.recyclabletrashclassificationapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DealerChatAdapter extends RecyclerView.Adapter<DealerChatAdapter.ViewHolder> {

    private Context context;
    private List<DealerProfileModel> dealers;

    public DealerChatAdapter(Context context, List<DealerProfileModel> dealers) {
        this.context = context;
        this.dealers = dealers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DealerProfileModel dealer = dealers.get(position);
        holder.tvId.setText("ID: " + dealer.getId());
        holder.tvEmail.setText("Email: " + dealer.getEmail());
        Glide.with(context)
                .load(dealer.getPhotoUrl())
                .placeholder(R.drawable.baseline_person_24)
                .into(holder.imgProfile);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("receiverId", dealer.getUid());
            intent.putExtra("receiverName", dealer.getName());// or dealer.getId()
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dealers.size();
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
