package com.silentcodder.hospital.Counter.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.silentcodder.hospital.Counter.Model.AppointmentHistory;
import com.silentcodder.hospital.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CounterAppointmentHistoryAdapter extends RecyclerView.Adapter<CounterAppointmentHistoryAdapter.ViewHolder>{

    List<AppointmentHistory> appointmentHistory;

    public CounterAppointmentHistoryAdapter(List<AppointmentHistory> appointmentHistory) {
        this.appointmentHistory = appointmentHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_appointment_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String ChildName = appointmentHistory.get(position).getChildName();
        String ChildProblem = appointmentHistory.get(position).getChildProblem();
        String Gender = appointmentHistory.get(position).getChildGender();

        if (Gender.equals("Boy") || Gender.equals("Other")){
            holder.boy.setVisibility(View.VISIBLE);
        }else if (Gender.equals("Girl")){
            holder.girl.setVisibility(View.VISIBLE);
        }
        holder.mChildName.setText(ChildName);
        holder.mProblem.setText(ChildProblem);
    }

    @Override
    public int getItemCount() {
        return appointmentHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mChildName,mDate,mProblem;
        CircleImageView boy,girl;
        CardView mCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mChildName = itemView.findViewById(R.id.childName);
            mDate = itemView.findViewById(R.id.date);
            mProblem = itemView.findViewById(R.id.problem);
            boy = itemView.findViewById(R.id.childBoyImg);
            girl = itemView.findViewById(R.id.childGirlImg);
            mCardView = itemView.findViewById(R.id.cardView);
        }
    }
}
