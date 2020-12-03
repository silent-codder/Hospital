package com.silentcodder.hospital.Counter.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.core.Filter;
import com.silentcodder.hospital.Counter.Adapter.CounterAppointmentHistoryAdapter;
import com.silentcodder.hospital.Counter.Adapter.SearchAdapter;
import com.silentcodder.hospital.Counter.Model.AppointmentHistory;
import com.silentcodder.hospital.Patient.Model.ChildData;
import com.silentcodder.hospital.R;

import java.util.ArrayList;
import java.util.List;

public class CounterSearchFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView mBtnSearch;
    EditText mSearch;
    TextView textView;


    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    List<ChildData> childData;
    SearchAdapter searchAdapter;

    ProgressDialog pd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter_search, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycleView);
        textView = view.findViewById(R.id.noDataText);
        mBtnSearch = view.findViewById(R.id.btnSearch);
        mSearch = view.findViewById(R.id.searchWord);
        pd = new ProgressDialog(getContext());

        //Recycle view
        // swipeRefreshLayout.setRefreshing(false);
        childData = new ArrayList<>();
        searchAdapter = new SearchAdapter(childData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchAdapter);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()<1)
                {
                    clear();
                }
                else {
                   loadData(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Search_txt = mSearch.getText().toString();
                if (!TextUtils.isEmpty(Search_txt)){
                    pd.setMessage("Searching..");
                    pd.show();
                    loadData(Search_txt);
                    clear();
                }else {
                    clear();
                    Toast.makeText(getContext(), "Enter data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.dismiss();
                clear();
            }
        });

        return view;
    }

    public void clear() {
        pd.dismiss();
        int size = childData.size();
        childData.clear();
        searchAdapter.notifyItemRangeRemoved(0,size);
    }

    private void loadData(String search_txt) {
        CollectionReference appointmentRef = firebaseFirestore.collection("Child-Details");

        Query mobileNumber = appointmentRef.whereEqualTo("MobileNumber","+91"+search_txt);
        Query fileNumber = appointmentRef.orderBy("FileNumber").startAt(search_txt).endAt(search_txt + "\uf8ff");

        mobileNumber.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                pd.dismiss();
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        ChildData childData1 = doc.getDocument().toObject(ChildData.class);
                        childData.add(childData1);
                        searchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        fileNumber.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                pd.dismiss();
                if (value.isEmpty()){

                }
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        ChildData childData1 = doc.getDocument().toObject(ChildData.class);
                        childData.add(childData1);
                        searchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}