package com.example.citportal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.citportal.central.CentralActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_staff extends AppCompatActivity {
    Button login;
    EditText name,pass;
    String username, password;
    FirebaseDatabase db;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button) findViewById(R.id.login);
        name=(EditText) findViewById(R.id.username);
        pass=(EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = name.getText().toString();
                password = pass.getText().toString();
                Constants.name = username;
                if (username.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(login_staff.this,"Enter both user name and password",Toast.LENGTH_SHORT).show();
                }
                else{

                    Users users = new Users(username,password);
                    db = FirebaseDatabase.getInstance("https://citportal-9150b-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    reference = db.getReference("Staff");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override

                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(username)){
                                final String getPassword = snapshot.child(username).child("password").getValue(String.class);

                                if(getPassword.equals(password)){
                                    Toast.makeText(login_staff.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login_staff.this, CentralActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(login_staff.this, "Invalid password", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(login_staff.this,"not registered",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(login_staff.this,"database error",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }
}