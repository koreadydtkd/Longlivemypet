package com.mhj.longlivemypet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CareFragment extends Fragment {
    TextView card1text, card2text, card3text;
    boolean isText1, isText2, isText3 = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_care, container, false);
        card1text = rootView.findViewById(R.id.card1text);
        card2text = rootView.findViewById(R.id.card2text);
        card3text = rootView.findViewById(R.id.card3text);

        rootView.findViewById(R.id.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isText1){
                    card1text.setVisibility(View.VISIBLE);
                    isText1 = false;
                }else{
                    card1text.setVisibility(View.GONE);
                    isText1 = true;
                }

            }
        });

        rootView.findViewById(R.id.cardView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isText2){
                    card2text.setVisibility(View.VISIBLE);
                    isText2 = false;
                }else{
                    card2text.setVisibility(View.GONE);
                    isText2 = true;
                }
            }
        });

        rootView.findViewById(R.id.cardView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isText3){
                    card3text.setVisibility(View.VISIBLE);
                    isText3 = false;
                }else{
                    card3text.setVisibility(View.GONE);
                    isText3 = true;
                }
            }
        });

        return rootView;
    }

}
