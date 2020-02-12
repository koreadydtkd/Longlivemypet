package com.mhj.longlivemypet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PetFragment extends Fragment {
    RecyclerView recyclerView;
    PetAdapter adapter;
    ViewGroup rootView;
    MainActivity mainActivity;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    String email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();


        Query query = firestore.collection("Pet").whereEqualTo("email", email).orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PetItem> options = new FirestoreRecyclerOptions.Builder<PetItem>().setQuery(query, PetItem.class).build();
        adapter = new PetAdapter(options);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        rootView.findViewById(R.id.button_AddPet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetAddFragment petAddFragment = new PetAddFragment();
                mainActivity.replaceFragment(petAddFragment);
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


