package com.chr.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewActivity extends AppCompatActivity {

    // 캘린더
    Calendar cal;

    TextView text;

    // 캘린더를 나타내는 gridView
    GridView gridview;

    // 버튼
    Button btn_prev, btn_next;

    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);

        // intent 가져온다
        Intent i = getIntent();

        btn_prev = findViewById(R.id.btn_prev);
        btn_next = findViewById(R.id.btn_next);

        text = (TextView)findViewById(R.id.yearmonth);

        gridview = (GridView) findViewById(R.id.gridview);

        // 현재 시간을 받는다
        cal = Calendar.getInstance();

        // 현재 년도 받기
        year = i.getIntExtra("year",cal.get(Calendar.YEAR));
        // 현재 월 받기
        month = i.getIntExtra("month",cal.get(Calendar.MONTH)+1);

        //버튼리스너
        btn_prev.setOnClickListener(click);
        btn_next.setOnClickListener(click);

        // 캘린더를 만드는 함수 호출
        calendarView(year,month);

    }

    public void calendarView(int year, int month){
        // 해당 월의 날짜를 담는 ArrayList
        ArrayList Data = new ArrayList();

        //연도, 월
        text.setText(year + "년 " + month + "월");

        //첫째주 시작요일맞추기
        cal.set(year, month - 1, 1);
        int startday = cal.get(Calendar.DAY_OF_WEEK);
        if (startday != 1) {
            for (int i = 0; i < startday - 1; i++) {
                Data.add(' ');
            }
        }

        // 월에 해당하는 마지막 날짜 구하기
        day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 이번달 날짜 수
        for (int i = 1 ;i <= day; i++) {
            Data.add(i);
        }

        //어댑터 준비 (배열 객체 이용, simple_list_item_1 리소스 사용)
        ArrayAdapter<String> adapt
                = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                Data);

        // 어댑터를 GridView 객체에 연결
        gridview.setAdapter(adapt);
        //날짜선택시 Toast메시지
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MonthViewActivity.this,
                        "" + year + "." + month + "."+ (position+1-startday+1),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 바튼 클릭 시 여기로 들어옴
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // btn의 id로 구분
            switch (view.getId()){
                case R.id.btn_prev:

                    // month가 1보다 크다면 줄이고
                    if(month > 1) {
                        month--;
                    }

                    // 아니라면 이전년도로 넘어가는 것이기 때문에 year를 전년도로 바꾸고 month를 12월로 세팅
                    else {
                        year--;
                        month = 12;
                    }

                    // Intent로 화면 전환
                    Intent i_prev = new Intent(getApplicationContext(), MonthViewActivity.class);
                    // 값을 가지고 이동
                    i_prev.putExtra("year", year);
                    i_prev.putExtra("month", month);
                    // 화면 전환
                    startActivity(i_prev);
                    // 현재 액티비티 종료
                    finish();
                    break;

                case R.id.btn_next:

                    // month가 12보다 작다면 1씩 월을 올리고
                    if(month < 12) {
                        month++;
                    }

                    // 아니라면 다음년도로 넘어가는 것이기 때문에 year를 다음년도로 바꾸고 month를 1월로 세팅
                    else {
                        year++;
                        month = 1;
                    }

                    // Intent로 화면 전환
                    Intent i_next = new Intent(getApplicationContext(), MonthViewActivity.class);
                    // 값을 가지고 이동
                    i_next.putExtra("year", year);
                    i_next.putExtra("month", month);
                    // 화면 전환
                    startActivity(i_next);
                    // 현재 액티비티 종료
                    finish();
                    break;
            }
        }
    };
}