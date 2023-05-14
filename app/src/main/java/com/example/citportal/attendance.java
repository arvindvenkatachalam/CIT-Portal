package com.example.citportal;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.citportal.peripheral.PeripheralActivity;

public class attendance extends Fragment {

    Button viewatt,mark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        viewatt=(Button)view.findViewById(R.id.view_attendance);
        mark=(Button)view.findViewById(R.id.mark);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(attendance.this.getActivity(),PeripheralActivity.class);
                startActivity(intent);
            }
        });
        //return inflater.inflate(R.layout.fragment_attendance, container, false);
        return view;
    }
}