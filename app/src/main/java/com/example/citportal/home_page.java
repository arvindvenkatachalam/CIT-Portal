package com.example.citportal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.citportal.peripheral.PeripheralActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class home_page extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;



    attendance attendance = new attendance();
    internals internals = new internals();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        TextView login = (TextView) findViewById(R.id.student_name);
        login.setText(Constants.name);
    bottomNavigationView = findViewById(R.id.bottomNavigationView);
    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,attendance).commit();

    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()){

                case R.id.attendance:

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,attendance).commit();
                    return true;
                case R.id.internals:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,internals).commit();
                    return true;
            }
            return false;
        }
    });
    }

}