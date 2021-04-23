package com.chr.calendarapp.week;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class WeekPagerAdapter extends FragmentStateAdapter {
    // 월의 주 개수 받기
    private static int NUM_ITEMS = 5;

    int year, month;

    public WeekPagerAdapter(FragmentActivity fa, int year, int month) {
        super(fa);
        this.year = year;
        this.month = month;
        getCalendarDay(year, month);

    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int position) {

        Log.i("postition", "" + position);

      /*if(position == NUM_ITEMS){
           position = 0;

           // month가 12보다 작다면 1씩 월을 올리고
           if(month < 12) {
               month++;
           }

           // 아니라면 다음년도로 넘어가는 것이기 때문에 year를 다음년도로 바꾸고 month를 1월로 세팅
           else {
               year++;
               month = 1;
           }
        }

        else if(position < 0){
            position = NUM_ITEMS - 1;

            if(month > 1) {
                month--;
            }

            // 아니라면 이전년도로 넘어가는 것이기 때문에 year를 전년도로 바꾸고 month를 12월로 세팅
            else {
                year--;
                month = 12;
            }
        }*/


        switch (position) {
            // 첫째주
            case 0:
                FirstWeekFragment first = new FirstWeekFragment(getCalendarDay(year, month));
                return first;
            // 둘째주
            case 1:
                Log.i("WeekPagerAdapter", "second in");
                SecondWeekFragment second = new SecondWeekFragment(getCalendarDay(year, month));
                return second;
            // 샛째주
            //case 2:
                /*ThirdFragment third = new ThirdFragment();
                return third;*/
            default:
                Log.i("WeekPagerAdapter", "null");
                FirstWeekFragment first1 = new FirstWeekFragment(getCalendarDay(year, month));
                return first1;
        }
    }

    // 전체 페이지 개수 반환
    @Override
    public int getItemCount() {
        return NUM_ITEMS;
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
        Log.i("Adapter", "day : " + day);

        // 이번달 날짜 수
        for (int i = 1 ;i <= day; i++) {
            dayList.add(i);
        }

        // 함수를 호출할 떄마다 NUM이 더해짐
        // 해당 월의 주 개수 구하기
       /* for(int i=0; i< dayList.size(); i++){
            if(i%6 == 0){
                NUM_ITEMS++;
            }
            Log.i("Adapter", "" + dayList.get(i));
        }

        Log.i("Adapter", "" + NUM_ITEMS);*/

        return dayList;
    }
}
