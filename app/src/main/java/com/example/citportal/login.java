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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {
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
                if (!username.isEmpty() && !password.isEmpty()){

                    Users users = new Users(username,password);
                    db = FirebaseDatabase.getInstance("https://cit-portal-7bd3e-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    reference = db.getReference("Users");
                    reference.child(username).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            name.setText("");
                            pass.setText("");
                            Toast.makeText(login.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login.this,home_page.class);
                            startActivity(intent);
                        }
                    });

                }

            }
        });
    }
}