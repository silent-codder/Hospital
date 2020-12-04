package com.silentcodder.hospital.Doctor.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.hospital.Counter.Adapter.CounterAppointmentHistoryAdapter;
import com.silentcodder.hospital.Counter.Model.AppointmentHistory;
import com.silentcodder.hospital.Doctor.Adapter.DoctorAppointmentAdapter;
import com.silentcodder.hospital.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorHomeFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    TextView textView;
    String UserId;
    SwipeRefreshLayout swipeRefreshLayout;

    List<AppointmentHistory> appointmentHistory;
    DoctorAppointmentAdapter doctorAppointmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycleView);
        UserId = firebaseAuth.getCurrentUser().getUid();

        imageView = view.findViewById(R.id.noData);
        textView = view.findViewById(R.id.noDataText);
        swipeRefreshLayout = view.findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        //RecycleView
        loadData();

        return view;
    }

    private void loadData() {
        //Recycle view
        swipeRefreshLayout.setRefreshing(false);
        appointmentHistory = new ArrayList<>();
        doctorAppointmentAdapter = new DoctorAppointmentAdapter(appointmentHistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(doctorAppointmentAdapter);

        CollectionReference appointmentRef = firebaseFirestore.collection("Doctor-OPD");

        Query query = appointmentRef.orderBy("TimeStamp",Query.Direction.ASCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String AppointmentHistoryId = doc.getDocument().getId();
                        AppointmentHistory appointmentHistory1 = doc.getDocument().toObject(AppointmentHistory.class).withId(AppointmentHistoryId);
                        appointmentHistory.add(appointmentHistory1);
                        doctorAppointmentAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}