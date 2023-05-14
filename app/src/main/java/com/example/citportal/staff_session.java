package com.example.citportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.citportal.central.CentralActivity;

public class staff_session extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_session);
        TextView login = (TextView) findViewById(R.id.btn);
        login.setText(Constants.name);
        Button sub1=(Button) findViewById(R.id.sub1);
        Button sub2=(Button) findViewById(R.id.sub2);
        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(staff_session.this, CentralActivity.class);
                startActivity(intent);
            }
        });
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(staff_session.this, CentralActivity.class);
                startActivity(intent);
            }
        });
    }
}