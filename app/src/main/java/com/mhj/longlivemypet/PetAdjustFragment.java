package com.mhj.longlivemypet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class PetAdjustFragment extends Fragment {

    MainActivity mainActivity;
    ViewGroup rootView;
    EditText editText_Name, editText_Breed, editText_Date, editText_Weight, editText_Sex, editText_Memo;
    String email,document;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet_adjust, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();
        setArgument();


        rootView.findViewById(R.id.button_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "펫 추가가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                //PetFragment petFragment = new PetFragment();
                mainActivity.replaceFragment(R.id.navigation_pet);
            }
        });

        rootView.findViewById(R.id.button_Adjust).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetItem petItem = new PetItem();
                editText_Name = rootView.findViewById(R.id.editText_Name);
                editText_Sex = rootView.findViewById(R.id.editText_Sex);
                editText_Breed = rootView.findViewById(R.id.editText_Breed);
                editText_Date = rootView.findViewById(R.id.editText_Date);
                editText_Weight = rootView.findViewById(R.id.editText_Weight);
                editText_Memo = rootView.findViewById((R.id.editText_Memo));

                petItem.setName(editText_Name.getText().toString());
                petItem.setSex(editText_Sex.getText().toString());
                petItem.setBreed(editText_Breed.getText().toString());
                petItem.setDate(editText_Date.getText().toString());
                petItem.setWeight(editText_Weight.getText().toString());
                petItem.setMemo(editText_Memo.getText().toString());
                petItem.setEmail(email);


                firestore.collection("Pet").document(document).update("name",petItem.getName());
                firestore.collection("Pet").document(document).update("sex",petItem.getSex());
                firestore.collection("Pet").document(document).update("breed",petItem.getBreed());
                firestore.collection("Pet").document(document).update("date",petItem.getDate());
                firestore.collection("Pet").document(document).update("weight",petItem.getWeight());
                firestore.collection("Pet").document(document).update("memo",petItem.getMemo());


                String petname = petItem.getName();
                Toast.makeText(getContext(), petname + "펫이 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
               // PetFragment petFragment = new PetFragment();
                mainActivity.replaceFragment(R.id.navigation_pet);
            }
        });

        return rootView;
    }

    public void setArgument() {
        editText_Name = rootView.findViewById(R.id.editText_Name);
        editText_Sex = rootView.findViewById(R.id.editText_Sex);
        editText_Breed = rootView.findViewById(R.id.editText_Breed);
        editText_Date= rootView.findViewById(R.id.editText_Date);
        editText_Weight = rootView.findViewById(R.id.editText_Weight);
        editText_Memo = rootView.findViewById(R.id.editText_Memo);

        if(getArguments() != null){

            document = getArguments().getString("document");
            editText_Name.setText(getArguments().getString("name"));
            editText_Sex.setText(getArguments().getString("sex"));
            editText_Breed.setText(getArguments().getString("breed"));
            editText_Date.setText(getArguments().getString("date"));
            editText_Weight.setText(getArguments().getString("weight"));
            editText_Memo.setText(getArguments().getString("memo"));
        }
    }
}
