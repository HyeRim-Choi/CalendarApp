package com.chr.calendarapp.month;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

public class MonthPagerAdapter extends FragmentStateAdapter {

    // 처음 시작 페이지
    int startPage = 1000;

    // 페이지 넘기기 전의 position
    int position;


    static int year, month, date;
    int cnt =0;
    int view = 1000;

    Activity activity;


    public MonthPagerAdapter(FragmentActivity fa, int year, int month, int date) {
        super(fa);
        this.activity = fa;
        this.year = year;
        this.month = month;
        this.date = date;

        position = startPage;
    }


    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int currentPosition) {

        //4차이났을경우
        if(currentPosition - position == 4){
            Log.i("test", " 4 " );
            fourNextYearMonth();
        }else if(currentPosition - position == -4){
            Log.i("test", " -4 " );
            fourPrevYearMonth();
        }
        //3차이났을경우
        else if(currentPosition - position == 3){
            Log.i("test", " 4 " );
            thirdNextYearMonth();
        }else if(currentPosition - position == -3){
            Log.i("test", " 4 " );
            thirdPrevYearMonth();
        }

//        if(currentPosition > position) {
//            view = currentPosition -1;
//            Log.i("test", " view " + view);
//        }

        if(currentPosition - position == 1){
            setNextYearMonth();
        }else if(currentPosition - position == -1){
            setPrevYearMonth();
        }else{
            Log.i("test", " 시작 " );
        }

        Log.i("test", " month : " + month + " year : " + year + " current " + currentPosition + " position "+ position);
        position = currentPosition;

        return new MonthDayFragment(activity, getCalendarDay(year, month), year, month);
    }




    // currentpostion맞추기 4차이
    public void fourNextYearMonth(){
        for(int i=0; i<4; i++){
            setNextYearMonth();
        }
    }
    public void fourPrevYearMonth(){
        for(int i=0; i<4; i++){
            setPrevYearMonth();
        }
    }

    // currentpostion맞추기 3차이
    public void thirdNextYearMonth(){
        for(int i=0; i<3; i++){
            setNextYearMonth();
        }
    }
    public void thirdPrevYearMonth(){
        for(int i=0; i<3; i++){
            setPrevYearMonth();
        }
    }

    // Next 년도, 월 세팅하기
    public static void setNextYearMonth(){

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
    public static void setPrevYearMonth(){
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