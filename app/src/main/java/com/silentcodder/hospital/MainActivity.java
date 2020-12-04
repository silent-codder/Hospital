package com.silentcodder.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.silentcodder.hospital.Counter.CounterMainActivity;
import com.silentcodder.hospital.Doctor.DoctorMainActivity;
import com.silentcodder.hospital.Patient.PatientMainActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Button mBtnPatient,mBtnHospitalStaff;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnPatient = findViewById(R.id.btnPatient);
        mBtnHospitalStaff = findViewById(R.id.btnHospitalStaff);

        dialog = new Dialog(this);

        mBtnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,PatientLogin.class));
            }
        });
        mBtnHospitalStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,HospitalStaffLogin.class));
                dialog.setContentView(R.layout.hospital_staff_selection);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                RadioButton doctor = dialog.findViewById(R.id.doctor);
                doctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.doctorImg);
                        circleImageView.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.counterImg);
                        circleImageView1.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.medicalImg);
                        circleImageView2.setVisibility(View.INVISIBLE);
                    }
                });
                RadioButton counter = dialog.findViewById(R.id.counter);
                counter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.doctorImg);
                        circleImageView.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.counterImg);
                        circleImageView1.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.medicalImg);
                        circleImageView2.setVisibility(View.INVISIBLE);
                    }
                });
                RadioButton medical = dialog.findViewById(R.id.medical);
                medical.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.doctorImg);
                        circleImageView.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.counterImg);
                        circleImageView1.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.medicalImg);
                        circleImageView2.setVisibility(View.VISIBLE);
                    }
                });

               Button btnStart = dialog.findViewById(R.id.btnStart);
               btnStart.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
                       int selectId = radioGroup.getCheckedRadioButtonId();
                       RadioButton radioButton = dialog.findViewById(selectId);
                       String Staff = radioButton.getText().toString();


                       if (selectId == -1){
                           Toast.makeText(MainActivity.this, "Please select your role...", Toast.LENGTH_SHORT).show();
                       }else {
                           Intent intent = new Intent(MainActivity.this,HospitalStaffLogin.class);
                           intent.putExtra("Role",Staff);
                           startActivity(intent);
                           dialog.dismiss();
                       }
                   }
               });

                ImageView cancel = dialog.findViewById(R.id.btnCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            startActivity(new Intent(MainActivity.this, DoctorMainActivity.class));
            finish();
        }
    }
}