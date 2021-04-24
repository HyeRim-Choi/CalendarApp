package com.chr.calendarapp.week;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.Calendar;


public class WeekPagerAdapter extends FragmentStateAdapter {

    // 시작하는 페이지
    int startPage = 1073741820;

    // 월의 주 개수 받기
    int weekNum;

    // 현재 position 에서 + weekNum 하여 어느 position 까지가 4월인지 알기 위한 변수
    int setPosWeekNum;

    // cnt라는 변수를 WeekDayFragment로 전달하여 1,2,3 각각 주의 날짜 저장하기
    int cnt;

    int year, month;


    public WeekPagerAdapter(FragmentActivity fa, int year, int month) {
        super(fa);
        this.year = year;
        this.month = month;
        cnt = 0;

        // 해당 월의 주 개수 구하기
        setWeekNum(startPage);

    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int position) {

        Log.i("postition", "" + position);

        // 현재 position과 4월 마지막 주를 나타내는 setPosWeekNum와 값이 같다면
        if(position == setPosWeekNum){
            // 다음달로 넘어가도록 세팅
            setNextYearMonth(position);
            cnt = 0;
        }

        Log.i("week", "" + cnt);

        return new WeekDayFragment(getCalendarDay(year, month), weekNum, cnt++);

    }


    // 페이지 개수
    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    // 해당 월의 주 개수 구하기
    public void setWeekNum(int position) {
        weekNum = 0;
        setPosWeekNum = 0;

        for(int i = 1; i< getCalendarDay(year, month).size(); i++){
            // 7일째가 되면 weekNum++ 하기
            if(i%6 == 0){
                weekNum++;
            }
        }

        // 해당 월 마지막 페이지 세팅
        setPosWeekNum = position + weekNum;

        Log.i("Adapter", "" + weekNum);
        Log.i("Adapter", "" + setPosWeekNum);
    }



    // Next 년도, 월 세팅하기
    public void setNextYearMonth(int position){

        // month가 12보다 작다면 1씩 월을 올리고
        if(month < 12) {
            month++;
        }

        // 아니라면 다음년도로 넘어가는 것이기 때문에 year를 다음년도로 바꾸고 month를 1월로 세팅
        else {
            year++;
            month = 1;
        }

        // 다음 달 position 세팅하기
        setWeekNum(position);

    }


    // Prev 년도, 월 세팅하기
    public void setPrevYearMonth(int position){

        Log.i("setPrevYearMonth", "come in");


        if(month > 1) {
            month--;
        }

        // 아니라면 이전년도로 넘어가는 것이기 때문에 year를 전년도로 바꾸고 month를 12월로 세팅
        else {
            year--;
            month = 12;
        }

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
}
