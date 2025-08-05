package com.example.recyclabletrashclassificationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageButton btnSend;

    private List<ChatMessage> chatMessages;
    private MessageAdapter adapter;
    private String receiverId, receiverName;
    private String senderId;
    private MenuItem makePaymentItem;
    private MenuItem showPaymentItem;
    int paymentval=0;

    String PublishableKey ="pk_test_51RhR1uQvKXb2BwZnT0S2L3eJ66v4KnJ2RbUUOYzNcc7VXq8jnQ7GCmj2uKPggEywUY02RTKLU4Iv4e57bM6nqzfT00AhmrRBkP";
    String SecretKey = "sk_test_51RhR1uQvKXb2BwZnjNqSlbFZEUJbPfKVMwRPUPCH5Fagfp1y3cex3GyUtauv2mmkvox7F5xdurd47Gp1lbF6O0Dy00pp05YCIg";
    String CustomerId;
    String EphericalKey;
    String ClientSecret;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        receiverId = getIntent().getStringExtra("receiverId");
        receiverName = getIntent().getStringExtra("receiverName");
        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

//payment
        PaymentConfiguration.init(this,PublishableKey);

        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            CustomerId = object.getString("id");
                            // Toast.makeText(MainActivity.this, CustomerId, Toast.LENGTH_SHORT).show();

                            getEmphericalKey();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MessageActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heeder = new HashMap<>();
                heeder.put("Authorization","Bearer "+SecretKey);


                return heeder;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

//toolbar

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatopt, menu);
        makePaymentItem = menu.findItem(R.id.make_payment);
        showPaymentItem = menu.findItem(R.id.view_transactions);
        //payment visibility for dealer only
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);
        userRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (user != null) {
                    if(user.getUserProfile().equals("5")){
                        makePaymentItem.setVisible(true);
                        showPaymentItem.setVisible(true);
                    } else if (user.getUserProfile().equals("2")) {
                        showPaymentItem.setVisible(true);
                    }
                }
                }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.make_payment) {
            //paymentFlow();
            showAmountDialog();
            return true;
        }
        else if (id == R.id.view_transactions) {
            openTransactionViewer(); // 👈 create this method next
            return true;
        }

        return super.onOptionsItemSelected(item);
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

//payment methods
private void paymentFlow() {
    if (ClientSecret == null || CustomerId == null || EphericalKey == null) {
        Toast.makeText(this, "Payment not ready. Please try again later.", Toast.LENGTH_SHORT).show();
        return;
    }
    paymentSheet.presentWithPaymentIntent(ClientSecret,new PaymentSheet.Configuration(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),new PaymentSheet.CustomerConfiguration(
            CustomerId,
            EphericalKey
    )));
}

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
            savePaymentToFirebase();
        }

    }

    private void getEmphericalKey() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("secret");
                            // Toast.makeText(MainActivity.this, CustomerId, Toast.LENGTH_SHORT).show();

                           // getClientSecret(CustomerId,EphericalKey);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heeder = new HashMap<>();
                heeder.put("Authorization","Bearer "+SecretKey);

                heeder.put("Stripe-Version","2025-07-30.basil");
                return heeder;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer",CustomerId);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void createPaymentIntent(int amountInCents) {
        if (CustomerId == null || EphericalKey == null) {
            Toast.makeText(this, "Payment not ready yet", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        ClientSecret = object.getString("client_secret");
                        paymentFlow(); // Now call payment flow
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error creating payment", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                params.put("amount", String.valueOf(amountInCents));
                params.put("currency", "usd");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void showAmountDialog() {
        EditText input = new EditText(this);
        input.setHint("Enter amount (USD)");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Make Payment")
                .setMessage("Enter amount in USD:")
                .setView(input)
                .setPositiveButton("Pay", (dialog, which) -> {
                    String amountStr = input.getText().toString().trim();
                    if (!amountStr.isEmpty()) {
                        int amountInCents = Integer.parseInt(amountStr) * 100;
                        paymentval= amountInCents/100;
                        createPaymentIntent(amountInCents);
                    } else {
                        Toast.makeText(this, "Amount required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void savePaymentToFirebase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Payments").child(generateChatId(senderId,receiverId));

        String paymentId = dbRef.push().getKey(); // Unique ID
        long amount = paymentval; // 💵 Use your actual amount in cents (e.g., $100 = 10000)
        long timestamp = System.currentTimeMillis();

        PaymentTransaction transaction = new PaymentTransaction(
                paymentId,
                senderId,
                receiverId,
                amount,
                timestamp
        );

        dbRef.child(paymentId).setValue(transaction)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Stored in DB", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "DB Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void openTransactionViewer() {
        Intent intent = new Intent(this, TransactionHistoryActivity.class);
        intent.putExtra("senderId", senderId);
        intent.putExtra("receiverId", receiverId);
        startActivity(intent);
    }


}