package com.example.smartbandsdkdemo.ui.devicesetting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbandsdkdemo.R;

import java.util.Calendar;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

public class DeviceSettingActivity extends AppCompatActivity {
    Button btn_pin, btn_version, btn_get_time, btn_set_time, btn_set_device_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        findViewByIds();
    }

    private void findViewByIds() {
        btn_pin = findViewById(R.id.button_get_battery_power);
        btn_version = findViewById(R.id.button_get_device_version);
        btn_get_time = findViewById(R.id.button_get_device_time);
        btn_set_time = findViewById(R.id.button_set_time_format);
        btn_set_device_time = findViewById(R.id.button_set_device_time);

        btn_pin.setOnClickListener(v -> BluetoothSDK.getBatteryPower(resultCallBack));
        btn_version.setOnClickListener(v -> BluetoothSDK.getDeviceVersion(resultCallBack));
        btn_get_time.setOnClickListener(v -> BluetoothSDK.getDeviceTime(resultCallBack));
        btn_set_time.setOnClickListener(v -> setting_time_format());
        btn_set_device_time.setOnClickListener( v->{
            BluetoothSDK.setDeviceTime(resultCallBack, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND));
        });
    }

    ResultCallBack resultCallBack = new ResultCallBack() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSuccess(int resultType, Object[] objects) {
            String TAG = "Device Setting Activity";
            switch (resultType) {

                case ResultCallBack.TYPE_GET_BATTERY_POWER:
                    Toast.makeText(DeviceSettingActivity.this, "Mức pin hiện tại: " + objects[0], Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "battery power : " + objects[0]);
                    break;
                case ResultCallBack.TYPE_GET_DEVICE_VERSION:
                    Toast.makeText(DeviceSettingActivity.this, "Phiên bản thiết bị: " + objects[0], Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "device version : " + objects[0]);
                    break;

                case ResultCallBack.TYPE_GET_DEVICE_TIME:
                    Toast.makeText(DeviceSettingActivity.this, "Thời gian hiện tại: " + objects[0], Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Device time: " + objects[0]);
                    break;
                case ResultCallBack.TYPE_SET_TIME_FORMAT:
                    Toast.makeText(DeviceSettingActivity.this, "Đã đặt lại định dạng thời gian!!!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Set time success!!!");
                    break;
                case ResultCallBack.TYPE_SET_DEVICE_TIME:
                    Toast.makeText(DeviceSettingActivity.this, "Đồng bộ thời gian với điện thoại!!!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "set time success!!!");
                    break;

            }
        }

        @Override
        public void onFail(int i) {

        }
    };

    public void setting_time_format() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Định dạng ngày tháng");
        b.setMessage("Bạn muốn hiển thị theo định dạng nào?");
        b.setPositiveButton("24H", (dialog, id) -> BluetoothSDK.setTimeFormat(resultCallBack, true));
        b.setNegativeButton("12H", (dialog, id) -> {
            BluetoothSDK.setTimeFormat(resultCallBack, false);
            dialog.cancel();
        });
        AlertDialog al = b.create();
        al.show();
    }
}
