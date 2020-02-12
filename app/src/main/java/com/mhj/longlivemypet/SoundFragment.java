package com.mhj.longlivemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SoundFragment extends Fragment {
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_sound, container, false);
        mainActivity = (MainActivity)getActivity();

        rootView.findViewById(R.id.button_dogsound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundDogFragment soundDogFragment = new SoundDogFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, soundDogFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        rootView.findViewById(R.id.button_catsound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundCatFragment soundCatFragment = new SoundCatFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, soundCatFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

}
