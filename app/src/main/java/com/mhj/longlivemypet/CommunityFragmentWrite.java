package com.mhj.longlivemypet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CommunityFragmentWrite extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    MainActivity mainActivity;
    EditText editText_title, editText_content;
    ImageView imageViewAdd;
    String nick, imgurl;
    Spinner spinner;
    Bitmap bitmap = null;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community_write, container, false);
        rootView.setEnabled(false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        getUserEmail();

        imageViewAdd = rootView.findViewById(R.id.imageViewAdd);
        editText_title = rootView.findViewById(R.id.editText_title);
        editText_content = rootView.findViewById(R.id.editText_content);
        spinner = rootView.findViewById(R.id.spinner);

        rootView.findViewById(R.id.imageViewAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessGallery();
            }
        });

        rootView.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityFragment communityFragment = new CommunityFragment();
                mainActivity.replaceFragment(communityFragment);
            }
        });

        rootView.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_title.getText().toString().length() < 2){
                    Toast.makeText(getContext(), "제목이 너무 짧습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editText_content.getText().toString().length() < 2){
                    Toast.makeText(getContext(), "내용이 너무 짧습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                addCommunity();
            }
        });

        rootView.findViewById(R.id.button_rotateImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap != null){
                    imageViewAdd.setImageBitmap(getRotatedBitmap(90));
                }
            }
        });

        return rootView;
    }

    private void getUserEmail(){
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

    void addItem(){
        CommunityItem communityItem = new CommunityItem();
        communityItem.setTitle(editText_title.getText().toString());
        communityItem.setClassification(spinner.getSelectedItem().toString());
        communityItem.setNick(nick);
        communityItem.setContent(editText_content.getText().toString());
        communityItem.setDate(System.currentTimeMillis());
        communityItem.setCommentCount(0);
        communityItem.setImgURL(imgurl);

        firestore.collection("Community").document().set(communityItem);
        Toast.makeText(getContext(), "작성완료되었습니다." ,Toast.LENGTH_SHORT).show();
        mainActivity.replaceFragment(R.id.navigation_community);
    }

    void addCommunity(){
        imageViewAdd.setDrawingCacheEnabled(true);
        imageViewAdd.buildDrawingCache();

        if(bitmap != null){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려주세요...");
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

            UploadTask uploadTask = imageRef.putBytes(data, metadata);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uri.isComplete());
                    imgurl = uri.getResult().toString();
                    addItem();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "업로드에 실패했습니다.\n잠시후 다시 시도해주세요" ,Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            imgurl = null;
            addItem();
        }
    }

    public void accessGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 92);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 92) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    imageViewAdd.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getContext(), "사진 선택을 취소하였습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
    }

}