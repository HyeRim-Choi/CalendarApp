package com.chr.calendarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.chr.calendarapp.month.MonthDayFragment;
import com.chr.calendarapp.month.MonthFragment;
import com.chr.calendarapp.week.WeekDayFragment;
import com.chr.calendarapp.week.WeekFragment;

import java.io.Serializable;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements WeekDayFragment.OnSetYearMonthListener , MonthDayFragment.OnSetYearMonthListener {

    // Toolbar
    Toolbar my_toolbar;

    // 캘린더
    Calendar cal;

    static int year, month, date;

    public static boolean chk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        my_toolbar = findViewById(R.id.my_toolbar);
        // toolbar 설정
        setSupportActionBar(my_toolbar);

        // 현재 시간을 받는다
        cal = Calendar.getInstance();
        // 현재 년도 받기
        year = cal.get(Calendar.YEAR);
        // 현재 월 받기
        month = cal.get(Calendar.MONTH)+1;
        // 현재 일 받기
        date = cal.get(Calendar.DATE);

        // toolbar title 설정
        my_toolbar.setTitle(year + "년 " + month + "월");

    }


    // 화면 회전이 되어도 동일한 모양을 나타내기 위해 onResume()에 코드 삽입
    @Override
    protected void onResume() {
        super.onResume();

        // toolbar title 설정
        // 화면 회전이 되었을 경우 AppBar가 현재 캘린더를 보는 년도와 월이 유지되도록
        my_toolbar.setTitle(year + "년 " + month + "월");

    }

    // 화면 회전
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 세로 모드
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            chk = true;
            onResume();
        }

        // 가로 모드
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            chk = false;
            onResume();
        }

    }


    // AppBar에 오버플로우 메뉴 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // 오버플로우 메뉴 선택 시 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.month:
                // 월간 달력으로 이동
                MonthFragment monthFragment = new MonthFragment(year, month, date);
                getSupportFragmentManager().beginTransaction().replace(R.id.calendar, monthFragment).commit();
                return true;

            case R.id.week:
                // 주간 달력으로 이동
                WeekFragment weekFragment = new WeekFragment(year, month, date);
                getSupportFragmentManager().beginTransaction().replace(R.id.calendar, weekFragment).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSetYearMonth(int setYear, int setMonth) {
        year = setYear;
        month = setMonth;

        // toolbar title 설정
        my_toolbar.setTitle(setYear + "년 " + setMonth + "월");
    }


    public void restart(){
        MonthFragment monthFragment = new MonthFragment(year, month, date);
        getSupportFragmentManager().beginTransaction().replace(R.id.calendar, monthFragment).commit();
    }
}