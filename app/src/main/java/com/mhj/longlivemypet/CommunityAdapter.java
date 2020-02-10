package com.mhj.longlivemypet;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class CommunityAdapter extends FirestoreRecyclerAdapter<CommunityItem, CommunityAdapter.CommunityHolder> {
    View itemView;
    String classification, title, userNick, date, commentCount;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CommunityAdapter(@NonNull FirestoreRecyclerOptions<CommunityItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommunityHolder holder, final int position, @NonNull final CommunityItem item) {
        holder.textView_classification.setText(item.getClassification());
        holder.textView_title.setText(item.getTitle());
        holder.textView_userNick.setText(item.getNick());
        holder.textView_date.setText(dateFormat.format(item.getDate()));
        holder.textView_commentCount.setText(item.getCommentCount() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classification = holder.textView_classification.getText().toString();
                title = holder.textView_title.getText().toString();
                userNick = holder.textView_userNick.getText().toString();
                date = holder.textView_date.getText().toString();
                commentCount = holder.textView_commentCount.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("classification", classification);
                intent.putExtra("title", title);
                intent.putExtra("userNick", userNick);
                intent.putExtra("date", date);
                intent.putExtra("commentCount", commentCount);
            }
        });
    }

    @NonNull
    @Override
    public CommunityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.community_item, parent, false);
        return new CommunityHolder(itemView);
    }

    public class CommunityHolder extends RecyclerView.ViewHolder{
        TextView textView_classification, textView_title, textView_content, textView_userNick, textView_date, textView_commentCount;
        public CommunityHolder(@NonNull View itemView) {
            super(itemView);
            textView_classification = itemView.findViewById(R.id.textView_classification);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_userNick = itemView.findViewById(R.id.textView_userNick);
            textView_date = itemView.findViewById(R.id.textView_date);
            textView_commentCount = itemView.findViewById(R.id.textView_commentCount);
            textView_content = itemView.findViewById(R.id.textView_content);
        }
    }
}
