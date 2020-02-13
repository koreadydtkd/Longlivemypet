package com.mhj.longlivemypet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class CommunityDetailFragment extends Fragment {
    TextView textView_userNick, textView_classification, textView_date, textView_title, textView_content;
    EditText editText_comment;
    ImageView imageView;
    CommunityDetailAdapter detailAdapter;
    ViewGroup rootView;
    RecyclerView recyclerView;
    String nick, document;
    MainActivity mainActivity;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community_detail, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        getUserNick();
        setArgument();
        mainActivity = (MainActivity) getActivity();

        editText_comment = rootView.findViewById(R.id.editText_comment);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        Query query = firestore.collection("Community").document(document).collection("Comment").orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CommunityDetailItem> options = new FirestoreRecyclerOptions.Builder<CommunityDetailItem>().setQuery(query, CommunityDetailItem.class).build();
        detailAdapter = new CommunityDetailAdapter(options, document);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailAdapter);

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

    private void deleteContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(textView_userNick.getText().toString());
        String contentNick = sb.substring(5, sb.length());
        if(contentNick.equals(nick)){
            Toast.makeText(getContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            firestore.collection("Community").document(document).delete();
            mainActivity.replaceFragment(R.id.navigation_community);
        }else{
            Toast.makeText(getContext(), "삭제권한이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addComment() {
        CommunityDetailItem detailItem = new CommunityDetailItem();
        detailItem.setNick(nick);
        detailItem.setCommnet(editText_comment.getText().toString());
        detailItem.setDate(System.currentTimeMillis());

        firestore.collection("Community").document(document).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long count = (long) documentSnapshot.get("commentCount");
                firestore.collection("Community").document(document).update("commentCount", count + 1);
            }
        });

        firestore.collection("Community").document(document).collection("Comment").document().set(detailItem);
        Toast.makeText(getContext(), "등록되었습니다." ,Toast.LENGTH_SHORT).show();
        editText_comment.setText("");
    }

    public void setArgument(){
        textView_userNick = rootView.findViewById(R.id.textView_userNick);
        textView_classification = rootView.findViewById(R.id.textView_classification);
        textView_date = rootView.findViewById(R.id.textView_date);
        textView_title = rootView.findViewById(R.id.textView_title);
        textView_content = rootView.findViewById(R.id.textView_content);
        imageView = rootView.findViewById(R.id.imageView);
        if(getArguments() != null){
            document = getArguments().getString("document");
            textView_userNick.setText("작성자: " + getArguments().getString("userNick"));
            textView_classification.setText(getArguments().getString("classification"));
            textView_date.setText(getArguments().getString("date"));
            textView_title.setText("제목: " + getArguments().getString("title"));
            textView_content.setText(getArguments().getString("content"));
            String imgURL = getArguments().getString("imgURL");
            Log.d("setArgument", imgURL);
            if(imgURL == null){
                Log.d("setArgument", "asas");
            }
            Picasso.get().load(imgURL).into(imageView);

        }
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

    private void getUserNick(){
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

}
