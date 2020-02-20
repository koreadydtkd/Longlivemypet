package com.mhj.longlivemypet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class PetCalendarAddFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    MainActivity mainActivity;
    EditText editText_write_date, editText_title,editText_body;
    String email;
    ViewGroup rootView;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet_calendar_add, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();
        editText_write_date = rootView.findViewById(R.id.editText_write_date);
        editText_title = rootView.findViewById(R.id.editText_title);
        editText_body = rootView.findViewById(R.id.editText_body);



        //일정추가취소버튼
        rootView.findViewById(R.id.button_CancelCalender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "일정 추가가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                mainActivity.replaceFragment(R.layout.fragment_pet_calendar);
            }
        });

        //일정추가완료버튼
        rootView.findViewById(R.id.button_AddCalender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPetCalendarItem();
            }
        });
        return rootView;
    }//onCreateView


    //입력된펫정보 PetItem으로 넘긴 후 전화면으로가기
    void AddPetCalendarItem(){

        if(editText_write_date.length()<8){
            Toast.makeText(mainActivity, "날짜를 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText_title.length()<1){
            Toast.makeText(mainActivity, "제목이 너무 짧습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText_body.length()<1){
            Toast.makeText(mainActivity, "내용이 너무 짧습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        progressDialog.show();
        progressDialog.setCancelable(false);
        PetCalendarItem petCalendarItem = new PetCalendarItem();
        petCalendarItem.setWrite_date(editText_write_date.getText().toString());
        petCalendarItem.setTitle(editText_title.getText().toString());
        petCalendarItem.setBody(editText_body.getText().toString());
        petCalendarItem.setEmail(email);
        firestore.collection("Calendar").document().set(petCalendarItem);
        Toast.makeText(getContext(), "일정 추가가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        mainActivity.replaceFragment(R.layout.fragment_pet_calendar);
        progressDialog.dismiss();
    }//AddPetCalendarItem


}//class PetAddFragment
