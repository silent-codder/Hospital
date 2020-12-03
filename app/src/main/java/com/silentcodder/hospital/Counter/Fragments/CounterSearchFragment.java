package com.silentcodder.hospital.Counter.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.R;

import java.util.List;

public class CounterSearchFragment extends Fragment {

    RecyclerView mRecycleView;
    ImageView mBtnSearch;
    EditText mSearch;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    List<ChildData> childData;
    String UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_counter_search, container, false);

        return view;
    }
}