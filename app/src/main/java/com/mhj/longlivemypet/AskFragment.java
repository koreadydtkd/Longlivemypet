package com.mhj.longlivemypet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AskFragment extends Fragment {
    EditText editText_content, editText_title;
    Button button_Ask, button_cancel;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    MainActivity mainActivity;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_ask, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();

        editText_title = rootView.findViewById(R.id.editText_title);
        editText_content = rootView.findViewById(R.id.editText_content);
        button_Ask = rootView.findViewById(R.id.button_Ask);
        button_cancel = rootView.findViewById(R.id.button_cancel);

        button_Ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.navigation_more);
            }
        });

        return rootView;
    }

    public void doAdd(){
        AskItem askItem = new AskItem(email, editText_content.getText().toString(), editText_title.getText().toString(), System.currentTimeMillis());
        firestore.collection("Questions").document().set(askItem);
        Toast.makeText(getContext(), "작성완료되었습니다." ,Toast.LENGTH_SHORT).show();
        mainActivity.replaceFragment(R.id.navigation_more);
    }

}
