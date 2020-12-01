package com.silentcodder.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.hospital.Counter.CounterMainActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalStaffLogin extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mBtnSubmit;
    ProgressDialog pd;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    String RoleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_staff_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mBtnSubmit = findViewById(R.id.btnSubmit);
        pd = new ProgressDialog(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        RoleId = getIntent().getStringExtra("Role");


        if (RoleId.equals("Doctor")){
            CircleImageView circleImageView = findViewById(R.id.doctorImg);
            circleImageView.setVisibility(View.VISIBLE);
            TextView textView = findViewById(R.id.staff);
            textView.setText(RoleId);
        }else if (RoleId.equals("Counter")){
            CircleImageView circleImageView = findViewById(R.id.counterImg);
            circleImageView.setVisibility(View.VISIBLE);
            TextView textView = findViewById(R.id.staff);
            textView.setText(RoleId);
        }else {
            CircleImageView circleImageView = findViewById(R.id.medicalImg);
            circleImageView.setVisibility(View.VISIBLE);
            TextView textView = findViewById(R.id.staff);
            textView.setText(RoleId);
        }

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = mEmail.getText().toString();
                String Password = mPassword.getText().toString();

                if (TextUtils.isEmpty(Email) && TextUtils.isEmpty(Password)){
                    Toast.makeText(HospitalStaffLogin.this, "Fill up information", Toast.LENGTH_SHORT).show();
                }else {
                    pd.setMessage("Wait a moment...");
                    pd.show();
                    if (Email.equals("counter@gmail.com")&& RoleId.equals("Counter")){
                        counterLogin(Email,Password);
                    }else if (Email.equals("doctor@gmail.com")&& RoleId.equals("Doctor")){
                        doctorLogin(Email,Password);
                    }else if (Email.equals("medical@gmail.com")&& RoleId.equals("Medical")){
                        medicalLogin(Email,Password);
                    }else {
                        pd.dismiss();
                        Toast.makeText(HospitalStaffLogin.this, "Incorrect information", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void medicalLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(HospitalStaffLogin.this, "Medical", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(HospitalStaffLogin.this,CounterMainActivity.class);
//                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(HospitalStaffLogin.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doctorLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(HospitalStaffLogin.this, "Doctor", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(HospitalStaffLogin.this,CounterMainActivity.class);
//                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(HospitalStaffLogin.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void counterLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Intent intent = new Intent(HospitalStaffLogin.this,CounterMainActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(HospitalStaffLogin.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}