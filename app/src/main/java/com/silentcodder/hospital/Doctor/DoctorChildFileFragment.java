package com.silentcodder.hospital.Doctor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.silentcodder.hospital.Adapter.ChildFileAdapter;
import com.silentcodder.hospital.Counter.Adapter.CounterAppointmentHistoryAdapter;
import com.silentcodder.hospital.Counter.Model.AppointmentHistory;
import com.silentcodder.hospital.Model.ChildFile;
import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class DoctorChildFileFragment extends Fragment {
    TextView mChildName,mChildGender,mChildDOB,mFileNumber;
    CircleImageView boy,girl;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String FileNumber,UserId,Id,File;
    ProgressDialog pd;
    Button mBtnAddSymptom,mBtnAddPrescription;
    Dialog dialog;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ChildFileAdapter childFileAdapter;
    List<ChildFile> childFile;

    StorageReference storageReference;
    private Uri profileImgUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_child_file, container, false);

        mChildName = view.findViewById(R.id.childName);
        mChildGender = view.findViewById(R.id.gender);
        mChildDOB = view.findViewById(R.id.birthDate);
        mFileNumber = view.findViewById(R.id.fileNumber);
        boy = view.findViewById(R.id.childBoyImg);
        girl = view.findViewById(R.id.childGirlImg);
        recyclerView = view.findViewById(R.id.recycleView);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        firebaseFirestore = FirebaseFirestore.getInstance();

        mBtnAddPrescription = view.findViewById(R.id.btnAddPrescription);
        mBtnAddSymptom = view.findViewById(R.id.btnAddSymptoms);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(getContext());
        pd = new ProgressDialog(getContext());
        pd.setMessage("Fetching data...");
        pd.show();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            FileNumber = bundle.getString("FileNumber");
            Id = bundle.getString("UserId");
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        firebaseFirestore.collection("Child-Details")
                .whereEqualTo("FileNumber",FileNumber).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()){
                            List<DocumentSnapshot> list = task.getResult().getDocuments();
                            if (list!=null && list.size()>0){
                                for (DocumentSnapshot doc : list){
                                    ChildData childData = new ChildData();
                                    String Name = childData.setChildName(doc.getString("ChildName"));
                                    String Gender = childData.setChildGender(doc.getString("ChildGender"));
                                    String DOB = childData.setChildDOB(doc.getString("ChildDOB"));
                                    String FileNo = childData.setFileNumber(doc.getString("FileNumber"));
                                    String Id = childData.setParentId(doc.getString("ParentId"));
                                    UserId = Id;
                                    File = FileNo;

                                    if (Gender.equals("Boy")){
                                        boy.setVisibility(View.VISIBLE);
                                        mChildGender.setText(Gender);
                                    }else if (Gender.equals("Girl")){
                                        girl.setVisibility(View.VISIBLE);
                                        mChildGender.setText(Gender);
                                    }else {
                                        boy.setVisibility(View.VISIBLE);
                                        mChildGender.setText(Gender);
                                    }

                                    mChildName.setText(Name);
                                    mChildDOB.setText(DOB);
                                    mFileNumber.setText(FileNo);
                                    pd.dismiss();
                                }
                            }
                        }
                    }
                });

        loadData();


        //buttons
        mBtnAddSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AddSymptoms();
            }
        });

        mBtnAddPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPrescription();
            }
        });

        return view;
    }

    private void AddSymptoms() {
        dialog.setContentView(R.layout.add_symptom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText Symptoms = dialog.findViewById(R.id.symptoms);
        Button BtnAddImg = dialog.findViewById(R.id.btnAddImage);
        Button BtnDone = dialog.findViewById(R.id.btnDone);

        BtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symptoms = Symptoms.getText().toString();
                if (TextUtils.isEmpty(symptoms)){
                    Symptoms.requestFocus();
                    Toast.makeText(getContext(), "Add Symptom", Toast.LENGTH_SHORT).show();
                }else {
                    pd.setMessage("wait..");
                    pd.show();
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("Symptoms",symptoms);
                    map.put("TimeStamp",System.currentTimeMillis());
                    map.put("UserId",UserId);
                    firebaseFirestore.collection("Child-File").document(String.valueOf(System.currentTimeMillis())).set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Add Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        BtnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });
    }

    private void AddPrescription() {
        dialog.setContentView(R.layout.add_prescription_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText Prescription = dialog.findViewById(R.id.prescription);
        Button BtnAddImg = dialog.findViewById(R.id.btnAddImage);
        Button BtnDone = dialog.findViewById(R.id.btnDone);

        BtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prescription = Prescription.getText().toString();
                if (TextUtils.isEmpty(prescription)){
                    Prescription.requestFocus();
                    Toast.makeText(getContext(), "Add Symptom", Toast.LENGTH_SHORT).show();
                }else {
                    pd.setMessage("wait..");
                    pd.show();
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("Prescription",prescription);
                    map.put("TimeStamp",System.currentTimeMillis());
                    map.put("UserId",UserId);
                    map.put("ImageUrl","");
                    firebaseFirestore.collection("Child-File").document(String.valueOf(System.currentTimeMillis())).set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Add Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        BtnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(false);
        childFile = new ArrayList<>();
        childFileAdapter = new ChildFileAdapter(childFile);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(childFileAdapter);

        CollectionReference appointmentRef = firebaseFirestore.collection("Child-File");



        Query query = appointmentRef.whereEqualTo("FileNumber",File);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        ChildFile childFile1 = doc.getDocument().toObject(ChildFile.class);
                        childFile.add(childFile1);
                        childFileAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void selectImg() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(40)
                .start(getContext(),this);
    }

    private void AddImg() {
        pd.setMessage("Updating...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        StorageReference profileImgPath = storageReference.child("Profile").child(System.currentTimeMillis() + ".jpg");

        profileImgPath.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImgPath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        String ProfileUri = task.getResult().toString();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("ImageUrl" , ProfileUri);
                        map.put("TimeStamp",System.currentTimeMillis());
                        map.put("UserId",UserId);
                        map.put("Prescription","");

                        firebaseFirestore.collection("Child-File").document(String.valueOf(System.currentTimeMillis())).set(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Image add successfully", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImgUri = result.getUri();
//                Img.setImageURI(profileImgUri);
                AddImg();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), "Error : " + error, Toast.LENGTH_SHORT).show();
            }

        }
    }

}