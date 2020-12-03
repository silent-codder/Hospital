package com.silentcodder.hospital.Counter.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    List<ChildData> childData;

    public SearchAdapter(List<ChildData> childData) {
        this.childData = childData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_appointment_history_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return childData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mChildName,mDate,mProblem;
        CircleImageView boy,girl;
        Button mOpenFile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mChildName = itemView.findViewById(R.id.childName);
            mDate = itemView.findViewById(R.id.date);
            mProblem = itemView.findViewById(R.id.problem);
            boy = itemView.findViewById(R.id.childBoyImg);
            girl = itemView.findViewById(R.id.childGirlImg);
            mOpenFile = itemView.findViewById(R.id.btnFileOpen);
        }
    }
}
