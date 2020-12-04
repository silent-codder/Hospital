package com.silentcodder.hospital.Doctor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.hospital.Counter.CounterChildFile;
import com.silentcodder.hospital.Counter.Model.AppointmentHistory;
import com.silentcodder.hospital.Doctor.DoctorChildFileFragment;
import com.silentcodder.hospital.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.ViewHolder>{

    List<AppointmentHistory> appointmentHistory;
    FirebaseFirestore firebaseFirestore;
    Context context;
    ProgressDialog pd;

    public DoctorAppointmentAdapter(List<AppointmentHistory> appointmentHistory) {
        this.appointmentHistory = appointmentHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appointment_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        pd = new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ChildName = appointmentHistory.get(position).getChildName();
        String ChildProblem = appointmentHistory.get(position).getChildProblem();
        String Gender = appointmentHistory.get(position).getChildGender();
        String Date = appointmentHistory.get(position).getChildAppointmentDate();
        String FileNumber = appointmentHistory.get(position).getChildFileNumber();
        String UserId = appointmentHistory.get(position).getID();
        String Problem = appointmentHistory.get(position).getChildProblem();

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
                Fragment fragment = new DoctorChildFileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("FileNumber", FileNumber);
                bundle.putString("UserId",UserId);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        //button to done appointment
        holder.mBtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                pd.setMessage("Appointment Complete");
                pd.show();

                    firebaseFirestore.collection("Parent-Details").document(UserId)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String ParentFirstName = task.getResult().getString("First-Name");
                            String ParentLastName = task.getResult().getString("Last-Name");
                            String  ParentAddress = task.getResult().getString("Address");
                            String ParentMobile = task.getResult().getString("Mobile-Number");

                            if (isChecked){
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("ParentName",ParentFirstName + " " + ParentLastName);
                                map.put("ParentAddress",ParentAddress);
                                map.put("ParentMobile",ParentMobile);
                                map.put("ID",UserId);
                                map.put("ChildName",ChildName);
                                map.put("ChildGender",Gender);
                                map.put("ChildProblem",Problem);
                                map.put("ChildFileNumber",FileNumber);
                                map.put("ChildAppointmentDate",Date);
                                map.put("TimeStamp",System.currentTimeMillis());

                                firebaseFirestore.collection("History-Appointments").add(map)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()){

                                                    firebaseFirestore.collection("Doctor-OPD").document(AppointmentHistoryId).delete();
                                                    pd.dismiss();
                                                    Toast toast = Toast.makeText(context, "File forward to doctor", Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER,0,0);
                                                    toast.show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
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
        Switch mBtnSwitch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mChildName = itemView.findViewById(R.id.childName);
            mDate = itemView.findViewById(R.id.date);
            mProblem = itemView.findViewById(R.id.problem);
            boy = itemView.findViewById(R.id.childBoyImg);
            girl = itemView.findViewById(R.id.childGirlImg);
            mOpenFile = itemView.findViewById(R.id.btnFileOpen);
            mBtnSwitch = itemView.findViewById(R.id.btnSwitch);
        }
    }
}
