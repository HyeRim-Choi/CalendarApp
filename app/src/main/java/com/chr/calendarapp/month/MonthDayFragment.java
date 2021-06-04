package com.chr.calendarapp.month;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.chr.calendarapp.DB.DBHelper;
import com.chr.calendarapp.DB.UserContract;
import com.chr.calendarapp.MainActivity;
import com.chr.calendarapp.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthDayFragment extends Fragment {
    GridView grid_month, x;
    ArrayList calendarDay;
    Activity activity;
    int year, month ,tmppos;
    public DBHelper mDbHelper;


    Calendar cal = Calendar.getInstance();

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

        MainActivity.viewAllToTextView();


        int chkdate = 0;
        //일정표시
        if(MainActivity.db.size() > 0) {
            for(int i=0; i<7; i++)
                calendarDay.remove(" ");
            for (int i = 0; i < 3; i++) { //tableraw 개수
                if (year == Integer.parseInt(MainActivity.db.get((3 + (i*12))).toString())) {
                    if (month == Integer.parseInt(MainActivity.db.get((4 + (i * 12))).toString())) {
                        for (int date = 0; date < calendarDay.size(); date++) {
                            int a = Integer.parseInt(calendarDay.get(date).toString());

                            if (a == Integer.parseInt(MainActivity.db.get((5 + (i * 12))).toString())) {
                                calendarDay.set(date, date + 1 + "\n date");
                                Log.i("add", "xxxxxxx "+ calendarDay.size());
                                Log.i("add", "aaaaa1 "+ Integer.parseInt(MainActivity.db.get((5 + (i * 12))).toString()));

                            }
                            Log.i("add", "aaaaa2 "+ Integer.parseInt(MainActivity.db.get(17).toString()));
                        }
                    }
                }

            }
        }

        cal.set(year, month - 1, 1);
        int startDay = cal.get(Calendar.DAY_OF_WEEK);

        if (startDay != 1) {
            for (int i = 0; i < startDay - 1; i++) {
                calendarDay.add(0, "");
            }
        }

        //6x7칸 맞추기
        while(true) {
            if (calendarDay.size() < 42) {
                calendarDay.add("");
            }else if(calendarDay.size() == 42){
                break;
            }
        }



        View v = inflater.inflate(R.layout.fragment_month_day, container, false);
            grid_month = v.findViewById(R.id.grid_month_day);


            //세로모드
        if(MainActivity.chk == true){
                ArrayAdapter<Integer> adapt_grid = new ArrayAdapter<Integer>(getActivity(), R.layout.month, calendarDay);
            grid_month.setAdapter(adapt_grid);
                Log.i("test ", "세로");
            }
            //가로모드
            else{
                ArrayAdapter<Integer> adapt_grid = new ArrayAdapter<Integer>(getActivity(), R.layout.month2, calendarDay);
                grid_month.setAdapter(adapt_grid);
            Log.i("test ", "가로");

        }

        //날짜 선택시 Toast년월일 출력과 선택 색칠
        grid_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int tmpDate = (position+1-startDay+1);
                Toast.makeText(getContext(),  year+"."+month+"." + tmpDate, Toast.LENGTH_SHORT).show();
                grid_month.getChildAt(position).setBackgroundColor(Color.CYAN);
                grid_month.getChildAt(tmppos).setBackgroundColor(Color.WHITE);
                tmppos = position;
                //일정추가
                MainActivity.regYear = year;
                MainActivity.regMonth = month;
                MainActivity.regDate = tmpDate;
                MainActivity.regTime = cal.get(Calendar.HOUR);
            }
        });
        return v;
    }

    // 인터페이스 추가 정의
    public interface OnSetYearMonthListener {
        void onSetYearMonth(int year, int month);
    }


}