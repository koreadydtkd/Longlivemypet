package com.mhj.longlivemypet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class CommunityDetailAdapter extends FirestoreRecyclerAdapter<CommunityDetailItem, CommunityDetailAdapter.CommunityDetailHolder> {
    View itemView;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
    String document;

    public CommunityDetailAdapter(@NonNull FirestoreRecyclerOptions<CommunityDetailItem> options, String document) {
        super(options);
        this.document = document;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommunityDetailHolder holder, int position, @NonNull CommunityDetailItem item) {
        holder.textView_nick.setText(item.getNick() + " " + dateFormat.format(item.getDate()));
        holder.textView_comment_date.setText(item.getCommnet());
    }

    @NonNull
    @Override
    public CommunityDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.community_comment_item, parent, false);
        return new CommunityDetailHolder(itemView);
    }

    public class CommunityDetailHolder extends RecyclerView.ViewHolder{
        TextView textView_nick, textView_comment_date;
        public CommunityDetailHolder(@NonNull View itemView) {
            super(itemView);
            textView_nick = itemView.findViewById(R.id.textView_nick);
            textView_comment_date = itemView.findViewById(R.id.textView_comment_date);
        }
    }

}
