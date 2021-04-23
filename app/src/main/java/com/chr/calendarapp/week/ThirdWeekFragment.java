package com.chr.calendarapp.week;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.chr.calendarapp.R;

import java.util.ArrayList;


public class ThirdWeekFragment extends Fragment {

    GridView grid_third, grid_week;
    ListView list_time;

    // 0 ~ 23
    ArrayList time;
    // GridView의 빈칸
    ArrayList week;
    // 셋째주 날짜 ArrayList
    ArrayList<Integer> dayThirdWeek;



    public ThirdWeekFragment(ArrayList<Integer> dayList) {
        dayThirdWeek = new ArrayList<>();
        // 첫 째주 날짜 ArrayList
        for(int i=14;i<21;i++){
            dayThirdWeek.add(dayList.get(i));
            Log.i("ThirdWeek", "" + dayList.get(i));
        }

        time = new ArrayList();
        for(int i=0;i<24;i++){
            time.add(i);
        }

        week = new ArrayList();
        for(int i=0;i<24*7;i++){
            week.add("");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third_week, container, false);

        grid_week = v.findViewById(R.id.grid_week);
        grid_third = v.findViewById(R.id.grid_third);
        list_time = v.findViewById(R.id.list_time);

        // 0 ~ 23 시간
        @SuppressLint("ResourceType")
        ArrayAdapter<Integer> adapt_list = new ArrayAdapter<Integer>(getActivity(), R.layout.week_time, time);
        list_time.setAdapter(adapt_list);

        // week의 칸
        ArrayAdapter<String> adapt_grid = new ArrayAdapter<String>(getActivity(), R.layout.week, week);
        grid_week.setAdapter(adapt_grid);

        // 둘째주 날짜
        ArrayAdapter<Integer> adapt_week = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dayThirdWeek);
        grid_third.setAdapter(adapt_week);

        return v;
    }
}