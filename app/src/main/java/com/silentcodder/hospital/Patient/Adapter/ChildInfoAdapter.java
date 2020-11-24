package com.silentcodder.hospital.Patient.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.R;

import java.util.List;

public class ChildInfoAdapter extends RecyclerView.Adapter<ChildInfoAdapter.ViewHolder>{

    List<ChildData> childData;

    public ChildInfoAdapter(List<ChildData> childData) {
        this.childData = childData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_info_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String ChildName = childData.get(position).getChildName();
        String ChildDOB = childData.get(position).getChildDOB();
        String ChildGender = childData.get(position).getChildGender();
        String FileNumber = childData.get(position).getFileNumber();

        holder.mFileNumber.setText(FileNumber);
        holder.mChildName.setText(ChildName);
        holder.mChildDOB.setText(ChildDOB);
        holder.mChildGender.setText(ChildGender);

    }

    @Override
    public int getItemCount() {
        return childData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mChildName,mChildGender,mChildDOB,mFileNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mChildName = itemView.findViewById(R.id.childName);
            mChildGender = itemView.findViewById(R.id.gender);
            mChildDOB = itemView.findViewById(R.id.birthDate);
            mFileNumber = itemView.findViewById(R.id.fileNumber);
        }
    }
}
