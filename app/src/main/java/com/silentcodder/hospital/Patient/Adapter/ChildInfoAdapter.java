package com.silentcodder.hospital.Patient.Adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.hospital.MainActivity;
import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.Patient.PatientChildFileFragment;
import com.silentcodder.hospital.PatientLogin;
import com.silentcodder.hospital.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChildInfoAdapter extends RecyclerView.Adapter<ChildInfoAdapter.ViewHolder>{

    List<ChildData> childData;
    Context context;
    Calendar calendar;
    Dialog dialog;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog pd;
    public ChildInfoAdapter(List<ChildData> childData) {
        this.childData = childData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_info_view,parent,false);
        context = parent.getContext();
        dialog = new Dialog(context);
        calendar = Calendar.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String ChildName = childData.get(position).getChildName();
        String ChildDOB = childData.get(position).getChildDOB();
        String ChildGender = childData.get(position).getChildGender();
        String FileNumber = childData.get(position).getFileNumber();
        String UserId = childData.get(position).getParentId();



        if (ChildGender.equals("Boy")){
            holder.boy.setVisibility(View.VISIBLE);
            holder.mChildGender.setText(ChildGender);
        }else if (ChildGender.equals("Girl")){
            holder.girl.setVisibility(View.VISIBLE);
            holder.mChildGender.setText(ChildGender);
        }else {
            holder.boy.setVisibility(View.VISIBLE);
            holder.mChildGender.setText(ChildGender);
        }

        holder.mFileNumber.setText(FileNumber);
        holder.mChildName.setText(ChildName);
        holder.mChildDOB.setText(ChildDOB);
        holder.mBtnFileOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new PatientChildFileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("FileNumber" , FileNumber);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

        //book appointment code here

        holder.mBtnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.book_appointment);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                ImageView btnCancel = dialog.findViewById(R.id.btnCancel);

                //dialog box cancel
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //get appointment date
                EditText mDate = dialog.findViewById(R.id.appointmentDate);
                mDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year

                        try {
                            c.setTime(new SimpleDateFormat("MMM").parse("Aug"));
                            int mMonth = c.get(Calendar.MONTH) + 1;
                            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day


                            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            SimpleDateFormat format = new SimpleDateFormat(" MMM yyyy");
                                            String date=format.format(calendar.getTime());
                                            mDate.setText(dayOfMonth + "" +date);

                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.show();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //get child name
               // EditText mChildName = dialog.findViewById(R.id.childName);
//                firebaseFirestore.collection("Child-Details").document(UserId)
//                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        String ChildName = task.getResult().getString("ChildName");
//                        mChildName.setText(ChildName);
//                    }
//                });

                //get child problem

                EditText mChildProblem = dialog.findViewById(R.id.childProblem);

                Button BtmBookAppointment = dialog.findViewById(R.id.btnBookAppointment);
                BtmBookAppointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pd.setMessage("Please wait a moments...");
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();

                       // String ApmChildName = mChildName.getText().toString();
                        String ApmProblem = mChildProblem.getText().toString();
                        String ApmDate = mDate.getText().toString();

                        if (TextUtils.isEmpty(ApmDate)){
                            Toast.makeText(context, "Select Appointment date", Toast.LENGTH_SHORT).show();
                        }else {
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("ChildName",ChildName);
                            map.put("Problem",ApmProblem);
                            map.put("AppointmentDate",ApmDate);
                            map.put("Gender",ChildGender);
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("UserId",UserId);

                            firebaseFirestore.collection("Appointments").add(map)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                pd.dismiss();
                                                Toast.makeText(context, "Get Appointment", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
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
        return childData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mChildName,mChildGender,mChildDOB,mFileNumber;
        Button mBtnFileOpen,mBtnBookAppointment;
        CircleImageView girl,boy;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mChildName = itemView.findViewById(R.id.childName);
            mChildGender = itemView.findViewById(R.id.gender);
            mChildDOB = itemView.findViewById(R.id.birthDate);
            mFileNumber = itemView.findViewById(R.id.fileNumber);
            mBtnFileOpen = itemView.findViewById(R.id.btnFileOpen);
            boy = itemView.findViewById(R.id.childImg);
            girl = itemView.findViewById(R.id.childGirlImg);
            mBtnBookAppointment = itemView.findViewById(R.id.btnBookAppointment);
        }
    }
}
