package com.silentcodder.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;
import com.hbb20.CountryCodePicker;

public class PatientLogin extends AppCompatActivity {

    EditText mMobileNumber;
    Button mBtnGetOTP;
    CountryCodePicker mCpp;
    TextView mBtnRegister;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        mMobileNumber = findViewById(R.id.mobileNumber);
        mBtnGetOTP = findViewById(R.id.btnGetOtp);
        mCpp = findViewById(R.id.cpp);
        mCpp.registerCarrierNumberEditText(mMobileNumber);
        mBtnRegister = findViewById(R.id.btnRegister);

        firebaseFirestore = FirebaseFirestore.getInstance();


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(PatientLogin.this,PatientRegister_1.class));

            }
        });

        mBtnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientLogin.this,PatientLoginOtp.class);
                intent.putExtra("Mobile",mCpp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
                finish();
            }
        });

    }
}