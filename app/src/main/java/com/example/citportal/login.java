package com.example.citportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.citportal.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    Button login;
    EditText name,pass;
    String username, password;
//    FirebaseDatabase db;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_login);

        login=(Button) findViewById(R.id.login);
        name=(EditText) findViewById(R.id.username);
        pass=(EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = name.getText().toString();
                password = pass.getText().toString();

                if (username.isEmpty() && password.isEmpty()){
                    Toast.makeText(login.this,"Enter both user name and password",Toast.LENGTH_SHORT).show();
                }
                else{
                    System.out.println(username + " " + password);
                    Users users = new Users(username,password);
                    reference = FirebaseDatabase.getInstance("https://cit-portal-7bd3e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
//                    reference = db.getReference("Users");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(username)){
                                final String getPassword = snapshot.child(username).child("password").getValue(String.class);

                                if(getPassword.equals(password)){
                                    Toast.makeText(login.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this,home_page.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(login.this, "Invalid password", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(login.this,"not registered",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(login.this,"database error",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}