package com.chr.calendarapp.week;


import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.Calendar;


public class WeekPagerAdapter extends FragmentStateAdapter {
    // 처음 시작 페이지
    int startPage = 1000;

    // 월의 주 개수 받기
    int weekNum;

    // 페이지 넘기기 전의 position
    int position;

    // 첫째 주인지 둘째 주인지 알기 위한 변수
    int cnt, chkCnt;

    // 현재 날짜로 배경색을 세팅하기 위한 변수
   int chkDate;

    // 계속 한 쪽으로 슬라이딩하다가 반대쪽으로 슬라이딩했는지 체크
    boolean chkPosition1;

    // 계속 한 쪽으로 슬라이딩하다가 반대쪽으로 슬라이딩 구분
    int chkPosition2;

    int year, month, date;

    // Activity AppBar 년도 월 변경을 위해 Fragment로 전달하는 year, month
    int setYear, setMonth;

    Activity activity;


    public WeekPagerAdapter(FragmentActivity fa, int year, int month, int date) {
        super(fa);
        this.activity = fa;
        this.year = year;
        this.month = month;
        this.date = date;

        chkDate = 1;

        cnt = -1;

        position = startPage;

        // 해당 달 weekNum 세팅
        setWeekNum();

    }


    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int currentPosition) {
        Log.i("WeekPagerAdapter", "currentPosition : " + currentPosition);
        Log.i("WeekPagerAdapter", "position : " + position);
        Log.i("WeekPagerAdapter", "cnt : " + cnt);

        // 첫 화면에 현재 날짜가 있는 페이지로 세팅
        if(chkDate == 1){
            Log.i("WeekPagerAdapter", "처음처음");
            cnt = setCalendarPage() - 1;
            chkDate = date;
            return new WeekDayFragment(activity, getCalendarDay(year, month), weekNum, cnt, year, month, setYear, setMonth, chkDate);
        }

        setYear = 0;
        setMonth = 0;


        // 현재 position이 전 position보다 작다면
        if(currentPosition < position){
            // 전 주로 세팅
            cnt--;
        }
        else{
            // 다음 주로 세팅
            cnt++;
        }


        // 계속 한 쪽으로 슬라이딩하다가 반대쪽으로 슬라이딩을 하면 position 값이 3 이상 차이가 남
        // 계속 왼쪽으로 슬라이딩을 하다가 오른쪽으로 슬라이딩을 전환하면
        if(currentPosition - position >= 3){
            chkPosition1 = true;

            // 전 달 마지막 주에서 다음 달로 슬라이딩하는 경우
            if(cnt == weekNum - 1){
                chkPosition2 = 1;
            }

            // 전 달 마지막 - 1주에서 다음 달로 슬라이딩하는 경우
            else if(cnt == weekNum - 2){
                chkPosition2 = 2;
            }

            cnt+=3;

        }

        // 계속 오른쪽으로 슬라이딩을 하다가 왼쪽으로 슬라이딩을 전환하면
       else if(currentPosition - position <= -3){
            chkPosition1 = true;

            // 다음 달 첫째주에서 전 달로 슬라이딩하는 경우
            if(cnt == 0){
                chkPosition2 = 1;
            }

            // 다음 달 둘째주에서 전 달로 슬라이딩하는 경우
            else if(cnt == 1){
                chkPosition2 = 2;
            }

            cnt-=3;
        }


        // 이전 달로 바뀌면
        if(cnt <= -1){
            chkCnt = cnt;

            // year, month 세팅
            setPrevYearMonth();

            // weekNum 세팅
            setWeekNum();

            // cnt 세팅
            cnt = weekNum - 1;

            // 다음 달에서 이전 달로 슬라이딩하면
            if(chkCnt < -1 && chkPosition1 == true){

                // 다음 달 첫째주에서 전 달로 슬라이딩하는 경우
                if(chkPosition2 == 1){
                    cnt-=2;
                }

                // 다음 달 둘째주에서 전 달로 슬라이딩하는 경우
                else if(chkPosition2 == 2){
                    cnt--;
                }

                else{
                    cnt-=3;
                }

                setYear = year;
                setMonth = month;
            }
        }


        // 다음 달로 바뀌면
        else if(cnt >= weekNum){
            chkCnt = cnt;

            // year, month 세팅
            setNextYearMonth();

            // weekNum 세팅
            setWeekNum();

            // cnt 세팅
            cnt = 0;

            // 이전 달에서 다음 달로 슬라이딩하면
            if(chkCnt > weekNum && chkPosition1 == true){

                // 전 달 마지막 주에서 다음 달로 슬라이딩하는 경우
                if(chkPosition2 == 1){
                    cnt+=2;
                }

                // 전 달 마지막 - 1주에서 다음 달로 슬라이딩하는 경우
                else if(chkPosition2 == 2){
                    cnt++;
                }

                else{
                    cnt+=3;
                }

                setYear = year;
                setMonth = month;

            }
        }

        // Year, Month가 변할 때마다 AppBar를 세팅하기 위한 if문
        if(cnt == 1 || cnt == weekNum - 2){
            setYear = year;
            setMonth = month;
        }

        position = currentPosition;
        chkPosition1 = false;
        chkPosition2 = 0;


        return new WeekDayFragment(activity, getCalendarDay(year, month), weekNum, cnt, year, month, setYear, setMonth, -1);

    }


    // 캘린더 초기 상태 만들기(현재 날짜 페이지가 나오도록)
    public int setCalendarPage(){

        // 일 받아서 현재 날짜로 캘린더 초기 설정
        ArrayList<Integer> dayList = getCalendarDay(year, month);

        int setCnt = 0;

        for(int i=0;i<dayList.size();i++){

            if(i%7 == 0){
                setCnt++;
            }

            if(dayList.get(i) == (Integer) date){
                break;
            }

        }

        // 현재 날짜가 있는 주 전달
        return setCnt;
    }


    // 주 개수 구하는 함수
    public void setWeekNum(){
        weekNum = 0;

        for(int i = 0; i< getCalendarDay(year, month).size(); i++){
            // 주 마다 첫 번째날이 존재하면 weekNum++ 하기
            if(i%7 == 0){
                weekNum++;
            }
        }
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

        // month가 1보다 크다면 1씩 월을 줄이고
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
