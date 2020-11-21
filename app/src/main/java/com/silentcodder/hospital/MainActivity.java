package com.silentcodder.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
}