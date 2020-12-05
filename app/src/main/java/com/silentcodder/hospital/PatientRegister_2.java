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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class PatientRegister_2 extends AppCompatActivity {

    EditText mFirstName,mLastName,mAddress;
    Button mNext;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String UserId,mPhone;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register_2);

        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastName);
        mAddress = findViewById(R.id.address);
        mNext = findViewById(R.id.btnNext);
        pd = new ProgressDialog(this);

        mPhone = getIntent().getStringExtra("Mobile");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FirstName = mFirstName.getText().toString();
                String LastName = mLastName.getText().toString();
                String Address = mAddress.getText().toString();

                if (TextUtils.isEmpty(FirstName)){
                    Toast toast = Toast.makeText(PatientRegister_2.this, "Enter first name...",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mFirstName.requestFocus();
                }else if (TextUtils.isEmpty(LastName)){
                    Toast toast = Toast.makeText(PatientRegister_2.this, "Enter last name...",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mLastName.requestFocus();
                }else if (TextUtils.isEmpty(Address)){
                    Toast toast = Toast.makeText(PatientRegister_2.this, "Enter city / village name",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    mAddress.requestFocus();
                }else {

                    pd.setMessage("Uploading...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();

                    HashMap<String , Object> map = new HashMap<>();
                    map.put("First-Name",FirstName);
                    map.put("Last-Name",LastName);
                    map.put("Address",Address);
                    map.put("User-Id",UserId);
                    map.put("Mobile-Number",mPhone);
                    map.put("isUser","1");

                    firebaseFirestore.collection("Parent-Details").document(UserId).set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        Toast.makeText(PatientRegister_2.this, "Upload", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PatientRegister_2.this,PatientRegister_3.class);
                                        intent.putExtra("mobileNumber",mPhone);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });

    }
}