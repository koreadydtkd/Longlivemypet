package com.mhj.longlivemypet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommunityDetailFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private String email;
    TextView textView_userNick, textView_classification, textView_date, textView_title, textView_content, textView_noComment;
    EditText editText_comment;
    ImageView imageView;
    CommunityDetailAdapter detailAdapter;
    ViewGroup rootView;
    RecyclerView recyclerView;
    String nick, document, imgURL;
    MainActivity mainActivity;
    ProgressBar progressBar;
    Map<String, Boolean> likeUser = new HashMap<String, Boolean>();
    Button button_like;
    boolean like = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community_detail, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mainActivity = (MainActivity) getActivity();
        button_like = rootView.findViewById(R.id.button_like);
        textView_noComment = rootView.findViewById(R.id.textView_noComment);
        getUserNick();
        setArgument();

        Query query = firestore.collection("Community").document(document).collection("Comment").orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CommunityDetailItem> options = new FirestoreRecyclerOptions.Builder<CommunityDetailItem>().setQuery(query, CommunityDetailItem.class).build();
        detailAdapter = new CommunityDetailAdapter(options, document){
            @Override
            public void onDataChanged() {
                if(detailAdapter.getItemCount() == 0){
                    textView_noComment.setVisibility(View.VISIBLE);
                }else{
                    textView_noComment.setVisibility(View.GONE);
                }
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailAdapter);

        button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likes();
            }
        });

        rootView.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_comment.getText().toString().length() < 1 || editText_comment.getText().toString() == null){
                    Toast.makeText(getContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                addComment();
            }
        });

        rootView.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContent();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        detailAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        detailAdapter.stopListening();
    }

    private void likes(){
        firestore.collection("Community").document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().get("likeUser") != null){
                        likeUser = (Map<String, Boolean>) task.getResult().get("likeUser");
                    }
                    if(likeUser != null && likeUser.containsKey(email) && likeUser.get(email)){
                        likeUser.put(email, false);
                        firestore.collection("Community").document(document)
                                .update("likeUser", likeUser, "likeCount", FieldValue.increment(-1));
                        button_like.setBackgroundResource(R.drawable.ic_thumb_up_black);
                    }else{
                        likeUser.put(email, true);
                        firestore.collection("Community").document(document)
                                .update("likeUser", likeUser, "likeCount", FieldValue.increment(1));
                        button_like.setBackgroundResource(R.drawable.ic_thumb_up);
                    }
                }
            }
        });
    }

    private void deleteContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(textView_userNick.getText().toString());
        String contentNick = sb.substring(5, sb.length());
        if(contentNick.equals(nick)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("삭제 하시겠습니까?");
            builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firestore.collection("Community").document(document).delete();
                    if(imgURL != null){
                        storage.getReferenceFromUrl(imgURL).delete();
                    }
                    Toast.makeText(getContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    mainActivity.replaceFragment(R.id.navigation_community);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else{
            Toast.makeText(getContext(), "삭제권한이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addComment() {
        CommunityDetailItem detailItem = new CommunityDetailItem();
        detailItem.setNick(nick);
        detailItem.setCommnet(editText_comment.getText().toString());
        detailItem.setDate(System.currentTimeMillis());

        firestore.collection("Community").document(document).collection("Comment").document().set(detailItem);
        firestore.collection("Community").document(document).update("commentCount", FieldValue.increment(1));
        Toast.makeText(getContext(), "등록되었습니다." ,Toast.LENGTH_SHORT).show();
        editText_comment.setText("");
    }

    public void setArgument(){
        textView_title = rootView.findViewById(R.id.textView_title);
        textView_userNick = rootView.findViewById(R.id.textView_userNick);
        textView_classification = rootView.findViewById(R.id.textView_classification);
        textView_date = rootView.findViewById(R.id.textView_date);
        imageView = rootView.findViewById(R.id.imageView);
        textView_content = rootView.findViewById(R.id.textView_content);
        progressBar = rootView.findViewById(R.id.progressbar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        editText_comment = rootView.findViewById(R.id.editText_comment);
        if(getArguments() != null){
            document = getArguments().getString("document");
            textView_userNick.setText("작성자: " + getArguments().getString("userNick"));
            textView_classification.setText(getArguments().getString("classification"));
            textView_date.setText(getArguments().getString("date"));
            textView_title.setText("제목: " + getArguments().getString("title"));
            textView_content.setText(getArguments().getString("content"));
            imgURL = getArguments().getString("imgURL");
            if(imgURL != null){
                imgUpload();
            }else{
                textView_content.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
            like = getArguments().getBoolean("like");
            if(like){
                button_like.setBackgroundResource(R.drawable.ic_thumb_up);
            }

        }
    }

    public void imgUpload(){
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#B96548"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(imgURL).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Picasso.get().load(imgURL).into(imageView);
                progressBar.setVisibility(View.GONE);
                textView_content.setVisibility(View.VISIBLE);
            }
            @Override
            public void onError(Exception e) {
                textView_content.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                try {
                    Toast.makeText(getContext(), "이미지 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }catch (NullPointerException n){
                    n.printStackTrace();
                    mainActivity.replaceFragment(R.id.navigation_community);
                }
            }
        });
    }

    private void getUserNick(){
        email = auth.getCurrentUser().getEmail();
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

}
