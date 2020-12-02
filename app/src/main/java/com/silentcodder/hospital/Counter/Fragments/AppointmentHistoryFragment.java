package com.silentcodder.hospital.Counter.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
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
import com.silentcodder.hospital.Counter.Adapter.CounterAppointmentAdapter;
import com.silentcodder.hospital.Counter.Adapter.CounterAppointmentHistoryAdapter;
import com.silentcodder.hospital.Counter.Model.AppointmentHistory;
import com.silentcodder.hospital.Patient.Model.Appointment;
import com.silentcodder.hospital.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    TextView textView;
    String UserId;

    List<AppointmentHistory> appointmentHistory;
    CounterAppointmentHistoryAdapter counterAppointmentHistoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_appointment_history, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycleView);
        UserId = firebaseAuth.getCurrentUser().getUid();

        imageView = view.findViewById(R.id.noData);
        textView = view.findViewById(R.id.noDataText);

        //Recycle view
       // swipeRefreshLayout.setRefreshing(false);
        appointmentHistory = new ArrayList<>();
        counterAppointmentHistoryAdapter = new CounterAppointmentHistoryAdapter(appointmentHistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(counterAppointmentHistoryAdapter);

        CollectionReference appointmentRef = firebaseFirestore.collection("Doctor-OPD");

       // Query query = appointmentRef.orderBy("ChildFileNumber",Query.Direction.ASCENDING);

        appointmentRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){

                        AppointmentHistory appointmentHistory1 = doc.getDocument().toObject(AppointmentHistory.class);
                        appointmentHistory.add(appointmentHistory1);
                        counterAppointmentHistoryAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }
}