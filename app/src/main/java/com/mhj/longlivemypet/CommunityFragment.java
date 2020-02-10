package com.mhj.longlivemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CommunityFragment extends Fragment {
    RecyclerView recyclerView;
    CommunityAdapter adapter;
    ViewGroup rootView;
    MainActivity mainActivity;
    FirebaseFirestore firestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community, container, false);
        mainActivity = (MainActivity) getActivity();

        firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("Community").whereEqualTo("cl", "공지사항").orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CommunityItem> options = new FirestoreRecyclerOptions.Builder<CommunityItem>().setQuery(query, CommunityItem.class).build();
        adapter = new CommunityAdapter(options);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        rootView.findViewById(R.id.button_Write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityFragmentWrite communityFragmentWrite= new CommunityFragmentWrite();
                mainActivity.replaceFragment(communityFragmentWrite);
            }
        });
        return rootView;
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
}
