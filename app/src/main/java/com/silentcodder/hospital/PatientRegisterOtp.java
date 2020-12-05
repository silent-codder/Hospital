package com.silentcodder.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.silentcodder.hospital.Patient.PatientMainActivity;

import java.util.concurrent.TimeUnit;

public class PatientRegisterOtp extends AppCompatActivity {

    Button mBtnVerifyOTP;
    EditText mGetOTP;
    FirebaseAuth firebaseAuth;
    String mPhoneNumber,OtpId;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register_otp);

        mPhoneNumber = getIntent().getStringExtra("Mobile");
        mBtnVerifyOTP = findViewById(R.id.btnVerifyOTP);
        mGetOTP = findViewById(R.id.getOtp);
        firebaseAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        firebaseAuth.setLanguageCode("Eng");
        InitiateOtp();

        mBtnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGetOTP.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                }else if (mGetOTP.getText().toString().length()!=6){
                    Toast.makeText(getApplicationContext(), "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }else {
                    pd.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OtpId,mGetOTP.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void InitiateOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        OtpId = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(PatientRegisterOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Intent intent = new Intent(PatientRegisterOtp.this,PatientRegister_2.class);
                            intent.putExtra("Mobile",mPhoneNumber);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PatientRegisterOtp.this, "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}