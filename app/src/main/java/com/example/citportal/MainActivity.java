package com.example.citportal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button move;
    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isBton = false;
    private boolean isBtconnectgranted = false;
    private boolean isFinelocationgranted = false;
    private boolean isBtscangranted = false;
    private boolean isBtadvertisegranted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if(result.get(Manifest.permission.BLUETOOTH)!=null){
                    isBton=result.get(Manifest.permission.BLUETOOTH);
                }

                if(result.get(Manifest.permission.BLUETOOTH_CONNECT)!=null){
                    isBtconnectgranted=result.get(Manifest.permission.BLUETOOTH_CONNECT);
                }
                if(result.get(Manifest.permission.BLUETOOTH_ADVERTISE)!=null){
                    isBtadvertisegranted=result.get(Manifest.permission.BLUETOOTH_ADVERTISE);
                }
                if(result.get(Manifest.permission.BLUETOOTH_SCAN)!=null){
                    isBtscangranted=result.get(Manifest.permission.BLUETOOTH_SCAN);
                }
                if(result.get(Manifest.permission.ACCESS_FINE_LOCATION)!=null){
                    isFinelocationgranted=result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
        });
        requestPermission();
        move=findViewById(R.id.bt_nextscreen);
        move.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,login_types.class);
            startActivity(intent);

            // testing GIT
        });
    }

    private void requestPermission()
    {
        isBton = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)==PackageManager.PERMISSION_GRANTED;
        isBtconnectgranted = ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_CONNECT)==PackageManager.PERMISSION_GRANTED;
        isBtadvertisegranted = ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_ADVERTISE)==PackageManager.PERMISSION_GRANTED;
        isBtscangranted = ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN)==PackageManager.PERMISSION_GRANTED;
        isFinelocationgranted = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;
        List<String> permisssionRequest=new ArrayList<String>();
        if(!isBtconnectgranted){
            permisssionRequest.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        if(!isBtadvertisegranted){
            permisssionRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE);
        }
        if(!isBtscangranted){
            permisssionRequest.add(Manifest.permission.BLUETOOTH_SCAN);
        }
        if(!isFinelocationgranted){
            permisssionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(!permisssionRequest.isEmpty()){
            mPermissionResultLauncher.launch(permisssionRequest.toArray(new String[0]));

        }
    }
}