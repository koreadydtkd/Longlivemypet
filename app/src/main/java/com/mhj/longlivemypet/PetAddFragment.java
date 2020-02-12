package com.mhj.longlivemypet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class PetAddFragment extends Fragment {

    MainActivity mainActivity;
    EditText editText_Name, editText_Breed, editText_Date, editText_Weight, editText_Sex;
    String email;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet_add, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();


        rootView.findViewById(R.id.button_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "펫 추가가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                PetFragment petFragment = new PetFragment();
                mainActivity.replaceFragment(petFragment);
            }
        });

        rootView.findViewById(R.id.button_Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetItem petItem = new PetItem();
                editText_Name = rootView.findViewById(R.id.editText_Name);
                editText_Sex = rootView.findViewById(R.id.editText_Sex);
                editText_Breed = rootView.findViewById(R.id.editText_Breed);
                editText_Date = rootView.findViewById(R.id.editText_Date);
                editText_Weight = rootView.findViewById(R.id.editText_Weight);

                petItem.setName(editText_Name.getText().toString());
                petItem.setSex(editText_Sex.getText().toString());
                petItem.setBreed(editText_Breed.getText().toString());
                petItem.setDate(editText_Date.getText().toString());
                petItem.setWeight(editText_Weight.getText().toString());
                petItem.setEmail(email);

                firestore.collection("Pet").document().set(petItem);
                Toast.makeText(getContext(), "펫 추가가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                PetFragment petFragment = new PetFragment();
                mainActivity.replaceFragment(petFragment);
            }
        });

        return rootView;
    }


}


