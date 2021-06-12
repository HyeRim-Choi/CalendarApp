package com.chr.calendarapp.month;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.chr.calendarapp.MainActivity;
import com.chr.calendarapp.R;
import com.chr.calendarapp.database.ScheduleDatabaseManager;
import com.chr.calendarapp.database.ScheduleVO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MonthDayFragment extends Fragment {
    public GridView grid_month;
    ArrayList calendarDay;

    Activity activity;
    int year, month, date, tmppos;

    // 일정추가버튼
    FloatingActionButton fab_add;

    Calendar cal = Calendar.getInstance();

    // database
    ScheduleDatabaseManager scheduleDatabaseManager;

    // dialog목록
    ArrayList<String> items = new ArrayList<>();

    public MonthDayFragment(Activity activity, ArrayList calendarDay, int year, int month) {
        this.activity = activity;
        this.calendarDay = calendarDay;
        this.year = year;
        this.month = month;
    }

    public MonthDayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 캘린더 시간맞춤
        cal.set(year, month - 1, 1);

        //6x7칸 맞추기
        while (true) {
            if (calendarDay.size() < 42) {
                calendarDay.add("");
            } else if (calendarDay.size() == 42) {
                break;
            }
        }
        Log.i("qwer1", calendarDay + "calandar");


        // 6x7칸 맞추기
        for(int j = 0; j < 42; j++){
            // DB 에서 Title 불러오기
            selectSchedule(year, month, j);
        }

        View v = inflater.inflate(R.layout.fragment_month_day, container, false);
        grid_month = v.findViewById(R.id.grid_month_day);

        // 일정추가버튼
        fab_add = v.findViewById(R.id.fab_add);

        //세로모드
        if (MainActivity.chk == true) {
            ArrayAdapter<Integer> adapt_grid = new ArrayAdapter<Integer>(getActivity(), R.layout.month, calendarDay);
            grid_month.setAdapter(adapt_grid);
            Log.i("test ", "세로");
        }
        //가로모드
        else {
            ArrayAdapter<Integer> adapt_grid = new ArrayAdapter<Integer>(getActivity(), R.layout.month2, calendarDay);
            grid_month.setAdapter(adapt_grid);
            Log.i("test ", "가로");
        }

        int startDay = cal.get(Calendar.DAY_OF_WEEK);

        //날짜 선택시 Toast년월일 출력과 선택 색칠
        grid_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int tmpDate = (position + 1 - startDay + 1);
                Toast.makeText(getContext(), year + "." + month + "." + tmpDate, Toast.LENGTH_SHORT).show();
                grid_month.getChildAt(position).setBackgroundColor(Color.CYAN);
                grid_month.getChildAt(tmppos).setBackgroundColor(Color.WHITE);
                tmppos = position;

                //선택한 날짜
                date = tmpDate;
                String[] columns = new String[]{"title", "time_start", "time_end", "place", "latitude", "longitude", "memo"};

                // where문
                String selection = "year=" + year + " and month=" + month + " and date=" + date;

                Cursor cursor = scheduleDatabaseManager.query(columns, selection, null, null, null, null);

                // dialog에 title추가
                items.clear();
                while (cursor.moveToNext()) {
                    items.add(cursor.getString(0));
                }

                // 일정추가되있으면 dialog 출력
                if (calendarDay.get(position).toString().length() > 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    // dialog title year.month.date출력
                    builder.setTitle(year + "." + month + "." + date + "일");
                    CharSequence[] items1 = items.toArray(new String[items.size()]);

                    builder.setItems(items1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int pos) {

                            // dialog에 타이틀 목록 출력
                            Toast.makeText(activity.getApplicationContext(), items1[pos], Toast.LENGTH_LONG).show();

                            // vo를 가지고 상세일정 창으로 이동하기
                            ScheduleVO vo = selectSchedule(year, month, date);
                            Intent i = new Intent(activity, MonthRegisterScheduleActivity.class);
                            i.putExtra("year", year);
                            i.putExtra("month", month);
                            i.putExtra("date", date);
                            i.putExtra("time", cal.get(Calendar.HOUR));
                            i.putExtra("schedule", (Serializable) vo);
                            startActivityForResult(i, 1000);
                        }
                    });

                    // dialog 출력
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        fab_add.setOnClickListener(click);
        return v;
    }

    // 일정추가버튼누를시
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 일정 추가 버튼 클릭 시
                case R.id.fab_add:
                    Intent i = new Intent(activity, MonthRegisterScheduleActivity.class);
                    i.putExtra("year", year);
                    i.putExtra("month", month);
                    i.putExtra("date", date);
                    i.putExtra("longitude", "");
                    i.putExtra("latitude", "");
                    i.putExtra("time", cal.get(Calendar.HOUR));
                    i.putExtra("schedule", (Serializable) null);
                    startActivityForResult(i, 1000);
                    break;
            }
        }
    };


    public ScheduleVO selectSchedule(int year, int month, int date) {
        scheduleDatabaseManager = ScheduleDatabaseManager.getInstance(activity);

        // 월에맞는 date
        int date1 = date + cal.get(Calendar.DAY_OF_WEEK) -2;

        // select
        String[] columns = new String[]{"title", "time_start", "time_end", "place", "latitude", "longitude", "memo"};
        // where문
        String selection = "year=" + year + " and month=" + month + " and date=" + date;

        Cursor cursor = scheduleDatabaseManager.query(columns, selection, null, null, null, null);
        Cursor cursorChk = scheduleDatabaseManager.query(columns, selection, null, null, null, null);

        // DB에서 가져 온 데이터들 저장하기
        ScheduleVO vo = new ScheduleVO();

        if (cursor != null) {

            // title이 존재하지 않는곳에 date넣기
            if (!cursorChk.moveToNext()) {
                calendarDay.set(date, calendarDay.get(date) );
                Log.i("qwer", date +"타이틀없음" + calendarDay.get(date));

            }
                while (cursor.moveToNext()) {
                    String subTitle;
                    // title이 존재하면 격자 grid에 title 넣기
                    if(calendarDay.get(date).equals("")){
                        Log.i("qwer", "빈값" +calendarDay.get(date) );

                    }else {
                        // title이 너무 길면 자름
                        if(cursor.getString(0).length() > 5){
                            subTitle = cursor.getString(0).substring(0,5) + "...";
                        }else{
                            subTitle = cursor.getString(0);
                        }

                        // grid에 date + title넣기
                        calendarDay.set(date1 , calendarDay.get(date1) + "\n" + subTitle); // title

                        // vo set
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

        if (requestCode != 1000) {
            return;
        }

        //프래그먼트 재시작
        MonthFragment monthFragment = new MonthFragment(year, month, date);
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.replace(R.id.calendar, monthFragment);
        tr.commit();
    }

    // 인터페이스 추가 정의
    public interface OnSetYearMonthListener {
        void onSetYearMonth(int year, int month);
    }
}

