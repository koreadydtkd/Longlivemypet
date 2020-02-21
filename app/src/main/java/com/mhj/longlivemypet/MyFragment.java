package com.mhj.longlivemypet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    TextView textView_email, textView_name, textView_sex, textView_birth;
    EditText editText_nick, editText_phone;
    String email, name, nick, sex, birth, phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        textView_email = rootView.findViewById(R.id.textView_email);
        textView_name = rootView.findViewById(R.id.textView_name);
        textView_sex = rootView.findViewById(R.id.textView_sex);
        textView_birth = rootView.findViewById(R.id.textView_birth);
        editText_nick = rootView.findViewById(R.id.editText_nick);
        editText_phone = rootView.findViewById(R.id.editText_phone);

        getUserNick();
        Log.d("TEST", email + "\n" + name + "\n" + nick + "\n" + sex + "\n" + birth + "\n" + phone);

        rootView.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        rootView.findViewById(R.id.button_NickEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rootView.findViewById(R.id.button_PhoneEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

    private void getUserNick(){
        email = auth.getCurrentUser().getEmail();
        firestore.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    while (!task.isComplete());
                    name = (String) task.getResult().get("name");
                    nick = (String) task.getResult().get("nick");
                    sex = task.getResult().get("sex").toString();
                    birth = (String) task.getResult().get("birth");
                    phone = (String) task.getResult().get("phone");

                }
            }
        });
    }

}
