package com.chr.calendarapp.month;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.chr.calendarapp.R;

import java.time.MonthDay;


public class MonthFragment extends Fragment{
    //초기 년,월
    int setyear;
    int setmonth;

    int year, month, date;
    int postmp = 1000;

    public MonthFragment(int year, int month, int date) {
        this.setyear = year;
        this.setmonth = month;
        this.year = year;
        this.month = month;
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_month, container, false);

        // ViewPager 추가
        ViewPager2 vpPager = v.findViewById(R.id.vpPager2);
        FragmentStateAdapter adapter = new MonthPagerAdapter(getActivity(), year, month, date);
        vpPager.setAdapter(adapter);

        // 먼저 보여질 창(Integer Max값의 중간 값으로 세팅)
        vpPager.setCurrentItem(1000, false);

        //AppBar 년, 월 설정
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.i("test", " selected position "+  position);

                //왼쪽으로 이동하면
                if(position < postmp){
                    setPrevYearMonth();
                }
                //오른쪽으로 이동하면
                else if(position > postmp) {
                    setNextYearMonth();
                }
                //AppBar설정
                ((MonthDayFragment.OnSetYearMonthListener)getActivity()).onSetYearMonth(setyear, setmonth);
                postmp = position;
            }
        });
        return v;
    }

    // AppBar Next 년도, 월 구하기
    public void setNextYearMonth(){
        // month가 12보다 작다면 1씩 월을 올리고
        if(setmonth < 12) {
            setmonth++;
        }

        // 아니라면 다음년도로 넘어가는 것이기 때문에 year를 다음년도로 바꾸고 month를 1월로 세팅
        else {
            setyear++;
            setmonth = 1;
        }
    }

    // AppBar Prev 년도, 월 구하기
    public void setPrevYearMonth(){
        Log.i("setPrevYearMonth", "come in");

        if(setmonth > 1) {
            setmonth--;
        }

        // 아니라면 이전년도로 넘어가는 것이기 때문에 year를 전년도로 바꾸고 month를 12월로 세팅
        else {
            setyear--;
            setmonth = 12;
        }
    }

}
