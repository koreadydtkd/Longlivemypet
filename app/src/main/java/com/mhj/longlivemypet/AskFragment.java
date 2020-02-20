package com.mhj.longlivemypet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AskFragment extends Fragment {

    EditText editText_content, editText_title;
    Button button_Ask, button_cancel;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    MainActivity mainActivity;
    AskFragment askFragment;
    String email;
    String nick;

    Date today = new Date();
    SimpleDateFormat dateText = new SimpleDateFormat("yyyy-MM-dd HH시mm분ss초");
    String now = dateText.format(today);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        askFragment = this;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_ask, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();
        getUserNick();

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

    private void getUserNick(){
        String email = auth.getCurrentUser().getEmail();
        firestore.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    while (!task.isComplete());
                    nick = (String) task.getResult().get("nick");
                }
            }
        });
    }

    public void doAdd(){
        AskItem askItem = new AskItem();
        askItem.setEmail(email);
        askItem.setNick(nick);
        askItem.setQuestion(editText_content.getText().toString());
        askItem.setTitle(editText_title.getText().toString());
        askItem.setDate(now);

        firestore.collection("Questions").document().set(askItem);
        Toast.makeText(getContext(), "작성완료되었습니다." ,Toast.LENGTH_SHORT).show();
        mainActivity.replaceFragment(R.id.navigation_more);
    }

}
