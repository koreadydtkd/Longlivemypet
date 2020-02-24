package com.mhj.longlivemypet;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SoundFragment1 extends Fragment {
    MainActivity mainActivity;
    MediaPlayer mediaPlayer = new MediaPlayer();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_sound_fragment1, container, false);
        mainActivity = (MainActivity)getActivity();

        //비행기소리 재생(Play)
        Button buttonPlay = rootView.findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.airplane);
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


        //초인종소리 재생(Play)
        Button buttonPlay2 = rootView.findViewById(R.id.buttonPlay2);
        buttonPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.bell);
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


        //새소리 재생(Play)
        Button buttonPlay3 = rootView.findViewById(R.id.buttonPlay3);
        buttonPlay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.bird);
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

        //에어혼소리 재생(Play)
        Button buttonPlay4 = rootView.findViewById(R.id.buttonPlay4);
        buttonPlay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.horn);
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

        //노크소리 재생(Play)
        Button buttonPlay5 = rootView.findViewById(R.id.buttonPlay5);
        buttonPlay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.knocking);
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

        //천둥소리 재생(Play)
        Button buttonPlay6 = rootView.findViewById(R.id.buttonPlay6);
        buttonPlay6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMediaPlayer();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.thunder);
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
