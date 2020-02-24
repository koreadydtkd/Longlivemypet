package com.mhj.longlivemypet;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class SoundFragment2 extends Fragment {
    MainActivity mainActivity;
    MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_sound_fragment2, container, false);
        mainActivity = (MainActivity)getActivity();

        killMediaPlayer();
        //청소기소리 재생(Play)
        Button buttonPlay7 = rootView.findViewById(R.id.buttonPlay7);
        buttonPlay7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.cleaner);
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();//재생시작!

                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
                Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000); //1초 지속시간
                Toast.makeText(getContext(), "소리를 최대한 키워주시고 \n 펫의 반응을 지켜보세요!", Toast.LENGTH_SHORT).show();
            }
        });


        //하모니카소리 재생(Play)
        Button buttonPlay8 = rootView.findViewById(R.id.buttonPlay8);
        buttonPlay8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.harmonica);
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();//재생시작!

                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
                Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000); //1초 지속시간
                Toast.makeText(getContext(), "소리를 최대한 키워주시고 \n 펫의 반응을 지켜보세요!", Toast.LENGTH_SHORT).show();
            }
        });


        //말울음소리 재생(Play)
        Button buttonPlay9 = rootView.findViewById(R.id.buttonPlay9);
        buttonPlay9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.horse);
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();//재생시작!

                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
                Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000); //1초 지속시간
                Toast.makeText(getContext(), "소리를 최대한 키워주시고 \n 펫의 반응을 지켜보세요!", Toast.LENGTH_SHORT).show();
            }
        });


        //원숭이울음소리 재생(Play)
        Button buttonPlay10 = rootView.findViewById(R.id.buttonPlay10);
        buttonPlay10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.monkey);
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();//재생시작!

                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
                Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000); //1초 지속시간
                Toast.makeText(getContext(), "소리를 최대한 키워주시고 \n 펫의 반응을 지켜보세요!", Toast.LENGTH_SHORT).show();
            }
        });


        //장난감소리 재생(Play)
        Button buttonPlay11 = rootView.findViewById(R.id.buttonPlay11);
        buttonPlay11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.toy);
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();//재생시작!

                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
                Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000); //1초 지속시간
                Toast.makeText(getContext(), "소리를 최대한 키워주시고 \n 펫의 반응을 지켜보세요!", Toast.LENGTH_SHORT).show();
            }
        });


        //늑대울음소리 재생(Play)
        Button buttonPlay12 = rootView.findViewById(R.id.buttonPlay12);
        buttonPlay12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.wolf);
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();//재생시작!

                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
                Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000); //1초 지속시간
                Toast.makeText(getContext(), "소리를 최대한 키워주시고 \n 펫의 반응을 지켜보세요!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }






    void killMediaPlayer(){
        if (mediaPlayer != null){
            try{
                mediaPlayer.release();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}