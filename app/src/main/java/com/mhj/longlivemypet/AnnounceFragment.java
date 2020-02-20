package com.mhj.longlivemypet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.TextView;

public class AnnounceFragment extends Fragment {
    ScrollView personal_information_content;
    TextView introduce_content;
    boolean personalCheck, introduceCheck = true;
    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_announce, container, false);
        personal_information_content = rootView.findViewById(R.id.personal_information_content);
        rootView.findViewById(R.id.personal_information).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(personalCheck){
                    personal_information_content.setVisibility(View.VISIBLE);
                    personalCheck = false;
                }else{
                    personal_information_content.setVisibility(View.GONE);
                    personalCheck = true;
                }
            }
        });

        introduce_content = rootView.findViewById(R.id.introduce_content);
        rootView.findViewById(R.id.introduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(introduceCheck){
                    introduce_content.setVisibility(View.VISIBLE);
                    introduceCheck = false;
                }else{
                    introduce_content.setVisibility(View.GONE);
                    introduceCheck = true;
                }

            }
        });

        return rootView;
    }

}
