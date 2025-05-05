package com.example.recyclabletrashclassificationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<ChatMessage> messages;
    private String currentUserId;

    public MessageAdapter(Context context, List<ChatMessage> messages, String currentUserId) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.tvMessage.setText(message.getMessage());

        if (message.getSenderId().equals(currentUserId)) {
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_sender);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tvMessage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params.removeRule(RelativeLayout.ALIGN_PARENT_START);
            holder.tvMessage.setLayoutParams(params);

            // Allow deletion for user's own messages
            holder.tvMessage.setOnLongClickListener(v -> {
                showDeleteDialog(position, message);
                return true;
            });

        } else {
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_receiver);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tvMessage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
            holder.tvMessage.setLayoutParams(params);

            // Disable long press for others' messages
            holder.tvMessage.setOnLongClickListener(null);
        }




    }
    private void showDeleteDialog(int position, ChatMessage message) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteMessageFromFirebase(message);
                    messages.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteMessageFromFirebase(ChatMessage message) {
        String chatId = generateChatId(message.getSenderId(), message.getReceiverId());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);

        ref.orderByChild("timestamp").equalTo(message.getTimestamp()).addListenerForSingleValueEvent(
                new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Optional: Log or toast error
                    }
                });
    }
    private String generateChatId(String id1, String id2) {
        return (id1.compareTo(id2) < 0) ? id1 + "_" + id2 : id2 + "_" + id1;
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }
}

