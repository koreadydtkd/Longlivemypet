package com.mhj.longlivemypet;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PetCalendarAdjustFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    MainActivity mainActivity;
    EditText editText_title,editText_body;
    String email,document;
    ViewGroup rootView;
    ProgressDialog progressDialog;
    TextView textViewWrite_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet_calendar_adjust, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();
        textViewWrite_date = rootView.findViewById(R.id.textViewWrite_date);
        editText_title = rootView.findViewById(R.id.editText_title);
        editText_body = rootView.findViewById(R.id.editText_body);
        setArgument();

        //일정수정취소버튼
        rootView.findViewById(R.id.button_CancelCalender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "일정 수정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                mainActivity.replaceFragment(R.layout.fragment_pet_calendar);
            }
        });

        //일정추가완료버튼
        rootView.findViewById(R.id.button_AdjustCalender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPetCalendarItem();
            }
        });
        return rootView;
    }//onCreateView

    //입력된 일정정보 PetCalendarItem 넘긴 후 전화면으로가기 + 수정된내용 파이어베이스 업데이트
    void AddPetCalendarItem(){
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
        petCalendarItem.setWrite_date(textViewWrite_date.getText().toString());
        petCalendarItem.setTitle(editText_title.getText().toString());
        petCalendarItem.setBody(editText_body.getText().toString());
        petCalendarItem.setEmail(email);

        firestore.collection("Calendar")
                .document(document).update("title",petCalendarItem.getTitle(), "body",petCalendarItem.getBody(), "write_date",petCalendarItem.getWrite_date());

        Toast.makeText(getContext(), "일정 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        mainActivity.replaceFragment(R.layout.fragment_pet_calendar);
        progressDialog.dismiss();
    }//PetCalendarAdjustFragment

    //수정전 최초 일정추가에서 입력받았던 정보 불러오기, 이화면 시작하자마자 실행
    public void setArgument() {
        if(getArguments() != null){
            document = getArguments().getString("document");
            textViewWrite_date.setText(getArguments().getString("write_date"));
            editText_title.setText(getArguments().getString("title"));
            editText_body.setText(getArguments().getString("body"));
        }
    }//setArgument

}//class PetAdjustFragment