package com.example.recyclabletrashclassificationapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class DealerFormAdapter extends RecyclerView.Adapter<DealerFormAdapter.ViewHolder> {

private Context context;
private Activity activity;
private List<DealerApplyFormModel> modelList;

public DealerFormAdapter(Context context,Activity activity, List<DealerApplyFormModel> applicationFormModelList) {
        this.context = context;
        this.modelList = applicationFormModelList;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_application_form, parent, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    DealerApplyFormModel applicationFormModel = modelList.get(position);
        holder.textViewApplicationId.setText("ID: "+ applicationFormModel.getApplyid());
        holder.textViewEmail.setText("Email: "+ applicationFormModel.getEmail());
        holder.applyTime.setText(applicationFormModel.getDate());

        // Load image with Glide or Picasso
        Glide.with(context)
        .load(applicationFormModel.getPhotoUrl()) // Assuming userPic is URL
        .placeholder(R.drawable.baseline_person_24)
        .transform(new RoundedCorners(20))
        .into(holder.imageViewPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

        Intent intent = new Intent(context, DealerFromView.class);  // Replace NewActivity with your target Activity
        intent.putExtra("applicationId",applicationFormModel.getApplyid()); // Send the application ID
        // activity.finish();
        context.startActivity(intent);
        // activity.startActivityForResult(intent,1);


        }
        });
        }

@Override
public int getItemCount() {
        return modelList.size();
        }

public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView textViewApplicationId, textViewEmail,applyTime;
    ImageView imageViewPic;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewApplicationId = itemView.findViewById(R.id.textViewApplicationId);
        textViewEmail = itemView.findViewById(R.id.textViewEmail);
        applyTime=itemView.findViewById(R.id.textViewApplyTime);
        imageViewPic = itemView.findViewById(R.id.imageViewPic);
    }
}
}


