package com.example.smartbandsdkdemo.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.app.SettingType;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

public class PhonecallReceiver extends BroadcastReceiver {
    String incoming_number;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothSDK.setSwitchSetting(resultCallBack, SettingType.SWITCH_SMS, true);
        BluetoothSDK.setSwitchSetting(resultCallBack, SettingType.SWITCH_INCOME_CALL, true);
        BluetoothSDK.setSwitchSetting(resultCallBack, SettingType.SWITCH_MISS_CALL, true);
        BluetoothSDK.setSwitchSetting(resultCallBack, SettingType.SWITCH_SOCIAL, true);
        String phoneNr = intent.getStringExtra("incoming_number");
        String event = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        incoming_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        switch (event) {
            case "RINGING":
                Toast.makeText(context, "Calling! " + phoneNr, Toast.LENGTH_SHORT).show();
                Log.i("Ringing!", " " + phoneNr);
                BluetoothSDK.sendIncomeCall(resultCallBack, "Calling...");

                break;
            case "OFFHOOK":
                Toast.makeText(context, "So dien thoai la: " + phoneNr, Toast.LENGTH_SHORT).show();
                Log.i("Received!", "" + phoneNr);

                break;
            case "IDLE":
                Log.i("Idle!", " " + phoneNr);

                break;
        }
    }

    ResultCallBack resultCallBack = new ResultCallBack() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSuccess(int resultType, Object[] objects) {
            String TAG = "Device Setting Activity";
            if (resultType == ResultCallBack.TYPE_GET_SWITCH_SETTING) {
                Log.i(TAG, "Device Call : " + objects[0]);
            }
        }

        @Override
        public void onFail(int i) {

        }
    };
}
