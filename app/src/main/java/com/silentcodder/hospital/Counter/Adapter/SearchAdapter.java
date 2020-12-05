package com.silentcodder.hospital.Counter.Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.hospital.Counter.CounterChildFile;
import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    List<ChildData> childData;
    FirebaseFirestore firebaseFirestore;
    public SearchAdapter(List<ChildData> childData) {
        this.childData = childData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_search_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String ChildName = childData.get(position).getChildName();
        String ChildDob = childData.get(position).getChildDOB();
        String FileNumber = childData.get(position).getFileNumber();
        String Gender = childData.get(position).getChildGender();
        String Id = childData.get(position).getParentId();

        if (Gender.equals("Boy") || Gender.equals("Other")){
            holder.boy.setVisibility(View.VISIBLE);
            holder.girl.setVisibility(View.INVISIBLE);
        }else if (Gender.equals("Girl")){
            holder.girl.setVisibility(View.VISIBLE);
            holder.boy.setVisibility(View.INVISIBLE);
        }

        holder.mChildName.setText(ChildName);
        holder.mChildDob.setText(ChildDob);
        holder.mFileNumber.setText(FileNumber);

        holder.mOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new CounterChildFile();
                Bundle bundle = new Bundle();
                bundle.putString("FileNumber" , FileNumber);
                bundle.putString("UserId",Id);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        firebaseFirestore.collection("Parent-Details").document(Id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String FirstName = task.getResult().getString("First-Name");
                        String LastName = task.getResult().getString("Last-Name");

                        holder.mParentName.setText(FirstName + " " + LastName);
                     }
                });

    }

    @Override
    public int getItemCount() {
        return childData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mChildName,mChildDob,mFileNumber,mParentName;
        CircleImageView boy,girl;
        Button mOpenFile,mBookAppointment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mChildName = itemView.findViewById(R.id.childName);
            mChildDob = itemView.findViewById(R.id.childDOB);
            mFileNumber = itemView.findViewById(R.id.fileNumber);
            mParentName = itemView.findViewById(R.id.parentName);
            boy = itemView.findViewById(R.id.childBoyImg);
            girl = itemView.findViewById(R.id.childGirlImg);
            mOpenFile = itemView.findViewById(R.id.btnFileOpen);
            mBookAppointment = itemView.findViewById(R.id.btnBookAppointment);
        }
    }
}
