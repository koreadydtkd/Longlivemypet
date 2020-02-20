package com.mhj.longlivemypet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class PetAdjustFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    MainActivity mainActivity;
    ImageView imageViewPet;
    EditText editText_Name, editText_Breed, editText_Date, editText_Weight, editText_Sex, editText_Memo;
    String email,imageURL,document;
    ViewGroup rootView;
    final int PICK_IMAGE_REQUEST = 0;
    ProgressDialog progressDialog;
    Bitmap bitmap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pet_adjust, container, false);
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
        //수정전 최초펫추가에서 입력받았던 정보 불러오기
        setArgument();

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

        //펫수정취소버튼
        rootView.findViewById(R.id.button_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "펫 수정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                mainActivity.replaceFragment(R.id.navigation_pet);
            }
        });

        //펫수정완료버튼
        rootView.findViewById(R.id.button_Adjust).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjustPet_Complete();
            }
        });

        //이미뷰돌리기
        rootView.findViewById(R.id.button_rotateImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap != null){
                    imageViewPet.setImageBitmap(getRotatedBitmap(90));
                }
            }
        });

        return rootView;
    }//onCreateView


    //이미지뷰 돌리기
    private Bitmap getRotatedBitmap(int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap != rotateBitmap) {
                bitmap.recycle();
                bitmap = rotateBitmap;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }//getRotatedBitmap


    //갤러리에서 찾은사진 이미지뷰에 띄우기
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                imageViewPet.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//onActivityResult



    //입력된펫정보 PetItem으로 넘긴 후 전화면으로가기 + 수정된내용 파이어베이스 업데이트
    void AdjustPetItem(){
        PetItem petItem = new PetItem();
        petItem.setName(editText_Name.getText().toString());
        petItem.setSex(editText_Sex.getText().toString());
        petItem.setDate(editText_Date.getText().toString());
        petItem.setWeight(editText_Weight.getText().toString());
        petItem.setBreed(editText_Breed.getText().toString());
        petItem.setMemo(editText_Memo.getText().toString());
        petItem.setEmail(email);
        petItem.setImageURL(imageURL);
        firestore.collection("Pet").document(document).update("name",petItem.getName());
        firestore.collection("Pet").document(document).update("sex",petItem.getSex());
        firestore.collection("Pet").document(document).update("breed",petItem.getBreed());
        firestore.collection("Pet").document(document).update("date",petItem.getDate());
        firestore.collection("Pet").document(document).update("weight",petItem.getWeight());
        firestore.collection("Pet").document(document).update("memo",petItem.getMemo());
        firestore.collection("Pet").document(document).update("imageURL",petItem.getImageURL());
        String petname = petItem.getName();
        Toast.makeText(getContext(), petname + "펫 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        mainActivity.replaceFragment(R.id.navigation_pet);

    }//AdjustPetItem


    //펫수정(사진+정보) 최종완료
    void AdjustPet_Complete(){
        imageViewPet.setDrawingCacheEnabled(true);
        imageViewPet.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewPet.getDrawable()).getBitmap();
        if(editText_Name.length()<1){
            Toast.makeText(mainActivity, "이름을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText_Sex.length()<2){
            Toast.makeText(mainActivity, "성별을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText_Date.length()<6){
            Toast.makeText(mainActivity, "생일을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText_Weight.length()<3){
            Toast.makeText(mainActivity, "몸무게를 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText_Breed.length()<1){
            Toast.makeText(mainActivity, "종을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (!bitmap.isRecycled()){
            storage = FirebaseStorage.getInstance();
            storage.getReferenceFromUrl(imageURL).delete();
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
                    AdjustPetItem();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "업로드에 실패했습니다.\n잠시후 다시 시도해주세요" ,Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            AdjustPetItem();
            progressDialog.dismiss();
        }
    }//AdjustPet_Complete


    //수정전 최초펫추가에서 입력받았던 정보 불러오기, 이화면 시작하자마자 실행
    public void setArgument() {
        if(getArguments() != null){
            document = getArguments().getString("document");
            editText_Name.setText(getArguments().getString("name"));
            editText_Sex.setText(getArguments().getString("sex"));
            editText_Breed.setText(getArguments().getString("breed"));
            editText_Date.setText(getArguments().getString("date"));
            editText_Weight.setText(getArguments().getString("weight"));
            editText_Memo.setText(getArguments().getString("memo"));
            imageURL = getArguments().getString("imageURL"); //수정하기화면으로 넘어왓을때 이전 사진 피카소로 띄어놓기
            Picasso.get().load(imageURL).into(imageViewPet);
        }
    }//setArgument


}//class PetAdjustFragment
