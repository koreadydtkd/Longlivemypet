package com.mhj.longlivemypet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    TextView textView_email, textView_name, textView_sex, textView_birth;
    EditText editText_nick, editText_phone;
    RecyclerView recyclerView;
    MyAdapter adapter;
    private String email;

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
        recyclerView = rootView.findViewById(R.id.recyclerView);

        getUserInfo();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        setQuestions();

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
                changeNick();
            }
        });

        rootView.findViewById(R.id.button_PhoneEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Users").document(email).update("phone", editText_phone.getText().toString());
                Toast.makeText(getContext(), "핸드폰번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        rootView.findViewById(R.id.button_Withdrawal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Withdrawal();
            }
        });

        return rootView;
    }

    private void Withdrawal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("회원탈퇴");
        builder.setMessage("작성된 게시글은 삭제되지 않습니다.\n탈퇴하시겠습니까?");
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.getCurrentUser().delete();
                MainActivity activity = (MainActivity) getActivity();
                activity.finishAffinity();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setQuestions() {
        firestore.collection("Questions").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    AskItem askItem = snapshot.toObject(AskItem.class);
                    adapter.addItem(new AskItem(askItem.getTitle(), askItem.getQuestion(), askItem.getDate()));
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void changeNick(){
        firestore.collection("Users").whereEqualTo("nick", editText_nick.getText().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                            UserDTO users = snapshot.toObject(UserDTO.class);
                            if(users.getNick().equals(editText_nick.getText().toString())){
                                Toast.makeText(getContext(), "중복된 별명입니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        firestore.collection("Users").document(email).update("nick", editText_nick.getText().toString());
                        Toast.makeText(getContext(), "별명이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserInfo(){
        email = auth.getCurrentUser().getEmail();
        firestore.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    while (!task.isComplete());
                    textView_email.setText(email);
                    textView_name.setText(task.getResult().get("name").toString());
                    editText_nick.setText(task.getResult().get("nick").toString());
                    if(task.getResult().get("sex").toString().equals("false")){
                        textView_sex.setText("남자");
                    }else{
                        textView_sex.setText("여자");
                    }
                    textView_birth.setText(task.getResult().get("birth").toString());
                    editText_phone.setText(task.getResult().get("phone").toString());

                }
            }
        });
    }

}
