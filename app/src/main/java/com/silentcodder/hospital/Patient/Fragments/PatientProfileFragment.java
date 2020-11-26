package com.silentcodder.hospital.Patient.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.silentcodder.hospital.PatientRegister_3;
import com.silentcodder.hospital.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class PatientProfileFragment extends Fragment {

    TextView mUserName,mMobileNumber,mAppointmentCount,mChildCount;
    Dialog dialog;
    Button mEditProfile;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    private Uri profileImgUri;
    CircleImageView Img,mProfileImg;
    String UserId,AppointmentCount,ChildCount;

    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        dialog = new Dialog(getContext());
        mEditProfile = view.findViewById(R.id.editProfile);
        mUserName = view.findViewById(R.id.firstName);
        mMobileNumber = view.findViewById(R.id.mobileNumber);
        mProfileImg = view.findViewById(R.id.counterProfile);
        mAppointmentCount = view.findViewById(R.id.appointmentCount);
        mChildCount = view.findViewById(R.id.childCount);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(getContext());

        firebaseFirestore.collection("Parent-Details").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String FirstName = task.getResult().getString("First-Name");
                        String LastName = task.getResult().getString("Last-Name");
                        String MobileNumber = task.getResult().getString("Mobile-Number");
                        String Profile = task.getResult().getString("ProfileImgUrl");

                        mUserName.setText(FirstName + " " +LastName);
                        mMobileNumber.setText(MobileNumber);
                        Picasso.get().load(Profile).into(mProfileImg);

                    }
                });

        //get child count

        Query query = firebaseFirestore.collection("Child-Details").whereEqualTo("ParentId",UserId);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    int count  = value.size();
                    mChildCount.setText(String.valueOf(count));
                }else {
                    mChildCount.setText("0");
                }
            }
        });

        //get appointment count

        Query appointmentCount = firebaseFirestore.collection("Appointments").whereEqualTo("UserId",UserId);

        appointmentCount.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    int count  = value.size();
                    mAppointmentCount.setText(String.valueOf(count));
                }else {
                    mAppointmentCount.setText("0");
                }
            }
        });

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.edit_profile_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                //find Ids
                ImageView mBtnCancel = dialog.findViewById(R.id.btnCancel);
                TextView mSelectImg = dialog.findViewById(R.id.selectImg);
                EditText mFirstName = dialog.findViewById(R.id.firstName);
                EditText mLastName = dialog.findViewById(R.id.lastName);
                Button mBtnUpdateProfile = dialog.findViewById(R.id.btnUpdateProfile);
                Img = dialog.findViewById(R.id.img);

                //dialog button
                mBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                mSelectImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImg();
                    }
                });

                mBtnUpdateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          String FirstName = mFirstName.getText().toString();
                          String LastName = mLastName.getText().toString();

                        if (TextUtils.isEmpty(FirstName)){
                            Toast toast = Toast.makeText(getContext(),"Enter child name",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            mFirstName.requestFocus();
                        }else  if (TextUtils.isEmpty(LastName)){
                            Toast toast = Toast.makeText(getContext(),"Enter child name",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            mLastName.requestFocus();
                        }else {
                            pd.setMessage("Updating...");
                            pd.show();
                            HashMap<String ,Object> map = new HashMap<>();
                            map.put("First-Name",FirstName);
                            map.put("Last-Name",LastName);

                            firebaseFirestore.collection("Parent-Details").document(UserId).update(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                pd.dismiss();
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Update successfully..", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });
            }
        });

        return view;
    }

    private void selectImg() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(10)
                .setAspectRatio(1,1)
                .start(getContext(),this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImgUri = result.getUri();
                Img.setImageURI(profileImgUri);
               AddImg();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), "Error : " + error, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void AddImg() {
        pd.setMessage("Updating...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        StorageReference profileImgPath = storageReference.child("Profile").child(UserId + ".jpg");

        profileImgPath.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImgPath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        String ProfileUri = task.getResult().toString();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("ProfileImgUrl" , ProfileUri);

                        firebaseFirestore.collection("Parent-Details").document(UserId).update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(getContext(), "Profile Update...", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}