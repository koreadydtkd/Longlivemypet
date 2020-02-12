package com.mhj.longlivemypet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class CommunityDetailAdapter extends FirestoreRecyclerAdapter<CommunityDetailItem, CommunityDetailAdapter.CommunityDetailHolder> {
    View itemView;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
    String document, documentcomment, nick;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public CommunityDetailAdapter(@NonNull FirestoreRecyclerOptions<CommunityDetailItem> options, String document) {
        super(options);
        this.document = document;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommunityDetailHolder holder, final int position, @NonNull CommunityDetailItem item) {
        getUserNick();

        holder.textView_nick.setText(item.getNick());
        holder.textView_date.setText(dateFormat.format(item.getDate()));
        holder.textView_comment.setText(item.getCommnet());

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.textView_nick.getText().toString().equals(nick)){
                    firestore.collection("Community").document(document).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            long count = (long) documentSnapshot.get("commentCount");
                            firestore.collection("Community").document(document).update("commentCount", count - 1);
                        }
                    });
                    documentcomment = getSnapshots().getSnapshot(position).getId();
                    firestore.collection("Community").document(document).collection("Comment").document(documentcomment).delete();
                    Toast.makeText(holder.itemView.getContext(), "댓글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(holder.itemView.getContext(), "삭제권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @NonNull
    @Override
    public CommunityDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.community_comment_item, parent, false);
        return new CommunityDetailHolder(itemView);
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

    public class CommunityDetailHolder extends RecyclerView.ViewHolder{
        TextView textView_nick, textView_comment, textView_date;
        Button button_delete;
        public CommunityDetailHolder(@NonNull View itemView) {
            super(itemView);
            textView_nick = itemView.findViewById(R.id.textView_nick);
            textView_date = itemView.findViewById(R.id.textView_date);
            textView_comment = itemView.findViewById(R.id.textView_comment);
            button_delete = itemView.findViewById(R.id.button_delete);
        }
    }

}
