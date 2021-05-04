package com.chr.calendarapp.month;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthPagerAdapter extends FragmentStateAdapter {
    // 처음 시작 페이지
    int startPage = 1000;

    // 페이지 넘기기 전의 position
    int position;


    int year, month, date;
    int cnt , chkCnt;


    // Activity AppBar 년도 월 변경을 위해 Fragment로 전달하는 year, month
    int setYear, setMonth;

    Activity activity;


    public MonthPagerAdapter(FragmentActivity fa, int year, int month, int date) {
        super(fa);
        this.activity = fa;
        this.year = year;
        this.month = month;
        this.date = date;

        position = startPage;
        cnt = -1;
    }


    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public MonthDayFragment createFragment(int currentPosition) {

        if(currentPosition > position){
            position++;
            setNextYearMonth();
        }
        if(currentPosition < position){
            position--;
            setPrevYearMonth();
        }
        setYear = year;
        setMonth = month;
        Log.i("test",  year +" "+ month );
        return new MonthDayFragment(activity, getCalendarDay(year, month), setYear, setMonth);
    }






    // Next 년도, 월 세팅하기
    public void setNextYearMonth(){

        // month가 12보다 작다면 1씩 월을 올리고
        if(month < 12) {
            month++;
        }

        // 아니라면 다음년도로 넘어가는 것이기 때문에 year를 다음년도로 바꾸고 month를 1월로 세팅
        else {
            year++;
            month = 1;
        }

        // Next 년도, 월 전달
        //setYear = year;
        //setMonth = month;

    }


    // Prev 년도, 월 세팅하기
    public void setPrevYearMonth(){

        Log.i("setPrevYearMonth", "come in");

        if(month > 1) {
            month--;
        }

        // 아니라면 이전년도로 넘어가는 것이기 때문에 year를 전년도로 바꾸고 month를 12월로 세팅
        else {
            year--;
            month = 12;
        }

        // Prev 년도, 월 전달
        //setYear = year;
        //setMonth = month;

    }


    // 날짜 ArrayList 받기
    public ArrayList getCalendarDay(int year, int month){

        Log.i("Adaptet" , "y : " + year + " m : " + month);

        Calendar cal = Calendar.getInstance();

        // 날짜 데이터
        ArrayList dayList = new ArrayList();

        //첫째 주 시작 요일 맞추기
        cal.set(year, month - 1, 1);
        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        if (startDay != 1) {
            for (int i = 0; i < startDay - 1; i++) {
                dayList.add(" ");
            }
        }

        // 월에 해당하는 마지막 날짜 구하기
        int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 이번달 날짜 수
        for (int i = 1 ;i <= day; i++) {
            dayList.add(i);
        }

        return dayList;
    }

    // 페이지 개수
    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}