package com.mhj.longlivemypet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class PetCalendarFragment extends Fragment implements PetCalendarAdapter.PetCalendarItemDetailListener {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    MainActivity mainActivity;
    RecyclerView recyclerView;
    PetCalendarAdapter adapter;
    ViewGroup rootView;
    String email;
    TextView textViewPlz;
    FirestoreRecyclerOptions<PetCalendarItem> options;
    PetCalendarFragment petCalendarFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        petCalendarFragment = this;
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet_calendar, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();
        textViewPlz = rootView.findViewById(R.id.textViewPlz);
        Query query = firestore.collection("Calendar").whereEqualTo("email", email).orderBy("write_date", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<PetCalendarItem>().setQuery(query, PetCalendarItem.class).build();
        adapter = new PetCalendarAdapter(options,this, petCalendarFragment);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("PetCalendarFragment 500", "count:" + adapter.getItemCount());
                if( adapter.getItemCount() > 0) {
                    //일정 1개이상 존재시
                    textViewPlz.setVisibility(View.GONE);
                }else if (adapter.getItemCount() == 0){
                    //일정 리스트에 없을때
                    textViewPlz.setVisibility(View.VISIBLE);
                }
            }
        }, 500);


        //일정추가하러가기
        rootView.findViewById(R.id.button_AddCalender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetCalendarAddFragment petCalendarAddFragment = new PetCalendarAddFragment();
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
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("PetCalendarFragment 500", "count:" + adapter.getItemCount());
                if( adapter.getItemCount() > 0) {
                    //1개이상 존재시
                    textViewPlz.setVisibility(View.GONE);
                }else if (adapter.getItemCount() == 0){
                    //리스트에 없을때
                    textViewPlz.setVisibility(View.VISIBLE);
                }
            }
        }, 500);
    }//onResume


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


