package com.example.recyclabletrashclassificationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DealerProfileAdapter extends RecyclerView.Adapter<DealerProfileAdapter.ViewHolder> {

    private Context context;
    private List<DealerProfileModel> dealerList;

    public DealerProfileAdapter(Context context, List<DealerProfileModel> dealerList) {
        this.context = context;
        this.dealerList = dealerList;
    }

    @NonNull
    @Override
    public DealerProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dealer_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealerProfileAdapter.ViewHolder holder, int position) {
        DealerProfileModel dealer = dealerList.get(position);
        holder.tvId.setText("ID: " + dealer.getId());
        holder.tvEmail.setText("Email: " + dealer.getEmail());
        Glide.with(context).load(dealer.getPhotoUrl()).placeholder(R.drawable.baseline_person_24).into(holder.imageView);

        holder.btnDismiss.setOnClickListener(v -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                    .child(dealer.getUid())
                    .child("userProfile");
            databaseReference.setValue("6");
            //delete from firebase storage
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DealerProfiles").child(dealer.getUid());

            FirebaseStorage.getInstance("gs://chat-55084.appspot.com").getReference("Applications").child(dealer.getUid()).delete();

            reference.removeValue();
            Toast.makeText(context, "Dealer dismissed", Toast.LENGTH_SHORT).show();
            dealerList.remove(position);
            notifyItemRemoved(position);

    });
    }


    @Override
    public int getItemCount() {
        return dealerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvEmail;
        ImageView imageView;
        Button btnDismiss;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvDealerId);
            tvEmail = itemView.findViewById(R.id.tvDealerEmail);
            imageView = itemView.findViewById(R.id.ivDealerPic);
            btnDismiss = itemView.findViewById(R.id.btnDismiss);
        }
    }
}
