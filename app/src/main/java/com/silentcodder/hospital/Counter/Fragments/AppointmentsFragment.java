package com.silentcodder.hospital.Counter.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.hospital.Counter.Adapter.CounterAppointmentAdapter;
import com.silentcodder.hospital.Patient.Adapter.PatientAppointmentAdapter;
import com.silentcodder.hospital.Patient.Model.Appointment;
import com.silentcodder.hospital.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentsFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    ImageView imageView;
    TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    List<Appointment> appointment;
    CounterAppointmentAdapter counterAppointmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

         imageView = view.findViewById(R.id.noData);
         textView = view.findViewById(R.id.noDataText);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycleView);
        UserId = firebaseAuth.getCurrentUser().getUid();

        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        //recycle function
        loadData();

        return view;
    }

    private void refresh() {
        loadData();
    }

    private void loadData() {
        //Recycle view
        swipeRefreshLayout.setRefreshing(false);
        appointment = new ArrayList<>();
        counterAppointmentAdapter = new CounterAppointmentAdapter(appointment);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(counterAppointmentAdapter);

        CollectionReference appointmentRef = firebaseFirestore.collection("Appointments");

        Long date = System.currentTimeMillis();
        String dateString = (String) DateFormat
                .format("dd MMM yyyy",new Date(date)).toString();

        Query query = appointmentRef.whereGreaterThanOrEqualTo("AppointmentDate",dateString)
                .orderBy("AppointmentDate",Query.Direction.ASCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);

                }
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String AppointmentId = doc.getDocument().getId();
                        Appointment mAppointment = doc.getDocument().toObject(Appointment.class).withId(AppointmentId);
                        appointment.add(mAppointment);
                        counterAppointmentAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}