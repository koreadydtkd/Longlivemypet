package com.mhj.longlivemypet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;

public class SoundFragment_Viewpager extends Fragment {

    ViewGroup rootView;
    MainActivity mainActivity;
    ViewPager pager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sound_fragment__viewpager, container, false);

        pager= rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2); //페이지갯수

        MypagerAdapter adapter = new MypagerAdapter(getChildFragmentManager());
        SoundFragment1 soundFragment1 = new SoundFragment1();
        SoundFragment2 soundFragment2 = new SoundFragment2();

        adapter.addItem(soundFragment1);
        adapter.addItem(soundFragment2);

        pager.setAdapter(adapter);
        final PageIndicatorView pageIndicatorView =rootView.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(1);
        pageIndicatorView.setSelected(1);
        pageIndicatorView.setAnimationType(AnimationType.COLOR);


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }


    class MypagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();
        public MypagerAdapter(FragmentManager fm){
            super(fm);
        }
        public void addItem(Fragment item){
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }


    }}
