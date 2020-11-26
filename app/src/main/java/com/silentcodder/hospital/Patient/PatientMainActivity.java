package com.silentcodder.hospital.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.hospital.MainActivity;
import com.silentcodder.hospital.Patient.Fragments.PatientHistoryFragment;
import com.silentcodder.hospital.Patient.Fragments.PatientHomeFragment;
import com.silentcodder.hospital.Patient.Fragments.PatientProfileFragment;
import com.silentcodder.hospital.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientMainActivity extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    Fragment selectFragment;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.navMenu);
        drawerLayout = findViewById(R.id.drawer);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String UserId = firebaseAuth.getCurrentUser().getUid();


        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));


        firebaseFirestore.collection("Parent-Details").document(UserId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String FirstName = documentSnapshot.getString("First-Name");
                String LastName = documentSnapshot.getString("Last-Name");
                String MobileNumber = documentSnapshot.getString("Mobile-Number");
                String ProfileUrl = documentSnapshot.getString("ProfileImgUrl");

                View view = nav.inflateHeaderView(R.layout.counter_header);
                CircleImageView profile = view.findViewById(R.id.counterProfile);
                TextView name = view.findViewById(R.id.firstName);
                TextView mobileNumber = view.findViewById(R.id.mobileNumber);
                name.setText(FirstName + " " + LastName);
                mobileNumber.setText(MobileNumber);
                Picasso.get().load(ProfileUrl).into(profile);


            }
        });



        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                removeColor(nav);

                item.setChecked(true);

                switch (item.getItemId()){
                    case R.id.nav_patient_home :
                        selectFragment = new PatientHomeFragment();
                        break;
                    case R.id.nav_patient_hitory :
                        selectFragment = new PatientHistoryFragment();
                        break;
                    case R.id.nav_patient_profile :
                        selectFragment = new PatientProfileFragment();
                        break;
                    case R.id.nav_patient_logout :
                        Toast.makeText(PatientMainActivity.this, "Working on it !!", Toast.LENGTH_SHORT).show();
                        break;
                }

                if (selectFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientHomeFragment()).commit();

    }
    private void removeColor(NavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);
        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(PatientMainActivity.this, MainActivity.class));
        finish();
    }
}