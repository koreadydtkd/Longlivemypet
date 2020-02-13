package com.mhj.longlivemypet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class CommunityFragmentWrite extends Fragment {
    MainActivity mainActivity;
    EditText editText_title, editText_content;
    ImageView imageView;
    String nick;
    Spinner spinner;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community_write, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        getUserEmail();

        rootView.findViewById(R.id.imageViewAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 클릭시 실행
            }
        });

        rootView.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityFragment communityFragment = new CommunityFragment();
                mainActivity.replaceFragment(communityFragment);
            }
        });

        rootView.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityItem communityItem = new CommunityItem();
                editText_title = rootView.findViewById(R.id.editText_title);
                editText_content = rootView.findViewById(R.id.editText_content);
                spinner = rootView.findViewById(R.id.spinner);
                if(editText_title.getText().toString().length() < 2){
                    Toast.makeText(getContext(), "제목이 너무 짧습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editText_content.getText().toString().length() < 2){
                    Toast.makeText(getContext(), "내용이 너무 짧습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                communityItem.setTitle(editText_title.getText().toString());
                communityItem.setClassification(spinner.getSelectedItem().toString());
                communityItem.setNick(nick);
                communityItem.setContent(editText_content.getText().toString());
                communityItem.setDate(System.currentTimeMillis());
                communityItem.setCommentCount(0);

                firestore.collection("Community").document().set(communityItem);
                Toast.makeText(getContext(), "작성완료되었습니다." ,Toast.LENGTH_SHORT).show();

                CommunityFragment communityFragment = new CommunityFragment();
                mainActivity.replaceFragment(communityFragment);
            }
        });

        return rootView;
    }

    private void getUserEmail(){
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

}
