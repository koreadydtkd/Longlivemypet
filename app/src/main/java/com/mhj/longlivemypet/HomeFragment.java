package com.mhj.longlivemypet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    private FirebaseAuth auth;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        auth = FirebaseAuth.getInstance();

        //로그아웃 임시
        rootView.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        return rootView;
    }

}
