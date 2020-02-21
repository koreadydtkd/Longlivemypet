package com.mhj.longlivemypet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PetCalendarFragment extends Fragment implements PetCalendarAdapter.PetCalendarItemDetailListener {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    MainActivity mainActivity;
    RecyclerView recyclerView;
    PetCalendarAdapter adapter;
    ViewGroup rootView;
    String email,time;
    TextView textViewPlz,textViewwhenDate;
    FirestoreRecyclerOptions<PetCalendarItem> options;
    PetCalendarFragment petCalendarFragment;
    CalendarView calendarView;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        petCalendarFragment = this;
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet_calendar, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();
        textViewwhenDate = rootView.findViewById(R.id.textViewwhenDate);
        textViewPlz = rootView.findViewById(R.id.textViewPlz);
        calendarView =rootView.findViewById(R.id.calendarView);

        Date date = new Date();
        time = simpleDateFormat.format(date);
        textViewwhenDate.setText(time); //텍스트뷰에 현재날짜 띄우기

        setRecyclerView(time);

        //날짜 선택 이벤트
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                time = year + "/" +(month + 1) +"/" +dayOfMonth;
                textViewwhenDate.setText(time); // 선택한 날짜로 설정

                adapter.stopListening();
                setRecyclerView(time);
                adapter.startListening();
                textViewPlz.setVisibility(View.GONE);
            }
        });


        //일정추가하러가기
        rootView.findViewById(R.id.button_AddCalender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetCalendarAddFragment petCalendarAddFragment = new PetCalendarAddFragment();
                Bundle bundle = new Bundle();
                bundle.putString("1",textViewwhenDate.getText().toString());
                petCalendarAddFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, petCalendarAddFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return rootView;
    }//onCreateView




    @Override
    public void onResume() {
        super.onResume();

        Query query = firestore.collection("Calendar").whereEqualTo("email", email).whereEqualTo("write_date", time);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() == 0) {
                    Log.e("테스트", "없음");
                    textViewPlz.setVisibility(View.VISIBLE);
                } else {
                    Log.e("테스트", "있음");
                    textViewPlz.setVisibility(View.GONE);
                }
            }
        });
    }//onResume

    void setRecyclerView(String time){
        Query query = firestore.collection("Calendar").whereEqualTo("email", email).whereEqualTo("write_date", time);
        options = new FirestoreRecyclerOptions.Builder<PetCalendarItem>().setQuery(query, PetCalendarItem.class).build();
        adapter = new PetCalendarAdapter(options,this, petCalendarFragment);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size() == 0){
                    Log.e("테스트", "없음");
                    textViewPlz.setVisibility(View.VISIBLE);
                }
                else{
                    Log.e("테스트", "있음");
                    textViewPlz.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }//onStart


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }//onStop


    @Override
    public void petCalendaritemDetail(String document, String title, String body, String write_date) {
        PetCalendarAdjustFragment petCalendarAdjustFragment = new PetCalendarAdjustFragment();
        Bundle bundle = new Bundle();
        bundle.putString("document", document);
        bundle.putString("title", title);
        bundle.putString("body", body);
        bundle.putString("write_date", write_date);
        petCalendarAdjustFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, petCalendarAdjustFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }//petCalenderitemDetail


}//class PetCalendarFragment


