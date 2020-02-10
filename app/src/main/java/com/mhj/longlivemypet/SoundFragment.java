package com.mhj.longlivemypet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class SoundFragment extends Fragment {

    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_sound, container, false);




        Button button_dogsound = rootView.findViewById(R.id.button_dogsound);
        button_dogsound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundFragment2 soundFragment2 = new SoundFragment2();
                mainActivity.replaceFragment(soundFragment2);
            }
        });



        Button button_catsound = rootView.findViewById(R.id.button_catsound);
        button_catsound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundFragment3 soundFragment3 = new SoundFragment3();
                mainActivity.replaceFragment(soundFragment3);
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
