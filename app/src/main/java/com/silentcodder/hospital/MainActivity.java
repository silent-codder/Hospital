package com.silentcodder.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.silentcodder.hospital.Patient.PatientMainActivity;

public class MainActivity extends AppCompatActivity {

    Button mBtnPatient,mBtnHospitalStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnPatient = findViewById(R.id.btnPatient);
        mBtnHospitalStaff = findViewById(R.id.btnHospitalStaff);

        mBtnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,PatientLogin.class));
            }
        });
        mBtnHospitalStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HospitalStaffLogin.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            startActivity(new Intent(MainActivity.this, PatientMainActivity.class));
            fileList();
        }
    }
}