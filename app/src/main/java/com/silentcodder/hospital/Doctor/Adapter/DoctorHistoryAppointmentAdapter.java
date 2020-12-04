package com.silentcodder.hospital.Doctor.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.silentcodder.hospital.Counter.CounterChildFile;
import com.silentcodder.hospital.Counter.Model.AppointmentHistory;
import com.silentcodder.hospital.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorHistoryAppointmentAdapter extends RecyclerView.Adapter<DoctorHistoryAppointmentAdapter.ViewHolder> {

    List<AppointmentHistory> appointmentHistory;

    public DoctorHistoryAppointmentAdapter(List<AppointmentHistory> appointmentHistory) {
        this.appointmentHistory = appointmentHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_appointment_history_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ChildName = appointmentHistory.get(position).getChildName();
        String ChildProblem = appointmentHistory.get(position).getChildProblem();
        String Gender = appointmentHistory.get(position).getChildGender();
        String Date = appointmentHistory.get(position).getChildAppointmentDate();
        String FileNumber = appointmentHistory.get(position).getChildFileNumber();

        String AppointmentHistoryId = appointmentHistory.get(position).AppointmentHistoryId;

        if (Gender.equals("Boy") || Gender.equals("Other")){
            holder.boy.setVisibility(View.VISIBLE);
        }else if (Gender.equals("Girl")){
            holder.girl.setVisibility(View.VISIBLE);
        }
        holder.mChildName.setText(ChildName);
        holder.mProblem.setText(ChildProblem);
        holder.mDate.setText(Date);

        //button child file open
        holder.mOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new CounterChildFile();
                Bundle bundle = new Bundle();
                bundle.putString("FileNumber" , FileNumber);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentHistory.size();
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
