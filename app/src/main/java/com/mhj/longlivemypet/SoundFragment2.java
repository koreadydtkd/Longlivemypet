package com.mhj.longlivemypet;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SoundFragment2 extends Fragment {

    MainActivity mainActivity;
    SoundPool sound;
    boolean soundLoad = false;
    int soundID,soundID2,soundID3,soundID4,soundID5,soundID6;
    int[] soundIDs = {R.raw.dog2, R.raw.tiger, R.raw.pig, R.raw.monkey, R.raw.horse, R.raw.frog};
    Button button_dog, button_tiger, button_pig, button_monkey, button_horse, button_frog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_sound_fragment2, container, false);

        button_dog= rootView.findViewById(R.id.button_dog);
        button_tiger = rootView.findViewById(R.id.button_tiger);
        button_pig = rootView.findViewById(R.id.button_pig);
        button_monkey =rootView.findViewById(R.id.button_monkey);
        button_horse = rootView.findViewById(R.id.button_horse);
        button_frog = rootView.findViewById(R.id.button_frog);

        sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundLoad = true; // 사운드 파일이 로딩됨.
            }
        });

        soundID = sound.load(getContext(), soundIDs[0], 1);
        soundID2 = sound.load(getContext(), soundIDs[1], 1);
        soundID3 = sound.load(getContext(), soundIDs[2], 1);
        soundID4 = sound.load(getContext(), soundIDs[3], 1);
        soundID5 = sound.load(getContext(), soundIDs[4], 1);
        soundID6 = sound.load(getContext(), soundIDs[5], 1);


        button_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (soundLoad == true) {
                    sound.play(soundID, 1, 1, 1, 1, 1.0f);
                    Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate( 1000 ); //1초 지속시간

                }
            }
        });

        button_tiger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundLoad == true) {
                    sound.play(soundID2, 1, 1, 1, 1, 1.0f);
                    Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate( 1000 ); //1초 지속시간

                }
            }
        });

        button_pig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundLoad == true) {
                    sound.play(soundID3, 1, 1, 1, 1, 1.0f);
                    Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate( 1000 ); //1초 지속시간

                }
            }
        });

        button_monkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundLoad == true) {
                    sound.play(soundID4, 1, 1, 1, 1, 1.0f);
                    Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate( 1000 ); //1초 지속시간

                }
            }
        });

        button_horse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundLoad == true) {
                    sound.play(soundID5, 1, 1, 1, 1, 1.0f);
                    Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate( 1000 ); //1초 지속시간

                }
            }
        });

        button_frog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundLoad == true) {
                    sound.play(soundID6, 1, 1, 1, 1, 1.0f);
                    Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate( 1000 ); //1초 지속시간

                }
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        //getActivity()함수 : 현재 액티비티 컨텍스트를 가져오는 함수.
        mainActivity = (MainActivity)getActivity();
    }

    //액티비티에서 떨어져 나갈때 한번 호출됨.
    @Override
    public void onDetach() {
        super.onDetach();

    }

}
