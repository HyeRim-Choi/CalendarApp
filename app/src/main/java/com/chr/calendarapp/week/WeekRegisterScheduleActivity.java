package com.chr.calendarapp.week;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import com.chr.calendarapp.R;
import com.chr.calendarapp.database.ScheduleDatabaseManager;
import com.chr.calendarapp.database.ScheduleVO;

import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WeekRegisterScheduleActivity extends AppCompatActivity{

    EditText et_title, et_place, et_memo;
    TimePicker time_start, time_end;
    Button btn_search, btn_save, btn_cancel, btn_delete;
    MapView mapView;

    double latitude, longitude;

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

        mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // 데이터 받아오기
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        date = intent.getIntExtra("date", 0);
        time = intent.getIntExtra("time", 0);
        schedule = (ScheduleVO) intent.getSerializableExtra("schedule");

        if(schedule != null){
            // 상세 일정으로 세팅
            et_title.setText(schedule.getTitle());
            time_start.setHour(Integer.parseInt(String.valueOf(schedule.getTime_start().substring(0,2))));
            time_start.setMinute(Integer.parseInt(String.valueOf(schedule.getTime_start().substring(3))));
            time_end.setHour(Integer.parseInt(String.valueOf(schedule.getTime_end().substring(0,2))));
            time_end.setMinute(Integer.parseInt(String.valueOf(schedule.getTime_end().substring(3))));
            et_place.setText(schedule.getPlace());
            showMarker(schedule.getPlace(), schedule.getLatitude(), schedule.getLongitude());
            latitude = schedule.getLatitude();
            longitude = schedule.getLongitude();
            et_memo.setText(schedule.getMemo());

            chk = 1;

        }
        else{
            // 년도, 월, 일, 시간 초기 세팅
            et_title.setText(year + "년 " + month + "월 " + date + "일 " + time + "시");
            time_start.setHour(time);
            time_start.setMinute(0);
            time_end.setHour(time + 1);
            time_end.setMinute(0);

             chk = 2;
        }


        btn_search.setOnClickListener(click);
        btn_save.setOnClickListener(click);
        btn_delete.setOnClickListener(click);
        btn_cancel.setOnClickListener(click);

        // timeStart, timeEnd 가져오기
        time_start.setOnTimeChangedListener(timePicker);
        time_end.setOnTimeChangedListener(timePicker);

    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            Intent resultIntent = new Intent();

            switch (v.getId()){
                case R.id.btn_search:
                    // 장소 검색
                    String search = et_place.getText().toString();

                    // 검색 창이 비어있다면
                    if(search == null || search.isEmpty()){
                        Toast.makeText(WeekRegisterScheduleActivity.this, "장소를 검색해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 장소 검색을해서 위도, 경도 찾기
                    getLocation(search);

                    // 마커 띄우기
                    showMarker(search, latitude, longitude);

                    break;

                case R.id.btn_save:

                    // insert
                    if(chk == 2){
                        long id = insertRows();

                        // db에 insert가 성공적으로 되었다면
                        if(id > 0){
                            Toast.makeText(WeekRegisterScheduleActivity.this,"저장 되었습니다",Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                        else{
                            Toast.makeText(WeekRegisterScheduleActivity.this,"저장 실패하였습니다",Toast.LENGTH_SHORT).show();
                        }
                    }

                    // update
                    else if(chk == 1){
                        long id = updateRows();

                        // db에 update가 성공적으로 되었다면
                        if(id > 0){
                            Toast.makeText(WeekRegisterScheduleActivity.this,"수정 되었습니다",Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                        else{
                            Toast.makeText(WeekRegisterScheduleActivity.this,"수정 실패하였습니다",Toast.LENGTH_SHORT).show();
                        }
                    }


                    break;

                case R.id.btn_delete:

                    //delete

                    // 일정이 존재한다면
                    if(chk == 1){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(WeekRegisterScheduleActivity.this);

                        dialog.setTitle("삭제 확인");
                        dialog.setMessage("일정을 정말 삭제하시겠습니까?");

                        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 일정 삭제하기
                                long id = deleteRow();

                                // db에 delete가 성공적으로 되었다면
                                if(id > 0){
                                    Toast.makeText(WeekRegisterScheduleActivity.this,"삭제 되었습니다",Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(WeekRegisterScheduleActivity.this,"삭제 실패하였습니다",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        dialog.setNegativeButton("아니요", null);

                        dialog.show();
                    }

                    break;

                    // 취소 클릭 시
                case R.id.btn_cancel:
                    // 돌아가기
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    break;
            }
        }
    };


    // 주소 이름을 통해 위도 경도 받기
    public void getLocation(String search){
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(search,1);
            if (addresses.size() >0) {
                Address bestResult = (Address) addresses.get(0);
                latitude =  bestResult.getLatitude();
                longitude =  bestResult.getLongitude();
            }
        } catch (IOException e) {
            Log.e(getClass().toString(),"Failed in using Geocoder.", e);
            return;
        }
    }


    // 해당 주소에 마커 띄우기
    public void showMarker(String search, double latitude, double longitude){
        // 마커 띄우기
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(search);
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본 마커
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커 클릭 시
        mapView.addPOIItem(marker);

        // 화면 중앙에 표시 될 위치
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
    }


    // TimePicker 시간 가져오기
    TimePicker.OnTimeChangedListener timePicker = new TimePicker.OnTimeChangedListener() {

        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            String hour = Integer.toString(hourOfDay);
            String min = Integer.toString(minute);

            // "09" format 맞추기
            if(min.length() == 1){
                min = "0" + min;
            }

            if(hour.length() == 1){
                hour = "0" + hour;
            }

            switch (view.getId()){
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


    // insert
    public long insertRows(){
        scheduleDB = ScheduleDatabaseManager.getInstance(WeekRegisterScheduleActivity.this);

        String title = et_title.getText().toString();
        String place = et_place.getText().toString();
        String memo = et_memo.getText().toString();


        // db에 insert하기 위해 "09:10"으로 시간 format 맞추기
        if(timeStart == null){
            String strTime = time + "";
            if(strTime.length() == 1){
                strTime = "0" + strTime;
            }
            timeStart = strTime + ":0";
        }
        if(timeEnd == null){
            String strTime = (time + 1) + "";
            if(strTime.length() == 1){
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

    // update
    @RequiresApi(api = Build.VERSION_CODES.M)
    public long updateRows(){
        scheduleDB = ScheduleDatabaseManager.getInstance(WeekRegisterScheduleActivity.this);

        String title = et_title.getText().toString();
        String place = et_place.getText().toString();
        String memo = et_memo.getText().toString();

        // db에 update하기 위해 "09:10"으로 시간 format 맞추기
        if(timeStart == null){
            String strTime = time_start.getHour() + "";
            if(strTime.length() == 1){
                strTime = "0" + strTime;
            }
            timeStart = strTime + ":" + time_start.getMinute();
        }
        if(timeEnd == null){
            String strTime = time_end.getHour() + "";
            if(strTime.length() == 1){
                strTime = "0" + strTime;
            }
            timeEnd = strTime + ":" + time_end.getMinute();
        }

        // update schedule set title = title, place = place,,,, where year=2021 and month=6 and date=3 and time_start like '06%'
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

        // "09" format으로 세팅
        if(timeStr.length() == 1){
            timeStr = "0" + timeStr;
        }

        String whereClause = "year=" + year + " and month=" + month + " and date=" + date  + " and time_start like '" + timeStr + "%'";

       return scheduleDB.update(updateRow, whereClause, null);
    }

    // delete
    public long deleteRow(){
        scheduleDB = ScheduleDatabaseManager.getInstance(WeekRegisterScheduleActivity.this);

        String timeStr = time + "";

        // "09" format으로 세팅
        if(timeStr.length() == 1){
            timeStr = "0" + timeStr;
        }

        // delete from schedule where
        String whereClause = "year=" + year + " and month=" + month + " and date=" + date  + " and time_start like '" + timeStr + "%'";

        return scheduleDB.delete(whereClause,null);
    }


}