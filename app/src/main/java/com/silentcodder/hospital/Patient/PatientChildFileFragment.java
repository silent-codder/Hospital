package com.silentcodder.hospital.Patient;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.R;

import java.util.BitSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PatientChildFileFragment extends Fragment {

    TextView mChildName,mChildGender,mChildDOB,mFileNumber;
    CircleImageView boy,girl;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId,FileNumber;
    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_patient_child_file, container, false);

       mChildName = view.findViewById(R.id.childName);
       mChildGender = view.findViewById(R.id.gender);
       mChildDOB = view.findViewById(R.id.birthDate);
       mFileNumber = view.findViewById(R.id.fileNumber);
       boy = view.findViewById(R.id.childBoyImg);
       girl = view.findViewById(R.id.childGirlImg);
       pd = new ProgressDialog(getContext());
       pd.setMessage("Fetching data...");
       pd.show();

       Bundle bundle = this.getArguments();
       if (bundle!=null){
           FileNumber = bundle.getString("FileNumber");
       }

       firebaseFirestore = FirebaseFirestore.getInstance();
       firebaseAuth = FirebaseAuth.getInstance();
       UserId = firebaseAuth.getCurrentUser().getUid();

       firebaseFirestore.collection("Child-Details").whereEqualTo("ParentId",UserId)
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

       return view;
    }
}