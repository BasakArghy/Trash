package com.example.recyclabletrashclassificationapp;

public class PaymentTransaction {
    public String paymentId;
    public String senderId;
    public String receiverId;
    public long amount;
    public long timestamp;

    public PaymentTransaction() {
        // Needed for Firebase
    }

    public PaymentTransaction(String paymentId, String senderId, String receiverId, long amount, long timestamp) {
        this.paymentId = paymentId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}

