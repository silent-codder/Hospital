package com.silentcodder.hospital.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.chrisbanes.photoview.PhotoView;
import com.silentcodder.hospital.Model.ChildFile;
import com.silentcodder.hospital.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class ChildFileAdapter extends RecyclerView.Adapter<ChildFileAdapter.ViewHolder> {

    List<ChildFile> childFile;
    Dialog dialog;
    Context context;
    public ChildFileAdapter(List<ChildFile> childFile) {
        this.childFile = childFile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_prescription_view,parent,false);
        context = parent.getContext();
        dialog = new Dialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        long date = childFile.get(position).getTimeStamp();
        String prescriptionTxt = childFile.get(position).getPrescription();
        String imgUrl = childFile.get(position).getImageUrl();

        if (prescriptionTxt.isEmpty()){
            Picasso.get().load(imgUrl).into(holder.mImg);
            String dateString = (String) DateFormat
                    .format("MMM dd, yyyy",new Date(date)).toString();
            holder.mDate.setText(dateString);
            holder.mImg.setVisibility(View.VISIBLE);
            holder.lottieAnimationView.setVisibility(View.VISIBLE);
            holder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setContentView(R.layout.image_view_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    PhotoView photoView = dialog.findViewById(R.id.photo_view);
                    Picasso.get().load(imgUrl).into(photoView);

                    ImageView btnCancel = dialog.findViewById(R.id.btnCancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }else if (imgUrl.isEmpty()){
            holder.mPrescription.setText(prescriptionTxt);
            String dateString = (String) DateFormat
                    .format("MMM dd, yyyy",new Date(date)).toString();
            holder.mDate.setText(dateString);
        }else {
            Picasso.get().load(imgUrl).into(holder.mImg);
            holder.mPrescription.setText(prescriptionTxt);
            String dateString = (String) DateFormat
                    .format("MMM dd, yyyy",new Date(date)).toString();
            holder.mDate.setText(dateString);
            holder.mImg.setVisibility(View.VISIBLE);
            holder.lottieAnimationView.setVisibility(View.VISIBLE);
            holder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setContentView(R.layout.image_view_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    PhotoView photoView = dialog.findViewById(R.id.photo_view);
                    Picasso.get().load(imgUrl).into(photoView);
                    ImageView btnCancel = dialog.findViewById(R.id.btnCancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return childFile.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mPrescription,mDate;
        ImageView mImg;
        LottieAnimationView lottieAnimationView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.date);
            mPrescription = itemView.findViewById(R.id.prescription);
            mImg = itemView.findViewById(R.id.img);
            lottieAnimationView = itemView.findViewById(R.id.lottie);
        }
    }
}
