package com.example.recyclabletrashclassificationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<PaymentTransaction> transactionList;

    public TransactionAdapter(List<PaymentTransaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        PaymentTransaction transaction = transactionList.get(position);

        holder.tvPaymentId.setText("Payment ID: " + transaction.paymentId);
        holder.tvSender.setText("Sender: " + transaction.senderId);
        holder.tvReceiver.setText("Receiver: " + transaction.receiverId);
        holder.tvAmount.setText("Amount: $" + transaction.amount);

        String formattedDate = DateFormat.getDateTimeInstance().format(new Date(transaction.timestamp));
        holder.tvTimestamp.setText("Timestamp: " + formattedDate);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaymentId, tvSender, tvReceiver, tvAmount, tvTimestamp;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaymentId = itemView.findViewById(R.id.tv_payment_id);
            tvSender = itemView.findViewById(R.id.tv_sender);
            tvReceiver = itemView.findViewById(R.id.tv_receiver);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }
}
