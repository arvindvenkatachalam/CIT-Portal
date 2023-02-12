package com.example.citportal.central;


import static com.example.citportal.Constants.CHARACTERISTIC_UUID;
import static com.example.citportal.Constants.SERVICE_STRING;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothUtils {

    public static List<BluetoothGattCharacteristic> findBLECharacteristics(BluetoothGatt _gatt) {
        List<BluetoothGattCharacteristic> matching_characteristics = new ArrayList<>();
        List<BluetoothGattService> service_list = _gatt.getServices();
        BluetoothGattService service = findGattService(service_list);
        if (service == null) {
            return matching_characteristics;
        }

        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : characteristicList) {
            if (isMatchingCharacteristic(characteristic)) {
                matching_characteristics.add(characteristic);
            }
        }

        return matching_characteristics;
    }

    @Nullable
    public static BluetoothGattCharacteristic findCharacteristic(BluetoothGatt _gatt, String _uuid_string) {
        List<BluetoothGattService> service_list = _gatt.getServices();
        BluetoothGattService service = BluetoothUtils.findGattService(service_list);
        if (service == null) {
            return null;
        }

        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : characteristicList) {
            if (matchCharacteristic(characteristic, _uuid_string)) {
                return characteristic;
            }
        }

        return null;
    }

    private static boolean matchCharacteristic(BluetoothGattCharacteristic _characteristic, String _uuid_string) {
        if (_characteristic == null) {
            return false;
        }
        UUID uuid = _characteristic.getUuid();
        return matchUUIDs(uuid.toString(), _uuid_string);
    }

    @Nullable
    private static BluetoothGattService findGattService(List<BluetoothGattService> _service_list) {
        for (BluetoothGattService service : _service_list) {
            String service_uuid_string = service.getUuid().toString();
            if (matchServiceUUIDString(service_uuid_string)) {
                return service;
            }
        }
        return null;
    }

    private static boolean matchServiceUUIDString(String _service_uuid_string) {
        return matchUUIDs(_service_uuid_string, SERVICE_STRING);
    }

    private static boolean isMatchingCharacteristic(BluetoothGattCharacteristic _characteristic) {
        if (_characteristic == null) {
            return false;
        }
        UUID uuid = _characteristic.getUuid();
        return matchCharacteristicUUID(uuid.toString());
    }

    private static boolean matchCharacteristicUUID(String _characteristic_uuid_string) {
        return matchUUIDs(_characteristic_uuid_string, CHARACTERISTIC_UUID);
    }

    private static boolean matchUUIDs(String _uuid_string, String... _matches) {
        for (String match : _matches) {
            if (_uuid_string.equalsIgnoreCase(match)) {
                return true;
            }
        }

        return false;
    }
}
