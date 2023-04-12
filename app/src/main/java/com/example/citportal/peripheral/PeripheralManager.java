package com.example.citportal.peripheral;

import static com.example.citportal.Constants.CHARACTERISTIC_UUID;
import static com.example.citportal.Constants.CONFIG_UUID;
import static com.example.citportal.Constants.SERVICE_STRING;
import static com.example.citportal.Constants.SERVICE_UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.example.citportal.Constants;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class PeripheralManager {

    private final String TAG = PeripheralManager.class.getSimpleName();

    protected static volatile PeripheralManager sInstance = null;
    protected ServerHelper serverHelper = null;
    private Context mContext;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattServer mGattServer;
    private BluetoothGattService mBluetoothGattService;
    private BluetoothLeAdvertiser mAdvertiser;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGattCharacteristic mCharacteristic;

    private PeripheralCallback listener;

    public PeripheralManager(Context context) {
        this.mContext = context.getApplicationContext();
        this.serverHelper = new ServerHelper(3);
    }

    public void setCallBack(PeripheralCallback listener) {
        this.listener = listener;
    }

    public static PeripheralManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PeripheralManager(context);
        }

        return sInstance;
    }

    public void initServer() {
        Log.d(TAG, "initServer =================================== IN");

        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            listener.requestEnableBLE();
            return;
        }

        mBluetoothGattService = new BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        mCharacteristic = new BluetoothGattCharacteristic(UUID.fromString(CHARACTERISTIC_UUID),
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE);
        mCharacteristic.addDescriptor(new BluetoothGattDescriptor(UUID.fromString(CONFIG_UUID), (BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ)));
        mCharacteristic.setValue(new byte[]{0, 0});

        mBluetoothGattService.addCharacteristic(mCharacteristic);

        startAdvertising();
        startServer();
    }

    /**
     * Begin advertising over Bluetooth that this device is connectable
     * and supports the Current Time Service.
     */
    private void startAdvertising() {
        mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        if (mAdvertiser == null) {
            Log.w(TAG, "Failed to create advertiser");
            listener.onStatusMsg("Failed to create advertiser");
            return;
        }

        AdvertiseSettings advSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setConnectable(true)
                .build();

        AdvertiseData advData = new AdvertiseData.Builder()
                .setIncludeTxPowerLevel(true)
                .addServiceUuid(ParcelUuid.fromString(SERVICE_STRING))
                .build();

        AdvertiseData advScanResponse = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .build();
        mAdvertiser.startAdvertising(advSettings, advData, advScanResponse, mAdvCallBack);
    }

    /**
     * Stop Bluetooth advertisements.
     */
    private void stopAdvertising() {
        if (mAdvertiser == null)
            return;

        mAdvertiser.stopAdvertising(mAdvCallBack);
    }

    /**
     * Gatt Server.
     */
    private void startServer() {
        mGattServer = mBluetoothManager.openGattServer(mContext, bluetoothGattServerCallback);
        if (mGattServer == null) {
            Log.w(TAG, "Unable to create GATT server");
            listener.onStatusMsg("Unable to create GATT server");
            return;
        }

        mGattServer.addService(mBluetoothGattService);
    }

    /**
     * GATT Server .
     */
    private void stopServer() {
        if (mGattServer == null)
            return;

        mGattServer.close();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothAdapter.isEnabled()) {
            stopServer();
            stopAdvertising();
        }
    }

    public void sendData(String message) {
        if (mBluetoothDevice == null) {
            Log.e(TAG, "BluetoothDevice is null");
            listener.onStatusMsg("BluetoothDevice is null");
            return;
        } else if (mBluetoothDevice.getAddress() == null) {
            Log.e(TAG, "GattServer lost device address");
            listener.onStatusMsg("GattServer lost device address");
            return;
        } else if (mGattServer == null) {
            Log.e(TAG, "GattServer is null");
            listener.onStatusMsg("GattServer is null");
            return;
        }

        boolean indicate = (mCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) == BluetoothGattCharacteristic.PROPERTY_INDICATE;
        mCharacteristic.setValue(message.getBytes()); // 20byte limit
        mGattServer.notifyCharacteristicChanged(mBluetoothDevice, mCharacteristic, indicate);

        listener.onStatusMsg("write : " + message);
    }
    public void sendNumberData() {

        if (mBluetoothDevice == null) {
            Log.e(TAG, "BluetoothDevice is null");
            listener.onStatusMsg("BluetoothDevice is null");
            return;
        } else if (mBluetoothDevice.getAddress() == null) {
            Log.e(TAG, "GattServer lost device address");
            listener.onStatusMsg("GattServer lost device address");
            return;
        } else if (mGattServer == null) {
            Log.e(TAG, "GattServer is null");
            listener.onStatusMsg("GattServer is null");
            return;
        }

        boolean indicate = (mCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) == BluetoothGattCharacteristic.PROPERTY_INDICATE;
        byte[] message = serverHelper.getStatusStr();
        mCharacteristic.setValue(message); // 20byte limit
        mGattServer.notifyCharacteristicChanged(mBluetoothDevice, mCharacteristic, indicate);

        listener.onStatusMsg("write : " + message);
    }
    /**
     * Advertise Callback
     */
    private final AdvertiseCallback mAdvCallBack = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.e(TAG, "AdvertiseCallback onStartSuccess");
            listener.onStatusMsg("GattServer onStartSuccess");
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.e(TAG, "AdvertiseCallback onStartFailure");
            listener.onStatusMsg("GattServer onStartFailure");
        }
    };


    /**
     * Gatt Server Callback
     */
    BluetoothGattServerCallback bluetoothGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.e(TAG, "BluetoothGattServerCallback onConnectionStateChange");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    mBluetoothDevice = device;
                    listener.onStatusMsg("GattServer STATE_CONNECTED");
                    Calendar calendar = Calendar.getInstance();
                    String todayTime = (calendar.get(Calendar.MONTH) + 1)
                            + "/" + calendar.get(Calendar.DAY_OF_MONTH)
                            + " " + calendar.get(Calendar.HOUR_OF_DAY)
                            + ":" + calendar.get(Calendar.MINUTE)
                            + ":" + calendar.get(Calendar.SECOND);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sendData(Constants.name);
                    sendData(todayTime);
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    mBluetoothDevice = null;
                    listener.onStatusMsg("GattServer STATE_DISCONNECTED");
                }
            } else {
                mBluetoothDevice = null;
                listener.onStatusMsg("GattServer GATT_FAILURE");
            }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            Log.e(TAG, "BluetoothGattServerCallback onCharacteristicReadRequest");
            if (offset != 0) {
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_INVALID_OFFSET, offset,null);
                return;
            }
            mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic.getValue());
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            Log.e(TAG, "BluetoothGattServerCallback onCharacteristicWriteRequest");
            listener.onStatusMsg("read : " + new String(value));
            listener.onToast(new String(value));

            if (responseNeeded)
                mGattServer.sendResponse(device, requestId, 0, 0, null);
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            Log.e(TAG, "BluetoothGattServerCallback onDescriptorReadRequest");
            if (offset != 0) {
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_INVALID_OFFSET, offset,null);
                return;
            }
            mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, descriptor.getValue());
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            Log.e(TAG, "BluetoothGattServerCallback onDescriptorWriteRequest");
            mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null);
        }
    };
}
