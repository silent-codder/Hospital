package com.silentcodder.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class PatientRegister_1 extends AppCompatActivity {

    EditText mMobileNumber;
    Button mBtnGetOTP;
    CountryCodePicker mCpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register_1);

        mMobileNumber = findViewById(R.id.mobileNumber);
        mBtnGetOTP = findViewById(R.id.btnGetOtp);
        mCpp = findViewById(R.id.cpp);
        mCpp.registerCarrierNumberEditText(mMobileNumber);


        mBtnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PatientRegister_1.this,PatientRegisterOtp.class);
                intent.putExtra("Mobile",mCpp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
                finish();
            }
        });
    }

}