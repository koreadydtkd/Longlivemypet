package com.mhj.longlivemypet;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mhj.longlivemypet.decorators.EventDecorator;
import com.mhj.longlivemypet.decorators.OneDayDecorator;
import com.mhj.longlivemypet.decorators.SaturdayDecorator;
import com.mhj.longlivemypet.decorators.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;


public class PetCalendarFragment extends Fragment implements PetCalendarAdapter.PetCalendarItemDetailListener {
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
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
    MaterialCalendarView materialCalendarView;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date date = new Date();
        time = simpleDateFormat.format(date);

    }

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
        materialCalendarView =rootView.findViewById(R.id.calendarView);

        try {
            materialCalendarView.setSelectedDate(CalendarDay.today());
        }catch (Exception e) { }

        textViewwhenDate.setText(time); //텍스트뷰에 현재날짜 띄우기
        setRecyclerView(time); //함수따로만들어놓음, 리싸이클러뷰 선택한날짜로 셋팅해서 띄우기

        //캘린더 시작년도~ 마지막년도 셋팅
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //캘린더에 데코레이터 클래스 셋팅
        materialCalendarView.addDecorators(new SundayDecorator(),new SaturdayDecorator(), oneDayDecorator);

        final ArrayList<String> arrayList = new ArrayList<String>();

        firestore.collection("Calendar").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    PetCalendarItem item = snapshot.toObject(PetCalendarItem.class);
                    arrayList.add(item.getWrite_date());
                    Log.d("TEST@@@@@@@", item.getWrite_date());
                }
                arrayList.add("2000/1/1");
            }
        });

        new ApiSimulator(arrayList).executeOnExecutor(Executors.newSingleThreadExecutor());

        //날짜 선택시 텍스트뷰 선택날짜로 바꿔주는 이벤트
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                time= Year + "/" + Month + "/" + Day;
                materialCalendarView.clearSelection();
                textViewwhenDate.setText(time); // 선택한 날짜로 설정
                adapter.stopListening();
                setRecyclerView(time);
                adapter.startListening();
                textViewPlz.setVisibility(View.GONE);
            }
        });

        //일정추가하러가기버튼
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

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        ArrayList Time_Result;

        ApiSimulator(ArrayList Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.clear();

            ArrayList<CalendarDay> dates = new ArrayList<>();

            for (int i = 0; i < Time_Result.size(); i++) {
                CalendarDay calendarDay = CalendarDay.from(calendar);
                String time = (String) Time_Result.get(i);

                String[] tt = time.split("/");
                int year = Integer.parseInt(tt[0]);
                int month = Integer.parseInt(tt[1]);
                int day = Integer.parseInt(tt[2]);

                dates.add(calendarDay);
                calendar.set(year,month-1,day);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            MainActivity mainActivity = (MainActivity)getActivity();
            materialCalendarView.addDecorator(new EventDecorator(Color.BLACK, calendarDays, mainActivity));
        }
    }



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


