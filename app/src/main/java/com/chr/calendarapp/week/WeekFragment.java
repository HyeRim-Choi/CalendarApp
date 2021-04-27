package com.chr.calendarapp.week;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.chr.calendarapp.R;

import java.util.Calendar;


public class WeekFragment extends Fragment {

    int year, month;

    public WeekFragment(int year, int month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_week, container, false);

        // ViewPager 추가
        ViewPager2 vpPager = v.findViewById(R.id.vpPager);
        FragmentStateAdapter adapter = new WeekPagerAdapter(getActivity(), year, month);
        vpPager.setAdapter(adapter);

        // 먼저 보여질 창(Integer Max값의 중간 값으로 세팅)
        vpPager.setCurrentItem(1000, false);


        return v;
    }

}