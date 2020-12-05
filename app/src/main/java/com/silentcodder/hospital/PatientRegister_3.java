package com.silentcodder.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.hospital.Patient.PatientMainActivity;

import java.util.Calendar;
import java.util.HashMap;

public class PatientRegister_3 extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register_3);

        findIds();

        calendar = Calendar.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        pd = new ProgressDialog(this);


        mPhone = getIntent().getStringExtra("mobileNumber");

        firebaseFirestore.collection("Child-Details").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    int count = value.size();
                    String size = String.valueOf(count);

                    if (size.isEmpty()){
                        fileNumber = "1";
                    }else {
                        fileNumber = size;
                    }

                }
            }
        });

        mChildDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(PatientRegister_3.this,new DatePickerDialog.OnDateSetListener() {
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
                mGenderRadioBtn = findViewById(selectId);
                if (selectId == -1)
                {
                    Toast toast = Toast.makeText(PatientRegister_3.this,"Select child sex",Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(PatientRegister_3.this,"Enter child name",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mChildName.requestFocus();
                }else if (TextUtils.isEmpty(ChildDOB)){
                    Toast toast = Toast.makeText(PatientRegister_3.this,"Enter child birth date",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mChildDOB.requestFocus();
                }else if (TextUtils.isEmpty(Weight)){
                    Toast toast = Toast.makeText(PatientRegister_3.this,"Enter weight",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mWeight.requestFocus();
                }else {
                    pd.setMessage("uploading...");
                    pd.setCanceledOnTouchOutside(false);
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
                                        Toast.makeText(PatientRegister_3.this, "Upload", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PatientRegister_3.this, PatientMainActivity.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PatientRegister_3.this, "Server Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void findIds() {
        radioGroup = findViewById(R.id.radioGroup);
        mBtnNext = findViewById(R.id.btnNext);
        mChildDOB = findViewById(R.id.childDOB);
        mChildName = findViewById(R.id.childName);
        mWeight = findViewById(R.id.childWeight);
    }
}