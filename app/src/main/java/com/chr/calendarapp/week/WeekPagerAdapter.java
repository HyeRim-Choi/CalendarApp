package com.chr.calendarapp.week;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.Calendar;


public class WeekPagerAdapter extends FragmentStateAdapter {

    // 처음 시작하는 페이지
    int startPage = 15;

    // 월의 주 개수 받기
    int weekNum;

    // 페이지 넘기기 전의 position
    int position;

    // 첫째 주인지 둘째 주인지 알기 위한 변수
    int cnt;

    int year, month;


    public WeekPagerAdapter(FragmentActivity fa, int year, int month) {
        super(fa);
        this.year = year;
        this.month = month;

        cnt = -1;

        // 해당 달 weekNum 세팅
        setWeekNum();

    }


    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int currentPosition) {
        Log.i("postitionCurr", "" + currentPosition);
        Log.i("postition", "" + position);
        Log.i("cnt ", "" + cnt);

        // 현재 position이 전 position보다 작다면
        if(currentPosition < position){
            // 전 주로 세팅
            cnt--;
        }
        else{
            // 다음 주로 세팅
            cnt++;
        }


        if(cnt == -1){
            Log.i("prevMonthPosition : ", "" + "come in");
            // year, month 세팅
            setPrevYearMonth();

            // weekNum 세팅
            setWeekNum();

            // cnt 세팅
            cnt = weekNum - 1;
        }

        else if(cnt == weekNum){
            Log.i("nextMonthPosition : ", "" + "come in");
            // year, month 세팅
            setNextYearMonth();

            // weekNum 세팅
            setWeekNum();

            // cnt 세팅
            cnt = 0;
        }


        if(currentPosition - position == 4){
            cnt+=3;
        }
        else if(currentPosition - position == -4){
            cnt-=3;
        }


        position = currentPosition;


        Log.i("cnt : ", "" + cnt);

        return new WeekDayFragment(getCalendarDay(year, month), weekNum, cnt);

    }


    public void setWeekNum(){
        weekNum = 0;

        for(int i = 0; i< getCalendarDay(year, month).size(); i++){
            // 주 마다 첫 번째날이 존재하면 weekNum++ 하기
            if(i%7 == 0){
                weekNum++;
            }
        }

        Log.i("weekNum : ", "" + weekNum);

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
