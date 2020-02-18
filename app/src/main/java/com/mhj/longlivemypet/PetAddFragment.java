package com.mhj.longlivemypet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class PetAddFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    MainActivity mainActivity;
    ImageView imageViewPet;
    EditText editText_Name, editText_Breed, editText_Date, editText_Weight, editText_Sex, editText_Memo;
    String email,imageURL;
    ViewGroup rootView;
    final int PICK_IMAGE_REQUEST = 0;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet_add, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        email = auth.getCurrentUser().getEmail();
        editText_Name = rootView.findViewById(R.id.editText_Name);
        editText_Sex = rootView.findViewById(R.id.editText_Sex);
        editText_Breed = rootView.findViewById(R.id.editText_Breed);
        editText_Date = rootView.findViewById(R.id.editText_Date);
        editText_Weight = rootView.findViewById(R.id.editText_Weight);
        editText_Memo = rootView.findViewById(R.id.editText_Memo);
        imageViewPet = rootView.findViewById(R.id.imageViewPet);

        //갤러리에서이미지찾기버튼
        rootView.findViewById(R.id.button_Image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        //펫추가취소버튼
        rootView.findViewById(R.id.button_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "펫 추가가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                mainActivity.replaceFragment(R.id.navigation_pet);
            }
        });

        //펫추가완료버튼
        rootView.findViewById(R.id.button_Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPet_Complete();
            }
        });
        return rootView;
    }//onCreateView



    //갤러리에서 찾은사진 이미지뷰에 띄우기
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                imageViewPet.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//onActivityResult


    //입력된펫정보 PetItem으로 넘긴 후 전화면으로가기
    void AddPetItem(){
        PetItem petItem = new PetItem();
        petItem.setName(editText_Name.getText().toString());
        petItem.setSex(editText_Sex.getText().toString());
        petItem.setBreed(editText_Breed.getText().toString());
        petItem.setDate(editText_Date.getText().toString());
        petItem.setWeight(editText_Weight.getText().toString());
        petItem.setMemo(editText_Memo.getText().toString());
        petItem.setEmail(email);
        petItem.setImageURL(imageURL);
        firestore.collection("Pet").document().set(petItem);
        String petname = petItem.getName();
        Toast.makeText(getContext(), petname + "펫 추가가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        mainActivity.replaceFragment(R.id.navigation_pet);
    }//AddPetItem


    //펫등록(사진+정보) 최종완료
    void AddPet_Complete(){
        imageViewPet.setDrawingCacheEnabled(true);
        imageViewPet.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewPet.getDrawable()).getBitmap();

        if (bitmap !=null){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려주세요.");
            progressDialog.show();
            progressDialog.setCancelable(false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // 100은 100% 품질
            byte[] data = baos.toByteArray();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            Date date = new Date();
            long timestamp = date.getTime();
            String imageFilename = "image" + timestamp + ".jpg";
            final StorageReference imageRef = storageRef.child(imageFilename);
            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpg").build();
            UploadTask uploadTask = imageRef.putBytes( data, metadata );
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uri.isComplete());
                    imageURL = uri.getResult().toString();
                    AddPetItem();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "업로드에 실패했습니다.\n잠시후 다시 시도해주세요" ,Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            imageURL = null;
            AddPetItem();
            progressDialog.dismiss();
        }
    }//AddPet_Complete

}//class PetAddFragment