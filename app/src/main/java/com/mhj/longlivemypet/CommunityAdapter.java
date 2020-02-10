package com.mhj.longlivemypet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CommunityAdapter extends FirestoreRecyclerAdapter<CommunityItem, CommunityAdapter.CommunityHolder> {
    View itemView;

    public CommunityAdapter(@NonNull FirestoreRecyclerOptions<CommunityItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommunityHolder holder, final int position, @NonNull final CommunityItem item) {
        holder.textView_classification.setText(item.getClassification());
        holder.textView_title.setText(item.getTitle());
        holder.textView_userNick.setText(item.getNick());
        holder.textView_date.setText(item.getDate() + "");
        holder.textView_commentCount.setText(item.getCommentCount() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "다음진행: " + position + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Test!!
    @NonNull
    @Override
    public CommunityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.community_item, parent, false);
        return new CommunityHolder(itemView);
    }

    public class CommunityHolder extends RecyclerView.ViewHolder{
        TextView textView_classification, textView_title, textView_userNick, textView_date, textView_commentCount;
        public CommunityHolder(@NonNull View itemView) {
            super(itemView);
            textView_classification = itemView.findViewById(R.id.textView_classification);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_userNick = itemView.findViewById(R.id.textView_userNick);
            textView_date = itemView.findViewById(R.id.textView_date);
            textView_commentCount = itemView.findViewById(R.id.textView_commentCount);
        }
    }
}
