package com.silentcodder.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.hospital.Counter.CounterMainActivity;
import com.silentcodder.hospital.Doctor.DoctorMainActivity;
import com.silentcodder.hospital.Patient.PatientMainActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Button mBtnPatient,mBtnHospitalStaff;
    Dialog dialog;
    String isUser,UserId;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnPatient = findViewById(R.id.btnPatient);
        mBtnHospitalStaff = findViewById(R.id.btnHospitalStaff);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

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
        mBtnHospitalStaff.setVisibility(View.VISIBLE);
        mBtnPatient.setVisibility(View.VISIBLE);
        TextView UpperTxt = findViewById(R.id.upperTxt);
        UpperTxt.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            mBtnPatient.setVisibility(View.INVISIBLE);
            mBtnHospitalStaff.setVisibility(View.INVISIBLE);
            UpperTxt.setVisibility(View.INVISIBLE);
            TextView CenterTxt = findViewById(R.id.centerTxt);
            CenterTxt.setVisibility(View.VISIBLE);
            progressDialog.setMessage("Please wait \nSetup environment..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            firebaseFirestore.collection("Parent-Details").document(UserId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  if (task.isSuccessful()){
                      isUser = task.getResult().getString("isUser");
                      if (isUser.equals("1")){
                          //1 for patient login
                          progressDialog.dismiss();
                          startActivity(new Intent(MainActivity.this, PatientMainActivity.class));
                          finish();
                      }else if (isUser.equals("2")){
                          //2 for doctor login
                          progressDialog.dismiss();
                          startActivity(new Intent(MainActivity.this, DoctorMainActivity.class));
                          finish();
                      }else if (isUser.equals("3")){
                          // 3 for counter login
                          progressDialog.dismiss();
                          startActivity(new Intent(MainActivity.this, CounterMainActivity.class));
                          finish();
                      }
                  }
                }
            });

        }

    }
}