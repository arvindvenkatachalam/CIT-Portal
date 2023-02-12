package com.example.citportal.peripheral;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;

import java.util.Calendar;

import static com.example.citportal.Constants.REQUEST_ENABLE_BT;


import com.example.citportal.Constants;
import com.example.citportal.R;

public class PeripheralActivity extends AppCompatActivity {

    private final String TAG = PeripheralActivity.class.getSimpleName();

    private TextView tvStatus;
    private Button btnSend, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral);

        initView();
        initServer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT)
            initServer();
    }

    @Override
    public void onBackPressed() {
        showStatusMsg("Close Server");
        PeripheralManager.getInstance(PeripheralActivity.this).close();

        new Handler().postDelayed(() -> finish(), 500);
    }

    private void initView() {
        tvStatus = (TextView) findViewById(R.id.tv_status);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnClose = (Button) findViewById(R.id.btnClose);

        btnSend.setOnClickListener(view -> {
            String text = Constants.name;
            PeripheralManager.getInstance(PeripheralActivity.this).sendData(text);
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeripheralManager.getInstance(PeripheralActivity.this).close();
            }
        });
    }

    private void initServer() {
        PeripheralManager.getInstance(PeripheralActivity.this).setCallBack(peripheralCallback);
        PeripheralManager.getInstance(PeripheralActivity.this).initServer();
    }

    private void requestEnableBLE() {
        Intent ble_enable_intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivityForResult(ble_enable_intent, REQUEST_ENABLE_BT);
    }


    private void showStatusMsg(final String message) {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String oldMsg = tvStatus.getText().toString();
                tvStatus.setText(oldMsg + "\n" + message);

                scrollToBottom();
            }
        };
        handler.sendEmptyMessage(1);
    }

    private void showToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(PeripheralActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
        handler.sendEmptyMessage(1);
    }


    private void scrollToBottom() {
        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollview));
        scrollview.post(new Runnable() {
            @Override public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    PeripheralCallback peripheralCallback = new PeripheralCallback() {
        @Override
        public void requestEnableBLE() {
            PeripheralActivity.this.requestEnableBLE();
        }

        @Override
        public void onStatusMsg(String message) {
            showStatusMsg(message);
        }

        @Override
        public void onToast(String message) {
            showToast(message);
        }
    };

}