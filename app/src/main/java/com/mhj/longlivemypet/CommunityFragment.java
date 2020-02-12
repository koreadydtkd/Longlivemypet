package com.mhj.longlivemypet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CommunityFragment extends Fragment implements CommunityAdapter.itemDetailListener{
    RecyclerView recyclerView;
    CommunityAdapter adapter;
    ViewGroup rootView;
    MainActivity mainActivity;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    String nick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community, container, false);
        mainActivity = (MainActivity) getActivity();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        getUserNick();

        Query query = firestore.collection("Community").orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CommunityItem> options = new FirestoreRecyclerOptions.Builder<CommunityItem>().setQuery(query, CommunityItem.class).build();
        adapter = new CommunityAdapter(options, this);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        rootView.findViewById(R.id.textView_hospital).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.stopListening();
                changeList(firestore.collection("Community").whereEqualTo("classification", "병원추천").orderBy("date", Query.Direction.DESCENDING));
                adapter.startListening();
            }
        });

        rootView.findViewById(R.id.textView_cafe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.stopListening();
                changeList(firestore.collection("Community").whereEqualTo("classification", "카페추천").orderBy("date", Query.Direction.DESCENDING));
                adapter.startListening();
            }
        });

        rootView.findViewById(R.id.textView_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.stopListening();
                changeList(firestore.collection("Community").orderBy("date", Query.Direction.DESCENDING));
                adapter.startListening();
            }
        });

        rootView.findViewById(R.id.textView_food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.stopListening();
                changeList(firestore.collection("Community").whereEqualTo("classification", "맛집추천").orderBy("date", Query.Direction.DESCENDING));
                adapter.startListening();
            }
        });

        rootView.findViewById(R.id.textView_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.stopListening();
                changeList(firestore.collection("Community").whereEqualTo("classification", "궁금해요").orderBy("date", Query.Direction.DESCENDING));
                adapter.startListening();
            }
        });

        rootView.findViewById(R.id.button_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityFragmentWrite communityFragmentWrite = new CommunityFragmentWrite();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, communityFragmentWrite);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        rootView.findViewById(R.id.button_writeList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeList(firestore.collection("Community").whereEqualTo("nick", nick).orderBy("date", Query.Direction.DESCENDING));
            }
        });

        return rootView;
    }

    private void changeList(Query query) {
        adapter.stopListening();
        FirestoreRecyclerOptions<CommunityItem> options = new FirestoreRecyclerOptions.Builder<CommunityItem>().setQuery(query, CommunityItem.class).build();
        adapter = new CommunityAdapter(options, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void itemDetail(String document, String classification, String title, String userNick, String content, String date) {
        CommunityDetailFragment detailFragment = new CommunityDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("document", document);
        bundle.putString("classification", classification);
        bundle.putString("title", title);
        bundle.putString("userNick", userNick);
        bundle.putString("content", content);
        bundle.putString("date", date);
        detailFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
