package com.silentcodder.hospital.Patient.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silentcodder.hospital.Patient.Model.Appointment;
import com.silentcodder.hospital.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.ViewHolder> {

    public List<Appointment> appointments;

    public PatientAppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_appointment_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String ChildName  = appointments.get(position).getChildName();
        String Date = appointments.get(position).getAppointmentDate();
        String Problem = appointments.get(position).getProblem();
        String Gender = appointments.get(position).getGender();

        if (Gender.equals("Boy") || Gender.equals("Other")){
            holder.boy.setVisibility(View.VISIBLE);
        }else if (Gender.equals("Girl")){
            holder.girl.setVisibility(View.VISIBLE);
        }

        holder.mChildName.setText(ChildName);
        holder.mDate.setText(Date);
        holder.mProblem.setText(Problem);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mChildName,mDate,mProblem;
        CircleImageView boy,girl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mChildName = itemView.findViewById(R.id.childName);
            mDate = itemView.findViewById(R.id.date);
            mProblem = itemView.findViewById(R.id.problem);
            boy = itemView.findViewById(R.id.childBoyImg);
            girl = itemView.findViewById(R.id.childGirlImg);
        }
    }
}
