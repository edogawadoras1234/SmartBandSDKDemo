package com.example.smartbandsdkdemo.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbandsdkdemo.R;

import java.util.Arrays;
import java.util.List;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.app.SettingType;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;
import cn.appscomm.bluetoothsdk.model.ReminderData;

public class ActivityActivities extends AppCompatActivity {
    Button btn_blood, btn_mood, btn_calories, btn_set_remind, btn_submit, btn_get_remind_data, btn_del_remind_data, btn_choose_mode_remind;
    ProgressBar progressBar;
    EditText edt_hour, edt_minute, edt_cycler;
    LinearLayout linearLayout;
    TextView txt_info;
    int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        findViewByIds();
        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
    }

    private void findViewByIds() {
        btn_blood = findViewById(R.id.button_blood);
        btn_mood = findViewById(R.id.button_mood);
        btn_calories = findViewById(R.id.button_calories);
        progressBar = findViewById(R.id.progressBarActivities);
        btn_set_remind = findViewById(R.id.button_set_remind);
        btn_submit = findViewById(R.id.button_set_remind_submit);
        btn_get_remind_data = findViewById(R.id.button_get_remind_data);
        btn_del_remind_data = findViewById(R.id.button_delete_remind_data);
        btn_choose_mode_remind = findViewById(R.id.button_choose_mode_remind);
        linearLayout = findViewById(R.id.liner_layout_time);

        edt_hour = findViewById(R.id.edit_hour_remind);
        edt_minute = findViewById(R.id.edit_minutes_remind);
        edt_cycler = findViewById(R.id.edit_cycler_remind);

        txt_info = findViewById(R.id.text_information_activities);

        btn_blood.setOnClickListener(v -> {
            txt_info.setText("");
            progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            BluetoothSDK.getAllSportTypeCount(resultCallBack);
        });
        btn_calories.setOnClickListener(v -> {
            txt_info.setText("");
            progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            BluetoothSDK.getCaloriesType(resultCallBack);
        });
        btn_blood.setOnClickListener(v -> {
            txt_info.setText("");
            linearLayout.setVisibility(View.GONE);
        });
        btn_set_remind.setOnClickListener(v -> {
            txt_info.setText("");
            linearLayout.setVisibility(View.VISIBLE);
        });
        btn_submit.setOnClickListener(v -> {
            //SettingType: 0: EAT, 1:MEDICINE , 2: DRINK, 3: SLEEP, 4: AWAKE, 5: SPORT, 6: MEETING
            txt_info.setText("");
            if (edt_hour.getText().toString().length() == 0 || edt_minute.getText().toString().length() == 0 || edt_cycler.getText().toString().length() == 0) {
                Toast.makeText(this, "Không được bỏ trống thời gian!", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                ReminderData newReminderData = new ReminderData(0, type,
                        Integer.parseInt(String.valueOf(edt_hour.getText())), Integer.parseInt(String.valueOf(edt_minute.getText())),
                        Integer.parseInt(String.valueOf(edt_cycler.getText())), true, "Loi Nhan");
                BluetoothSDK.setReminder(resultCallBack, SettingType.REMINDER_ACTION_NEW, SettingType.REMINDER_PROTOCOL_BASE, null, newReminderData);

            }
        });

        btn_get_remind_data.setOnClickListener(v -> {
            txt_info.setText("");
            progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            BluetoothSDK.getReminder(resultCallBack, SettingType.REMINDER_PROTOCOL_BASE);
        });
        btn_del_remind_data.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            txt_info.setText("");
            BluetoothSDK.setReminder(resultCallBack, SettingType.REMINDER_ACTION_DELETE_ALL, SettingType.REMINDER_PROTOCOL_BASE_WITH_SHOCK, null, null);
        });
        btn_choose_mode_remind.setOnClickListener(v -> chooseType());
    }

    ResultCallBack resultCallBack = new ResultCallBack() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSuccess(int resultType, Object[] objects) {
            switch (resultType) {
                case ResultCallBack.TYPE_GET_ALL_SPORT_TYPE_COUNT:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ActivityActivities.this, "TYPE_GET_ALL_SPORT_TYPE_COUNT: " + Arrays.toString(objects), Toast.LENGTH_SHORT).show();
                    break;
                case ResultCallBack.TYPE_GET_CALORIES_TYPE:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ActivityActivities.this, "TYPE_GET_CALORIES_TYPE: " + Arrays.toString(objects), Toast.LENGTH_SHORT).show();
                    break;
                case ResultCallBack.TYPE_NEW_REMINDER:
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    Toast.makeText(ActivityActivities.this, "Đã thêm nhắc nhở!", Toast.LENGTH_SHORT).show();
                    edt_hour.setText("");
                    edt_minute.setText("");
                    edt_cycler.setText("");
                    break;
                case ResultCallBack.TYPE_GET_REMINDER:
                    progressBar.setVisibility(View.GONE);
                    if (objects == null || objects.length == 0) {
                        Toast.makeText(ActivityActivities.this, "No reminder data", Toast.LENGTH_SHORT).show();
                    } else {
                        List<ReminderData> reminderDataList = (List<ReminderData>) objects[0];
                        txt_info.setText(reminderDataList.toString());
                    }
                    break;
                case ResultCallBack.TYPE_DELETE_ALL_REMINDER:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ActivityActivities.this, "delete all reminder success", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onFail(int i) {

        }
    };

    private void chooseType() {
        String[] datas = {"Eat", "Medicine", "Drink", "Sleep", "Awake", "Break Time", "Meeting"};
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setSingleChoiceItems(datas, 0, (dialog, which) -> {
            switch (which) {
                case 0:
                    type = 0;
                    Toast.makeText(ActivityActivities.this, "Choose Eat", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    type = 1;
                    Toast.makeText(ActivityActivities.this, "Choose Medicine", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    type = 2;
                    Toast.makeText(ActivityActivities.this, "Choose Drink", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    type = 3;
                    Toast.makeText(ActivityActivities.this, "Choose Sleep", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    type = 4;
                    Toast.makeText(ActivityActivities.this, "Choose Awake", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    type = 5;
                    Toast.makeText(ActivityActivities.this, "Choose Break Time", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    type = 6;
                    Toast.makeText(ActivityActivities.this, "Choose Meeting", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        b.setTitle("Choose Reminder: ");
        b.show();
    }
}
