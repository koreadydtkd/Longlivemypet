package com.mhj.longlivemypet;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PetFragment extends Fragment implements PetAdapter.PetItemDetailListener{

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    MainActivity mainActivity;
    ImageView imageViewPlz;
    RecyclerView recyclerView;
    PetAdapter adapter;
    ViewGroup rootView;
    String email;
    TextView textViewPlz;
    FirestoreRecyclerOptions<PetItem> options;
    PetFragment petFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        petFragment = this;
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();
        textViewPlz = rootView.findViewById(R.id.textViewPlz);
        imageViewPlz = rootView.findViewById(R.id.imageViewPlz);
        Query query = firestore.collection("Pet").whereEqualTo("email", email).orderBy("breed", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<PetItem>().setQuery(query, PetItem.class).build();
        adapter = new PetAdapter(options,this, petFragment);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("PetFragment 500", "count:" + adapter.getItemCount());
                if( adapter.getItemCount() > 0) {
                    //추가펫 1개이상 존재시
                    textViewPlz.setVisibility(View.GONE);
                    imageViewPlz.setVisibility(View.GONE);
                }else if (adapter.getItemCount() == 0){
                    //추가펫 리스트에 없을때
                    textViewPlz.setVisibility(View.VISIBLE);
                    imageViewPlz.setVisibility(View.VISIBLE);
                }
            }
        }, 500);

        //펫추가하러가기
        rootView.findViewById(R.id.button_AddPet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetAddFragment petAddFragment = new PetAddFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, petAddFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return rootView;
    }//onCreateView

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("PetFragment 500", "count:" + adapter.getItemCount());
                if( adapter.getItemCount() > 0) {
                    //1개이상 존재시
                    textViewPlz.setVisibility(View.GONE);
                    imageViewPlz.setVisibility(View.GONE);
                }else if (adapter.getItemCount() == 0){
                    //리스트에 없을때
                    textViewPlz.setVisibility(View.VISIBLE);
                    imageViewPlz.setVisibility(View.VISIBLE);
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
    public void petitemDetail(String document, String name, String sex, String breed, String date, String weight, String memo, String imageURL) {
        PetAdjustFragment petAdjustFragment = new PetAdjustFragment();
        Bundle bundle = new Bundle();
        bundle.putString("document", document);
        bundle.putString("name", name);
        bundle.putString("sex", sex);
        bundle.putString("breed", breed);
        bundle.putString("date", date);
        bundle.putString("weight", weight);
        bundle.putString("memo", memo);
        bundle.putString("imageURL", imageURL);
        petAdjustFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, petAdjustFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }//petitemDetail

}//class PetFragment


