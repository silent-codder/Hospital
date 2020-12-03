package com.silentcodder.hospital.Counter.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.silentcodder.hospital.Counter.Model.AppointmentId;
import com.silentcodder.hospital.Patient.Model.Appointment;
import com.silentcodder.hospital.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CounterAppointmentAdapter extends RecyclerView.Adapter<CounterAppointmentAdapter.ViewHolder> {

    public List<Appointment> appointments;
    FirebaseFirestore firebaseFirestore;
    Context context;
    ProgressDialog pd;

    public CounterAppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_appointment_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        pd = new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String AppointmentId =appointments.get(position).AppointmentId;

        String ChildName  = appointments.get(position).getChildName();
        String Date = appointments.get(position).getAppointmentDate();
        String Problem = appointments.get(position).getProblem();
        String Gender = appointments.get(position).getGender();
        String UserId = appointments.get(position).getUserId();
        String FileNumber = appointments.get(position).getFileNumber();

        if (Gender.equals("Boy") || Gender.equals("Other")){
            holder.boy.setVisibility(View.VISIBLE);
        }else if (Gender.equals("Girl")){
            holder.girl.setVisibility(View.VISIBLE);
        }

        holder.mChildName.setText(ChildName);
        holder.mDate.setText(Date);
        holder.mProblem.setText(Problem);

        holder.mBtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                pd.setMessage("Forwarding to doctor..");
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

                            firebaseFirestore.collection("Doctor-OPD").add(map)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){

                                                firebaseFirestore.collection("Appointments").document(AppointmentId).delete();
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
        return appointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView mChildName,mDate,mProblem;
       CircleImageView boy,girl;
       CardView mCardView;
       Switch mBtnSwitch;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           mChildName = itemView.findViewById(R.id.childName);
           mDate = itemView.findViewById(R.id.date);
           mProblem = itemView.findViewById(R.id.problem);
           boy = itemView.findViewById(R.id.childBoyImg);
           girl = itemView.findViewById(R.id.childGirlImg);
           mBtnSwitch = itemView.findViewById(R.id.btnSwitch);
           mCardView = itemView.findViewById(R.id.cardView);
       }
   }
}
