package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageButton btnSend;

    private List<ChatMessage> chatMessages;
    private MessageAdapter adapter;
    private String receiverId, receiverName;
    private String senderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        receiverId = getIntent().getStringExtra("receiverId");
        receiverName = getIntent().getStringExtra("receiverName");
        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(receiverName);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);

        chatMessages = new ArrayList<>();
        adapter = new MessageAdapter(this, chatMessages, senderId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadMessages();

        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                sendMessage(msg);
                etMessage.setText("");
            }
        });




    }

    private void sendMessage(String msg) {
        String chatId = generateChatId(senderId, receiverId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);
        String key = ref.push().getKey();

        ChatMessage message = new ChatMessage(senderId, receiverId, msg, System.currentTimeMillis());
        ref.child(key).setValue(message);
    }

    private void loadMessages() {
        String chatId = generateChatId(senderId, receiverId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ChatMessage message = ds.getValue(ChatMessage.class);
                    chatMessages.add(message);
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chatMessages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String generateChatId(String id1, String id2) {
        return (id1.compareTo(id2) < 0) ? id1 + "_" + id2 : id2 + "_" + id1;
    }



}