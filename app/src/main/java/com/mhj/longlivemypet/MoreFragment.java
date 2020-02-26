package com.mhj.longlivemypet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MoreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_more, container, false);

        rootView.findViewById(R.id.cardViewMy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragment myFragment = new MyFragment();
                changeFragment(myFragment);
            }
        });

        rootView.findViewById(R.id.cardViewAnnounce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnounceFragment announceFragment = new AnnounceFragment();
                changeFragment(announceFragment);
            }
        });

        rootView.findViewById(R.id.cardViewCare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CareFragment careFragment = new CareFragment();
                changeFragment(careFragment);
            }
        });

        rootView.findViewById(R.id.cardViewSound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundFragment_Viewpager soundFragment_viewpager = new SoundFragment_Viewpager();
                changeFragment(soundFragment_viewpager);
            }
        });

        rootView.findViewById(R.id.cardViewAsk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskFragment askFragment = new AskFragment();
                changeFragment(askFragment);
            }
        });

        return rootView;
    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
