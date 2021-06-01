package com.chr.calendarapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.chr.calendarapp.R;

public class WeekRegisterScheduleActivity extends AppCompatActivity {

    EditText et_title, et_place, et_memo;
    TimePicker time_start, time_end;
    Button btn_search, btn_save, btn_cancel, btn_delete;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_register_schedule);

        et_title = findViewById(R.id.et_title);
        et_place = findViewById(R.id.et_place);
        et_memo = findViewById(R.id.et_memo);
        time_start = findViewById(R.id.time_start);
        time_end = findViewById(R.id.time_end);
        btn_search = findViewById(R.id.btn_search);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 0);
        int month = intent.getIntExtra("month", 0);
        int day = intent.getIntExtra("day", 0);
        int time = intent.getIntExtra("time", 0);

        et_title.setText(year + "년 " + month + "월 " + day + "일 " + time + "시");
        time_start.setHour(time);
        time_start.setMinute(0);
        time_end.setHour(time + 1);
        time_end.setMinute(0);


        btn_search.setOnClickListener(click);
        btn_save.setOnClickListener(click);
        btn_delete.setOnClickListener(click);
        btn_cancel.setOnClickListener(click);
    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_search:
                    break;

                case R.id.btn_save:
                    break;

                case R.id.btn_delete:
                    break;

                    // 취소 클릭 시
                case R.id.btn_cancel:
                    // 돌아가기
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    break;
            }
        }
    };
}