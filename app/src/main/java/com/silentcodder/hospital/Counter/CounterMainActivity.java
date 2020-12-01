package com.silentcodder.hospital.Counter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.silentcodder.hospital.Counter.Fragments.CounterHomeFragment;
import com.silentcodder.hospital.Counter.Fragments.CounterProfileFragment;
import com.silentcodder.hospital.Counter.Fragments.CounterSearchFragment;
import com.silentcodder.hospital.MainActivity;
import com.silentcodder.hospital.R;

public class CounterMainActivity extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Fragment selectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                        selectFragment = new CounterHomeFragment();
                        break;
                    case R.id.nav_counter_search :
                        selectFragment = new CounterSearchFragment();
                        break;
                    case R.id.nav_counter_profile :
                        selectFragment = new CounterProfileFragment();
                        break;
                    case R.id.nav_counter_logout :
                        Toast.makeText(CounterMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                       // logOut();
                        break;
                }

                if (selectFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CounterHomeFragment()).commit();

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
        startActivity(new Intent(CounterMainActivity.this,MainActivity.class));
        finish();
    }
}