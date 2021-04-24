package com.chr.calendarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.chr.calendarapp.week.WeekFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Toolbar
    Toolbar my_toolbar;

    // 캘린더
    Calendar cal;

    int year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // 화면 회전이 되어도 동일한 모양을 나타내기 위해 onResume()에 코드 삽입
    @Override
    protected void onResume() {
        super.onResume();

        my_toolbar = findViewById(R.id.my_toolbar);
        // toolbar 설정
        setSupportActionBar(my_toolbar);

        // 현재 시간을 받는다
        cal = Calendar.getInstance();
        // 현재 년도 받기
        year = cal.get(Calendar.YEAR);
        // 현재 월 받기
        month = cal.get(Calendar.MONTH)+1;

        // toolbar title 설정
        my_toolbar.setTitle(year + "년 " + month + "월");
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
                return true;

            case R.id.week:
                // 주간 달력으로 이동
                WeekFragment detailsFragment = new WeekFragment(year, month);
                getSupportFragmentManager().beginTransaction().replace(R.id.calendar, detailsFragment).commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // 화면 회전
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 세로 모드
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            onResume();
        }

        // 가로 모드
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onResume();
        }

    }
}