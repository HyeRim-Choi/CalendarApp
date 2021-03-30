package com.chr.calendarapp;

import java.util.ArrayList;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 캘린더 클래스
    Calendar cal_tmp;
    Calendar cal;

    // 버튼
    Button btn_prev, btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_prev = findViewById(R.id.btn_prev);
        btn_next = findViewById(R.id.btn_next);

        // 현재 시간을 받는다
        cal_tmp = Calendar.getInstance();
        // 현재 시간을 받는다
        cal = Calendar.getInstance();

        // 해당 월의 날짜를 담는 ArrayList
        ArrayList Data = new ArrayList();

        // 현재 월 얻기
        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
        int month = cal.get(Calendar.MONTH);

        //현재년월
        TextView text = (TextView)findViewById(R.id.yearmonth);
        text.setText(cal.get(Calendar.YEAR) + "년 " + month + "월");


        //첫째주 시작요일맞추기
        cal_tmp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, 1);
        int startday = cal_tmp.get(Calendar.DAY_OF_WEEK);
        if (startday != 1) {
            for (int i = 0; i < startday - 1; i++) {
                Data.add(' ');
            }
        }

        //이번달 요일수
        /*for (int i = 0 ;i < cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            Data.add(i);
        }*/

        // 이번달 날짜 수
        for (int i = 1 ;i <= cal.getMaximum(Calendar.DAY_OF_MONTH); i++) {
            Data.add(i);
        }


        //어댑터 준비 (배열 객체 이용, simple_list_item_1 리소스 사용)
        ArrayAdapter<String> adapt
                = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                Data);

        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview = (GridView) findViewById(R.id.gridview);
        // 어댑터를 GridView 객체에 연결
        gridview.setAdapter(adapt);
        //날짜선택시 Toast메시지
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this,
                        "" + cal.get(Calendar.YEAR)+ "." + cal.get(Calendar.MONTH) + "."+ (position),
                        Toast.LENGTH_SHORT).show();
            }
        });


        //이번년 이번달설정

        //버튼리스너
        btn_prev.setOnClickListener(click);
        btn_next.setOnClickListener(click);

    }

    // 바튼 클릭 시 여기로 들어옴
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // btn의 id로 구분분
            switch (view.getId()){
                case R.id.btn_prev:
                    break;

                case R.id.btn_next:
                    break;
            }
        }
    };
}