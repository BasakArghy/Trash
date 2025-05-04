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

public class CollectorProfileAdapter extends RecyclerView.Adapter<CollectorProfileAdapter.ViewHolder> {

    private Context context;
    private List<ProfileModel> collectors;

    public CollectorProfileAdapter(Context context, List<ProfileModel> collectors) {
        this.context = context;
        this.collectors = collectors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dealer_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileModel model = collectors.get(position);
        holder.tvId.setText("ID: " + model.getId());
        holder.tvEmail.setText("Email: " + model.getEmail());

        Glide.with(context).load(model.getPhotoUrl()).into(holder.imgProfile);

        holder.btnDismiss.setOnClickListener(v -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                    .child(model.getUid())
                    .child("userProfile");
            databaseReference.setValue("3");
            //delete from firebase storage
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Profiles").child(model.getUid());
// delete data from Firebase storage
            FirebaseStorage.getInstance("gs://chat-55084.appspot.com").getReference("Applications").child(model.getUid()).delete();


            reference.removeValue();
            Toast.makeText(context, "Dealer dismissed", Toast.LENGTH_SHORT).show();
            collectors.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return collectors.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvId, tvEmail;
        Button btnDismiss;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvDealerId);
            tvEmail = itemView.findViewById(R.id.tvDealerEmail);
            imgProfile = itemView.findViewById(R.id.ivDealerPic);
            btnDismiss = itemView.findViewById(R.id.btnDismiss);
        }
    }
}
