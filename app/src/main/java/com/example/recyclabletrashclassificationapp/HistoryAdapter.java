package com.example.recyclabletrashclassificationapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.recyclabletrashclassificationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<HistoryModel> historyList;

    public HistoryAdapter(Context context, List<HistoryModel> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryModel history = historyList.get(position);
        holder.txtName.setText(history.getName());
        holder.txtTime.setText(history.getTime());

        // Load image using Glide
        Glide.with(context).load(history.getPic()).transform(new RoundedCorners(20)).into(holder.imgPic);

        // Set long press listener to show the delete confirmation dialog
        holder.itemView.setOnLongClickListener(v -> {
            showDeleteDialog(history.getHistoryId(),history.getPic()); // Show the dialog
            return true; // Indicate the event was consumed
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtTime;
        ImageView imgPic;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgPic = itemView.findViewById(R.id.imgPic);
        }
    }
    private void showDeleteDialog(String historyId,String pic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete History");
        builder.setMessage("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteHistoryItem(historyId,pic);  // Call method to delete the item from Firebase and RecyclerView
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss(); // Close the dialog
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteHistoryItem(String historyId,String pic) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();


     


        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("history");

        // Delete from Firebase
        historyRef.child(userId).child(historyId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Successfully deleted from Firebase, remove from RecyclerView list
                    int position = findHistoryItemPosition(historyId);
                    if (position != -1) {
                        historyList.remove(position);
                        notifyItemRemoved(position); // Update RecyclerView
                    }
                    Toast.makeText(context.getApplicationContext(), "History item deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context.getApplicationContext(), "Failed to delete history item", Toast.LENGTH_SHORT).show();
                });

        //delete from firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://chat-55084.appspot.com");
        StorageReference storageRef = storage.getReferenceFromUrl(pic);
        storageRef.delete();
    }

    // Helper method to find the position of the history item in the list
    private int findHistoryItemPosition(String historyId) {
        for (int i = 0; i < historyList.size(); i++) {
            if (historyList.get(i).getHistoryId().equals(historyId)) {
                return i;
            }
        }
        return -1;
    }


}
