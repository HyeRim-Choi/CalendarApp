package com.chr.calendarapp.month;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.chr.calendarapp.R;
import com.chr.calendarapp.week.WeekDayFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MonthDayFragment extends Fragment {
    GridView grid_month;
    ArrayList calendarDay;
    Activity activity;
    int year, month;


    public MonthDayFragment(Activity activity, ArrayList calendarDay, int setYear, int setMonth) {

        this.year = setYear;
        this.month = setMonth;
        this.activity = activity;
        this.calendarDay = calendarDay;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_month_day, container, false);
        grid_month = v.findViewById(R.id.grid_month_day);

        while(true) {
            if (calendarDay.size() < 42) {
                calendarDay.add("");
            }else if(calendarDay.size() == 42){
                break;
            }
        }
        ArrayAdapter<String> adapt_grid = new ArrayAdapter<String>(getActivity(), R.layout.week, calendarDay);
        grid_month.setAdapter(adapt_grid);


        //if (activity instanceof WeekDayFragment.OnSetYearMonthListener)
            ((MonthDayFragment.OnSetYearMonthListener)activity).onSetYearMonth(year, month);

        //ArrayAdapter<String> adapt_grid = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, calendarDay);
        //grid_month.setAdapter(adapt_grid);


            // Inflate the layout for this fragment
        return v;
    }
    // 인터페이스 추가 정의
    public interface OnSetYearMonthListener {
        void onSetYearMonth(int year, int month);
    }
}