package com.silentcodder.hospital.Patient.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.hospital.AddChildFragment;
import com.silentcodder.hospital.Patient.Adapter.ChildInfoAdapter;
import com.silentcodder.hospital.Patient.Adapter.PatientAppointmentAdapter;
import com.silentcodder.hospital.Patient.Model.Appointment;
import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.PatientRegister_3;
import com.silentcodder.hospital.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;


public class PatientHomeFragment extends Fragment {


    ExtendedFloatingActionButton mBtnGetAppointment,mBtnAddChild;
    Dialog dialog;
    Calendar calendar;
    ProgressDialog pd;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;

    RecyclerView recyclerView;
    List<ChildData> childData;
    ChildInfoAdapter childInfoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_patient_home, container, false);

        mBtnGetAppointment = view.findViewById(R.id.btnGetAppointment);
        mBtnAddChild = view.findViewById(R.id.btnAddChild);
        recyclerView = view.findViewById(R.id.recycleView);
        dialog = new Dialog(getContext());
        calendar = Calendar.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        pd = new ProgressDialog(getContext());

        //Recycle view

        childData = new ArrayList<>();
        childInfoAdapter = new ChildInfoAdapter(childData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(childInfoAdapter);

        CollectionReference appointmentRef = firebaseFirestore.collection("Child-Details");

        Query query = appointmentRef.whereEqualTo("ParentId",UserId);


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        ChildData mChildData = doc.getDocument().toObject(ChildData.class);
                        childData.add(mChildData);
                        childInfoAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        mBtnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Fragment fragment = new AddChildFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });


        mBtnGetAppointment.setOnClickListener(new View.OnClickListener() {
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


                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
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
                EditText mChildName = dialog.findViewById(R.id.childName);
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

                        String ApmChildName = mChildName.getText().toString();
                        String ApmProblem = mChildProblem.getText().toString();
                        String ApmDate = mDate.getText().toString();

                        if (TextUtils.isEmpty(ApmChildName)){
                            Toast.makeText(getContext(), "Enter child name", Toast.LENGTH_SHORT).show();
                            mChildName.requestFocus();
                        }else if (TextUtils.isEmpty(ApmDate)){
                            Toast.makeText(getContext(), "Select Appointment date", Toast.LENGTH_SHORT).show();
                        }else {
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("ChildName",ApmChildName);
                            map.put("Problem",ApmProblem);
                            map.put("AppointmentDate",ApmDate);
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("UserId",UserId);

                            firebaseFirestore.collection("Appointments").add(map)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                pd.dismiss();
                                                Toast.makeText(getContext(), "Get Appointment", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });


        return view;
    }
}