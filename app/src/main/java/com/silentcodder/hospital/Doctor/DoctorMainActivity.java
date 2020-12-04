package com.silentcodder.hospital.Doctor;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.hospital.Counter.Fragments.CounterSearchFragment;
import com.silentcodder.hospital.Doctor.Fragments.DoctorHomeFragment;
import com.silentcodder.hospital.Doctor.Fragments.DoctorProfileFragment;
import com.silentcodder.hospital.Doctor.Fragments.DoctorHistoryAppointmentFragment;
import com.silentcodder.hospital.MainActivity;
import com.silentcodder.hospital.R;

public class DoctorMainActivity extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Fragment selectFragment;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ImageView mBtnSearch;

    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        mBtnSearch = findViewById(R.id.btnSearch);

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CounterSearchFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });


        nav = findViewById(R.id.navMenu);
        drawerLayout = findViewById(R.id.drawer);


        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                removeColor(nav);

                item.setChecked(true);


                switch (item.getItemId()){
                    case R.id.nav_counter_home :
                        selectFragment = new DoctorHomeFragment();
                        break;
                    case R.id.nav_counter_search :
                        selectFragment = new DoctorHistoryAppointmentFragment();
                        break;
                    case R.id.nav_counter_profile :
                        selectFragment = new DoctorProfileFragment();
                        break;
                    case R.id.nav_counter_logout :
                        Toast.makeText(DoctorMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        logOut();
                        break;
                }

                if (selectFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).addToBackStack(null).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DoctorHomeFragment()).commit();

    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DoctorMainActivity.this, MainActivity.class));
        finish();
    }

    private void removeColor(NavigationView nav) {
        for (int i = 0; i < nav.getMenu().size(); i++) {
            MenuItem item = nav.getMenu().getItem(i);
            item.setChecked(false);
        }
    }
}