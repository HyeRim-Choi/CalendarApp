package com.chr.calendarapp.week;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.chr.calendarapp.MainActivity;
import com.chr.calendarapp.R;
import com.chr.calendarapp.AddRegisterScheduleActivity;
import com.chr.calendarapp.database.ScheduleDatabaseManager;
import com.chr.calendarapp.database.ScheduleVO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;



public class WeekDayFragment extends Fragment {

    int year, month;

    // 초기 배경색 세팅이 되어있는 경우
    GridView grid_week_day, grid_week;
    TextView txt_time;
    // Floating Button 띄우기
    FloatingActionButton fab_add;

    // GridView의 빈칸
    ArrayList week;
    // 날짜 ArrayList
    ArrayList<Integer> dayWeek;

    // 월의 주 개수 받기
    int weekNum;

    // 날짜 GridView 전에 클릭했던 position
    int gridWeekDayPosition;

    // 격자 GridView 전에 클릭했던 position
    int gridWeekPosition;

    /* 만약 날짜 GridView를 클릭했다가 격자 GridView를 클릭했으면 클릭한 날짜 GridView 배경색을 화이트로 바꾸기 위해 */
    // 격자 GridView 클릭했는지 확인
    int chkGridWeekClick;

    // 날짜 GridView 클릭했는지 확인(클릭했으면 클릭한 격자 배경색을 화이트로 바꾸기 위해)
    int chkGridWeekDayClick;

    // 캘린더 초기 설정
    int chkDate;

    Activity activity;

    View v;

    // DB
    ScheduleDatabaseManager scheduleDatabaseManager;

    public WeekDayFragment(Activity activity, ArrayList<Integer> dayList, int weekNum, int cnt, int year, int month, int setYear, int setMonth, int chkDate) {
        this.weekNum = weekNum;
        this.activity = activity;
        this.chkDate = chkDate;
        this.year = year;
        this.month = month;

        // Activity AppBar의 년도, 월을 수정하기 위해 Activity 호출
        if(setYear != 0){
            // 선택된 항목 위치(position)을 OnSetYearMonthListener 인터페이스를 구현한 액티비티로 전달
            if (activity instanceof OnSetYearMonthListener){
                ((OnSetYearMonthListener)activity).onSetYearMonth(setYear, setMonth);
            }
        }

        dayWeek = new ArrayList<>();

        // getDay 함수 호출
        int day = getDay(dayList, cnt);

        // 1,2,3 각각 주의 날짜 ArrayList
        for(int i = 7 * cnt ; i < day ; i++){
            dayWeek.add(dayList.get(i));
        }

    }


    // 마지막 주의 날짜 Index 벗어나지 않게 가져오는 함수
    public int getDay(ArrayList<Integer> dayList, int cnt){
        int day;

        // 마지막 주이면
        if(weekNum - 1 == cnt){
            // 마지막 날짜까지 들어있는 index까지만 날짜 ArrayList에 저장할 수 있도록
            day = dayList.size();
        }

        // 아니라면
        else{
            // 1주의 7일 날짜를 다 저장하도록 day setting
            day = 7 * (cnt + 1);
        }

        return day;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_week_day, container, false);

        grid_week = v.findViewById(R.id.grid_week);
        grid_week_day = v.findViewById(R.id.grid_week_day);
        txt_time = v.findViewById(R.id.txt_time);
        fab_add = v.findViewById(R.id.fab_add);

        // 날짜 칸
        ArrayAdapter<Integer> adapt_week = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_list_item_1, dayWeek){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // ListView Custom
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(12);
                return view;
            }
        };

        grid_week_day.setAdapter(adapt_week);


        // 0 ~ 23 시간
        for(int i = 1; i < 24; i++){
            txt_time.append(i+"");

            if(i != 23){
                txt_time.append("\n\n\n");
            }
        }

        // GridView Week 칸 ArrayList
        week = new ArrayList();
         // 시간( 0 ~ 23 )
        for(int i = 0; i <= 23; i++){
            // 요일(일 ~ 토)
            for(int j = 0; j < 7; j++){
                // DB 에서 Title 불러오기
                selectSchedule(year, month, j, ""+i);
            }
        }

        // week의 격자 칸
        ArrayAdapter<String> adapt_grid = new ArrayAdapter<String>(getActivity(), R.layout.week, week){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // ListView Custom
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(7);
                return view;
            }
        };

        grid_week.setAdapter(adapt_grid);



        // 날짜 클릭 시
        grid_week_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int currentPosition, long id) {
                chkGridWeekDayClick = 1;

                // 격자 칸을 클릭하고 날짜 칸을 클릭했으면 격자 칸 배경색을 화이트로 바꾸기
                if(chkGridWeekClick == 1){
                    grid_week.getChildAt(gridWeekPosition).setBackgroundColor(Color.WHITE);
                    grid_week_day.getChildAt(gridWeekPosition % 7).setBackgroundColor(Color.WHITE);
                }

                // 전에 선택했던 날짜 배경색 화이트로 변경
                grid_week_day.getChildAt(gridWeekDayPosition).setBackgroundColor(Color.WHITE);
                // 선택된 날짜의 배경색 변경
                grid_week_day.getChildAt(currentPosition).setBackgroundColor(Color.CYAN);

                gridWeekDayPosition = currentPosition;

                chkGridWeekClick = 0;

            }
        });


        // 격자 클릭 시
        grid_week.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int currentPosition, long id) {
                chkGridWeekClick = 1;

                // 날짜 칸을 클릭하고 격자 칸을 클릭했으면 날짜 칸 배경색을 화이트로 바꾸기
                if(chkGridWeekDayClick == 1){
                    grid_week_day.getChildAt(gridWeekDayPosition).setBackgroundColor(Color.WHITE);
                }

                // 전에 선택했던 격자 배경색 화이트로 변경
                grid_week.getChildAt(gridWeekPosition).setBackgroundColor(Color.WHITE);
                // 전에 선택된 격자에 해당되는 날짜의 배경색도 변경
                grid_week_day.getChildAt(gridWeekPosition % 7).setBackgroundColor(Color.WHITE);

                // 선택된 격자의 배경색 변경
                grid_week.getChildAt(currentPosition).setBackgroundColor(Color.CYAN);
                // 선택된 격자에 해당되는 날짜의 배경색도 변경
                grid_week_day.getChildAt(currentPosition % 7).setBackgroundColor(Color.CYAN);

                Toast.makeText(getContext(), "position : "+currentPosition, Toast.LENGTH_SHORT).show();

                gridWeekPosition = currentPosition;

                chkGridWeekDayClick = 0;

                // title을 통해 일정 데이터를 VO에 저장하여 상세일정 창으로 보내기
                if(!grid_week.getItemAtPosition(currentPosition).equals("")){
                    // db select 하기
                    ScheduleVO vo = selectSchedule(year, month, gridWeekPosition % 7, ""+(gridWeekPosition / 7));

                    // vo를 가지고 상세일정 창으로 이동하기
                    Intent i = new Intent(activity, WeekRegisterScheduleActivity.class);
                    i.putExtra("year", year);
                    i.putExtra("month", month);
                    i.putExtra("date", (Integer)grid_week_day.getItemAtPosition(gridWeekPosition % 7));
                    i.putExtra("time", gridWeekPosition / 7);
                    i.putExtra("schedule", vo);
                    startActivityForResult(i, 1000);
                }


            }
        });


        fab_add.setOnClickListener(click);

        return v;
    }

    // 버튼 클릭 이벤트
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 일정 추가 버튼 클릭 시
                case R.id.fab_add:
                    Intent i = new Intent(activity, WeekRegisterScheduleActivity.class);
                    i.putExtra("year", year);
                    i.putExtra("month", month);
                    i.putExtra("date", (Integer)grid_week_day.getItemAtPosition(gridWeekPosition % 7));
                    i.putExtra("time", gridWeekPosition / 7);
                    i.putExtra("schedule", (Serializable) null);
                    startActivityForResult(i, 1000);
                    break;
            }
        }
    };


    // select
    public ScheduleVO selectSchedule(int year, int month, int date, String startTime)
    {
        scheduleDatabaseManager = ScheduleDatabaseManager.getInstance(activity);

        String day = "";

        try {
            // 날짜 gridView에서 인덱스에 맞는 날짜 가져오기
            day = (grid_week_day.getItemAtPosition(date)).toString();

        }
        catch (Exception e){
            // 날짜가 존재하지 않으면 " "로 초기화
            day = " ";
            e.printStackTrace();
        }
        finally {
            // db query에 'date= ' 이렇게 빈 값으로 들어가는 것을 방지하기 위해 day를 0으로 세팅
            if(day.equals(" ") || day == " " || day == null){
                day = 0 + "";
            }
        }

        // "09" format으로 세팅
        if(startTime.length() == 1){
            startTime = "0" + startTime;
        }


        // SELECT title FROM schedule WHERE year=2021 and month=6 and date=3 and time_start like '06%'
        // select
        String[] columns = new String[] {"title", "time_start", "time_end", "place", "latitude", "longitude", "memo"};
        // where
        String selection = "year=" + year + " and month=" + month + " and date=" + day  + " and time_start like '" + startTime + "%'";
        Log.i("WeekDayFragment1", day);
        Log.i("WeekDayFragment1", selection);

        Cursor cursor = scheduleDatabaseManager.query(columns, selection, null,null,null,null);
        Cursor cursorChk = scheduleDatabaseManager.query(columns, selection, null,null,null,null);

        // DB에서 가져 온 데이터들 저장하기
        ScheduleVO vo = new ScheduleVO();

        if(cursor != null)
        {

            // title이 존재하지 않으면 격자 grid에 빈 칸 넣기
            if(!cursorChk.moveToNext()){
                week.add("");
            }
            else{
                while (cursor.moveToNext())
                {

                    // title이 존재하면 격자 grid에 title 넣기
                    week.add(cursor.getString(0) + "\n"); // title

                    vo.setTitle(cursor.getString(0)); // title
                    vo.setTime_start(cursor.getString(1)); // time_start
                    vo.setTime_end(cursor.getString(2)); // time_end
                    vo.setPlace(cursor.getString(3)); // place
                    vo.setLatitude(cursor.getDouble(4)); // latitude
                    vo.setLongitude(cursor.getDouble(5)); // longitude
                    vo.setMemo(cursor.getString(6)); // memo

                }
            }

        }

        cursor.close(); // cursor 닫기

        return vo;

    }


    // 일정 등록 액티비티 수행 결과
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("Check", "onActivityResult");

        if (requestCode != 1000){
            return;
        }


        /* 수정 된 grid 칸 업데이트 */
        // GridView Week 칸 ArrayList
        week = new ArrayList();
        // 시간( 0 ~ 23 )
        for(int i = 0; i <= 23; i++){
            // 요일(일 ~ 토)
            for(int j = 0; j < 7; j++){
                // DB 에서 Title 불러오기
                selectSchedule(year, month, j, ""+i);
            }
        }

        // week의 격자 칸
        ArrayAdapter<String> adapt_grid = new ArrayAdapter<String>(getActivity(), R.layout.week, week){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // ListView Custom
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(7);
                return view;
            }
        };

        grid_week.setAdapter(adapt_grid);

    }

    // 인터페이스 추가 정의
    public interface OnSetYearMonthListener {
        public void onSetYearMonth(int year, int month);
    }
}