package com.chr.calendarapp.month;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.chr.calendarapp.R;
import com.chr.calendarapp.database.ScheduleDatabaseManager;
import com.chr.calendarapp.database.ScheduleVO;
import com.google.android.gms.maps.GoogleMap;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MonthRegisterScheduleActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mgoogleMap;

    EditText et_title, et_place, et_memo;
    TimePicker time_start, time_end;
    Button btn_search, btn_save, btn_cancel, btn_delete;

    double latitude ,longitude;

    // DB
    ScheduleDatabaseManager scheduleDB;

    // DB에서 가져온 데이터
    ScheduleVO schedule;

    int year, month, date, time;
    String timeStart, timeEnd;

    // '저장'을 눌렀을 시 insert인지 update인지 확인하려는 변수
    int chk;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_register_schedule);

        et_title = findViewById(R.id.et_title);
        et_place = findViewById(R.id.et_place);
        et_memo = findViewById(R.id.et_memo);
        time_start = findViewById(R.id.time_start);
        time_end = findViewById(R.id.time_end);
        btn_search = findViewById(R.id.btn_search);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.btn_delete);

        // 데이터 받아오기
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        date = intent.getIntExtra("date", 0);
        time = intent.getIntExtra("time", 0);
        latitude = intent.getDoubleExtra("latitude", 37.5882827);
        longitude = intent.getDoubleExtra("longitude", 127.006390);
        schedule = (ScheduleVO) intent.getSerializableExtra("schedule");


        if (schedule != null) {
            // 상세 일정으로 세팅
            et_title.setText(schedule.getTitle());
            time_start.setHour(Integer.parseInt(String.valueOf(schedule.getTime_start().substring(0, 2))));
            time_start.setMinute(Integer.parseInt(String.valueOf(schedule.getTime_start().substring(3))));
            time_end.setHour(Integer.parseInt(String.valueOf(schedule.getTime_end().substring(0, 2))));
            time_end.setMinute(Integer.parseInt(String.valueOf(schedule.getTime_end().substring(3))));
            et_place.setText(schedule.getPlace());
            latitude = schedule.getLatitude();
            longitude = schedule.getLongitude();
            et_memo.setText(schedule.getMemo());
            chk = 1;

        }
        // 일정추가할때 초기세팅
        else {
            time_start.setHour(time);
            time_start.setMinute(0);
            time_end.setHour(time + 1);
            time_end.setMinute(0);
            et_title.setText(year + "년 " + month + "월 " + date + "일 " + time + "시");
            chk = 2;
        }

        // 버튼 클릭시
        btn_search.setOnClickListener(click);
        btn_save.setOnClickListener(click);
        btn_delete.setOnClickListener(click);
        btn_cancel.setOnClickListener(click);

        // timeStart, timeEnd 가져오기
        time_start.setOnTimeChangedListener(timePicker);
        time_end.setOnTimeChangedListener(timePicker);


        // 구글 맵
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            Intent resultIntent = new Intent(getApplicationContext(), MonthDayFragment.class);

            switch (v.getId()) {

                // 장소 검색
                case R.id.btn_search:
                    String search = et_place.getText().toString();

                    // 검색 창이 비어있다면
                    if (search == null || search.isEmpty()) {
                        Toast.makeText(MonthRegisterScheduleActivity.this, "장소를 검색해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 장소 검색을해서 위도, 경도 찾기
                    getLocation(search);

                    break;

                // 저장
                case R.id.btn_save:

                    if (chk == 2) {
                        long id = insertRows();

                        // 저장 성공
                        if (id > 0) {
                            Toast.makeText(MonthRegisterScheduleActivity.this, "저장 되었습니다", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                        // 저장실패
                        else {
                            Toast.makeText(MonthRegisterScheduleActivity.this, "저장 실패하였습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // 업데이트
                    else if (chk == 1) {
                        long id = updateRows();

                        // 업데이트 성공
                        if (id > 0) {
                            Toast.makeText(MonthRegisterScheduleActivity.this, "수정 되었습니다", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                        // 업데이트 실패
                        else {
                            Toast.makeText(MonthRegisterScheduleActivity.this, "수정 실패하였습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                // 삭제
                case R.id.btn_delete:

                    if (chk == 1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MonthRegisterScheduleActivity.this);

                        dialog.setTitle("삭제 확인");
                        dialog.setMessage("일정을 정말 삭제하시겠습니까?");

                        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 일정 삭제하기
                                long id = deleteRow();

                                // 삭제 성공
                                if (id > 0) {
                                    Toast.makeText(MonthRegisterScheduleActivity.this, "삭제 되었습니다", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }
                                // 삭제 실패
                                else {
                                    Toast.makeText(MonthRegisterScheduleActivity.this, "삭제 실패하였습니다", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        dialog.setNegativeButton("아니요", null);
                        dialog.show();
                    }
                    break;

                // 취소 클릭
                case R.id.btn_cancel:
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    break;
            }

        }
    };


    // 이름을 통해 위도 경도 받기, 맵이동
    public void getLocation(String search) {

        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(search,1);
            if (addresses.size() >0) {
                Address bestResult = (Address) addresses.get(0);
                latitude =  bestResult.getLatitude();
                longitude =  bestResult.getLongitude();
                LatLng TM  = new LatLng(latitude,longitude);
                mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TM, 15));
                mgoogleMap.addMarker(new MarkerOptions().position(TM).title(et_place.toString()));

            }
        } catch (IOException e) {
            Log.e(getClass().toString(),"Failed in using Geocoder.", e);
            return;
        }
    }





    // TimePicker 시간 가져오기
    TimePicker.OnTimeChangedListener timePicker = new TimePicker.OnTimeChangedListener() {

        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            String hour = Integer.toString(hourOfDay);
            String min = Integer.toString(minute);

            // "09" format 맞추기
            if (min.length() == 1) {
                min = "0" + min;
            }

            if (hour.length() == 1) {
                hour = "0" + hour;
            }

            switch (view.getId()) {
                case R.id.time_start:
                    // "09:11" format
                    timeStart = hour + ":" + min;
                    break;

                case R.id.time_end:
                    timeEnd = hour + ":" + min;
                    break;
            }
        }
    };


    // 저장
    public long insertRows() {
        scheduleDB = ScheduleDatabaseManager.getInstance(MonthRegisterScheduleActivity.this);

        String title = et_title.getText().toString();
        String place = et_place.getText().toString();
        String memo = et_memo.getText().toString();


        // db에 저장하기위해 형식맞춤
        if (timeStart == null) {
            String strTime = time + "";
            if (strTime.length() == 1) {
                strTime = "0" + strTime;
            }
            timeStart = strTime + ":0";
        }
        if (timeEnd == null) {
            String strTime = (time + 1) + "";
            if (strTime.length() == 1) {
                strTime = "0" + strTime;
            }
            timeEnd = strTime + ":0";
        }

        // insert(db에 저장)
        ContentValues addRow = new ContentValues();

        addRow.put("title", title); // 제목
        addRow.put("time_start", timeStart); // 시작 시간
        addRow.put("time_end", timeEnd); // 종료 시간
        addRow.put("place", place); // 장소
        addRow.put("latitude", latitude); // 위도
        addRow.put("longitude", longitude); // 경도
        addRow.put("memo", memo); // 메모
        addRow.put("year", year); // 년
        addRow.put("month", month); // 월
        addRow.put("date", date); // 일

        return scheduleDB.insert(addRow);

    }

    // 업데이트
    @RequiresApi(api = Build.VERSION_CODES.M)
    public long updateRows() {
        scheduleDB = ScheduleDatabaseManager.getInstance(MonthRegisterScheduleActivity.this);

        String title = et_title.getText().toString();
        String place = et_place.getText().toString();
        String memo = et_memo.getText().toString();

        // db에 저장하기위해 형식맞춤
        if (timeStart == null) {
            String strTime = time_start.getHour() + "";
            if (strTime.length() == 1) {
                strTime = "0" + strTime;
            }
            timeStart = strTime + ":" + time_start.getMinute();
        }
        if (timeEnd == null) {
            String strTime = time_end.getHour() + "";
            if (strTime.length() == 1) {
                strTime = "0" + strTime;
            }
            timeEnd = strTime + ":" + time_end.getMinute();
        }

        ContentValues updateRow = new ContentValues();

        updateRow.put("title", title); // 제목
        updateRow.put("time_start", timeStart); // 시작 시간
        updateRow.put("time_end", timeEnd); // 종료 시간
        updateRow.put("place", place); // 장소
        updateRow.put("latitude", latitude); // 위도
        updateRow.put("longitude", longitude); // 경도
        updateRow.put("memo", memo); // 메모
        updateRow.put("year", year); // 년
        updateRow.put("month", month); // 월
        updateRow.put("date", date); // 일

        String timeStr = time + "";

        // db에 저장하기위해 형식맞춤
        if (timeStr.length() == 1) {
            timeStr = "0" + timeStr;
        }

        String whereClause = "year=" + year + " and month=" + month + " and date=" + date + " and time_start like '" + timeStr + "%'";

        return scheduleDB.update(updateRow, whereClause, null);
    }

    // 삭제
    public long deleteRow() {
        scheduleDB = ScheduleDatabaseManager.getInstance(MonthRegisterScheduleActivity.this);

        String timeStr = time + "";

        // db에서 찾기위해 형식맞춤
        if (timeStr.length() == 1) {
            timeStr = "0" + timeStr;
        }

        // delete from schedule where
        String whereClause = "year=" + year + " and month=" + month + " and date=" + date + " and time_start like '" + timeStr + "%'";
        return scheduleDB.delete(whereClause, null);
    }

    // 맵 마커, 카메라
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        LatLng TM  = new LatLng(latitude,longitude);
        mgoogleMap.addMarker(new MarkerOptions().position(TM).title(et_place.getText().toString()));
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TM, 15));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mgoogleMap.setMyLocationEnabled(true);
    }
}