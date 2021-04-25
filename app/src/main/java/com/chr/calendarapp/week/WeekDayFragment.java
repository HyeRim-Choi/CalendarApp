package com.chr.calendarapp.week;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.chr.calendarapp.R;

import java.util.ArrayList;


public class WeekDayFragment extends Fragment {

    GridView grid_week_day, grid_week;
    TextView txt_time;

    // GridView의 빈칸
    ArrayList week;
    // 날짜 ArrayList
    ArrayList<Integer> dayWeek;

    // 월의 주 개수 받기
    int weekNum;


    public WeekDayFragment(ArrayList<Integer> dayList, int weekNum, int cnt) {
        this.weekNum = weekNum;

        Log.i("WeekDayConstructor", "come in");


        dayWeek = new ArrayList<>();

        // getDay 함수 호출
        int day = getDay(dayList, cnt);

        // 1,2,3 각각 주의 날짜 ArrayList
        for(int i = 7 * cnt ; i < day ; i++){
            dayWeek.add(dayList.get(i));
        }

        // GridView Week 칸 ArrayList
        week = new ArrayList();
        for(int i=0;i<24*7;i++){
            week.add("");
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

        Log.i("day", "" + day);

        return day;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_week_day, container, false);

        grid_week = v.findViewById(R.id.grid_week);
        grid_week_day = v.findViewById(R.id.grid_week_day);
        txt_time = v.findViewById(R.id.txt_time);

        // 0 ~ 23 시간
        for(int i = 1; i < 24; i++){
            txt_time.append(i+"\n\n\n");
        }

        // week의 칸
        ArrayAdapter<String> adapt_grid = new ArrayAdapter<String>(getActivity(), R.layout.week, week);
        grid_week.setAdapter(adapt_grid);

        // 주 날짜
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


        return v;
    }
}