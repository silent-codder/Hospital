package com.silentcodder.hospital;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.hospital.Patient.Fragments.PatientHomeFragment;
import com.silentcodder.hospital.Patient.PatientMainActivity;

import java.util.Calendar;
import java.util.HashMap;


public class AddChildFragment extends Fragment {

    RadioGroup radioGroup;
    RadioButton mGenderRadioBtn;
    Button mBtnNext;
    EditText mChildDOB;
    EditText mChildName;
    EditText mWeight;
    Calendar calendar;
    String gender,UserId;
    String fileNumber,mPhone;
    int day,month,year;
    ProgressDialog pd;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_child, container, false);

        radioGroup = view.findViewById(R.id.radioGroup);
        mBtnNext = view.findViewById(R.id.btnNext);
        mChildDOB = view.findViewById(R.id.childDOB);
        mChildName = view.findViewById(R.id.childName);
        mWeight = view.findViewById(R.id.childWeight);

        calendar = Calendar.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        pd = new ProgressDialog(getContext());

        firebaseFirestore.collection("Parent-Details").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String moNo = task.getResult().getString("Mobile-Number");
                        mPhone = moNo;
                    }
                });

        firebaseFirestore.collection("Child-Details").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    int count = value.size();
                    String size = String.valueOf(count);

                    if (size.isEmpty()){
                        fileNumber = "0";
                    }else {
                        fileNumber = size;
                    }

                }
            }
        });

        mChildDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month += 1;
                        mChildDOB.setText(day + "-" + month + "-" + year);


                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectId = radioGroup.getCheckedRadioButtonId();
                mGenderRadioBtn = view.findViewById(selectId);
                if (selectId == -1)
                {
                    Toast toast = Toast.makeText(getContext(),"Select child sex",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    radioGroup.requestFocus();
                }else {
                    gender = mGenderRadioBtn.getText().toString();
                }

                String ChildName = mChildName.getText().toString();
                String ChildDOB = mChildDOB.getText().toString();
                String Weight = mWeight.getText().toString();

                if (TextUtils.isEmpty(ChildName)){
                    Toast toast = Toast.makeText(getContext(),"Enter child name",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mChildName.requestFocus();
                }else if (TextUtils.isEmpty(ChildDOB)){
                    Toast toast = Toast.makeText(getContext(),"Enter child birth date",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mChildDOB.requestFocus();
                }else if (TextUtils.isEmpty(Weight)){
                    Toast toast = Toast.makeText(getContext(),"Enter weight",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mWeight.requestFocus();
                }else {
                    pd.setMessage("uploading...");
                    pd.show();

                    HashMap<String,Object> map = new HashMap<>();
                    map.put("ChildName",ChildName);
                    map.put("ChildDOB",ChildDOB);
                    map.put("ChildWeight",Weight);
                    map.put("ChildGender",gender);
                    map.put("TimeStamp",System.currentTimeMillis());
                    map.put("FileNumber", fileNumber);
                    map.put("ParentId",UserId);
                    map.put("MobileNumber",mPhone);

                    firebaseFirestore.collection("Child-Details").add(map)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        Toast.makeText(getContext(), "Upload", Toast.LENGTH_SHORT).show();
                                        Fragment fragment = new PatientHomeFragment();
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Server Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }


}