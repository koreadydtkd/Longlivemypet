package com.mhj.longlivemypet;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommunityFragmentWrite extends Fragment {
    MainActivity mainActivity;
    EditText editText_title, editText_content;
    ImageView imageView;
    String nick;
    Spinner spinner;
    String imgurl;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community_write, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) getActivity();
        getUserEmail();

        imageView = rootView.findViewById(R.id.imageViewAdd);
        editText_title = rootView.findViewById(R.id.editText_title);
        editText_content = rootView.findViewById(R.id.editText_content);
        spinner = rootView.findViewById(R.id.spinner);

        rootView.findViewById(R.id.imageViewAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 클릭시 실행
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
                Toast.makeText(getContext(), "잠시만 기다려주세요." ,Toast.LENGTH_SHORT).show();
                uploadimg();
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

    void addCommunity(){
        CommunityItem communityItem = new CommunityItem();
        communityItem.setTitle(editText_title.getText().toString());
        communityItem.setClassification(spinner.getSelectedItem().toString());
        communityItem.setNick(nick);
        communityItem.setContent(editText_content.getText().toString());
        communityItem.setDate(System.currentTimeMillis());
        communityItem.setCommentCount(0);

        Log.d("CommunityFragmentWrite", "2" + imgurl);
        communityItem.setImgURL(imgurl);

        firestore.collection("Community").document().set(communityItem);
        Toast.makeText(getContext(), "작성완료되었습니다." ,Toast.LENGTH_SHORT).show();
        mainActivity.replaceFragment(R.id.navigation_community);
    }

    void uploadimg(){
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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
                imgurl = uri.getResult().toString();
                addCommunity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("CommunityFragmentWrite", "실패");
            }
        });
    }

}
