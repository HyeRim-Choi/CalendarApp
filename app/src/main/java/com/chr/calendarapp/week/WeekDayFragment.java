package com.chr.calendarapp.week;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.chr.calendarapp.R;
import java.util.ArrayList;


public class WeekDayFragment extends Fragment {
    // 초기 배경색 세팅이 되어있는 경우
    GridView grid_week_day, grid_week;
    TextView txt_time;

    // GridView의 빈칸
    ArrayList week;
    // 날짜 ArrayList
    ArrayList<Integer> dayWeek;

    // 월의 주 개수 받기
    int weekNum;

    // 날짜 GridView 전에 클릭했던 position
    int gridWeekDayPosition;

    // 격자 GridView 전에 클릭했던 position
    int gridWeekPosition;

    /* 만약 날짜 GridView를 클릭했다가 격자 GridView를 클릭했으면 클릭한 날짜 GridView 배경색을 화이트로 바꾸기 위해 */
    // 격자 GridView 클릭했는지 확인
    int chkGridWeekClick;

    // 날짜 GridView 클릭했는지 확인(클릭했으면 클릭한 격자 배경색을 화이트로 바꾸기 위해)
    int chkGridWeekDayClick;

    // 캘린더 초기 설정
    int chkDate;

    Activity activity;

    public WeekDayFragment(Activity activity, ArrayList<Integer> dayList, int weekNum, int cnt, int setYear, int setMonth, int chkDate) {
        this.weekNum = weekNum;
        this.activity = activity;
        this.chkDate = chkDate;

        // Activity AppBar의 년도, 월을 수정하기 위해 Activity 호출
        if(setYear!=0){

            // 선택된 항목 위치(position)을 OnSetYearMonthListener 인터페이스를 구현한 액티비티로 전달
            if (activity instanceof OnSetYearMonthListener){
                ((OnSetYearMonthListener)activity).onSetYearMonth(setYear, setMonth);
            }

        }

        dayWeek = new ArrayList<>();

        // getDay 함수 호출
        int day = getDay(dayList, cnt);

        // 1,2,3 각각 주의 날짜 ArrayList
        for(int i = 7 * cnt ; i < day ; i++){
            dayWeek.add(dayList.get(i));
        };

        // GridView Week 칸 ArrayList
        week = new ArrayList();
        for(int i=0;i<23*7;i++){
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
            txt_time.append(i+"");

            if(i != 23){
                 txt_time.append("\n\n\n");
            }
        }

        // week의 격자 칸
        ArrayAdapter<String> adapt_grid = new ArrayAdapter<String>(getActivity(), R.layout.week, week);

        grid_week.setAdapter(adapt_grid);


        // 날짜 칸
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


        // 날짜 클릭 시
        grid_week_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int currentPosition, long id) {
                chkGridWeekDayClick = 1;

                // 격자 칸을 클릭하고 날짜 칸을 클릭했으면 격자 칸 배경색을 화이트로 바꾸기
                if(chkGridWeekClick == 1){
                    grid_week.getChildAt(gridWeekPosition).setBackgroundColor(Color.WHITE);
                    grid_week_day.getChildAt(gridWeekPosition % 7).setBackgroundColor(Color.WHITE);
                }

                // 전에 선택했던 날짜 배경색 화이트로 변경
                grid_week_day.getChildAt(gridWeekDayPosition).setBackgroundColor(Color.WHITE);
                // 선택된 날짜의 배경색 변경
                grid_week_day.getChildAt(currentPosition).setBackgroundColor(Color.CYAN);

                gridWeekDayPosition = currentPosition;

                chkGridWeekClick = 0;
            }
        });


        // 격자 클릭 시
        grid_week.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int currentPosition, long id) {
                chkGridWeekClick = 1;


                // 날짜 칸을 클릭하고 격자 칸을 클릭했으면 날짜 칸 배경색을 화이트로 바꾸기
                if(chkGridWeekDayClick == 1){
                    grid_week_day.getChildAt(gridWeekDayPosition).setBackgroundColor(Color.WHITE);
                }

                // 전에 선택했던 격자 배경색 화이트로 변경
                grid_week.getChildAt(gridWeekPosition).setBackgroundColor(Color.WHITE);
                // 전에 선택된 격자에 해당되는 날짜의 배경색도 변경
                grid_week_day.getChildAt(gridWeekPosition % 7).setBackgroundColor(Color.WHITE);

                // 선택된 격자의 배경색 변경
                grid_week.getChildAt(currentPosition).setBackgroundColor(Color.CYAN);
                // 선택된 격자에 해당되는 날짜의 배경색도 변경
                grid_week_day.getChildAt(currentPosition % 7).setBackgroundColor(Color.CYAN);

                Toast.makeText(getContext(), "position : "+currentPosition, Toast.LENGTH_SHORT).show();

                gridWeekPosition = currentPosition;

                chkGridWeekDayClick = 0;
            }
        });

        return v;
    }


    // 인터페이스 추가 정의
    public interface OnSetYearMonthListener {
        public void onSetYearMonth(int year, int month);
    }
}