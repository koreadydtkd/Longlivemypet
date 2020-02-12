package com.mhj.longlivemypet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class CommunityAdapter extends FirestoreRecyclerAdapter<CommunityItem, CommunityAdapter.CommunityHolder> {
    View itemView;
    String document, classification, title, content, userNick, date;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    itemDetailListener listener;

    public CommunityAdapter(@NonNull FirestoreRecyclerOptions<CommunityItem> options, itemDetailListener listener) {
        super(options);
        this.listener = listener;
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
                document = getSnapshots().getSnapshot(position).getId();
                classification = item.classification;
                title = item.title;
                userNick = item.nick;
                content = item.content;
                date = dateFormat.format(item.getDate());
                listener.itemDetail(document, classification, title, userNick, content, date);
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

    interface itemDetailListener{
        void itemDetail(String document, String classification, String title, String userNick, String content, String date);
    }

}
