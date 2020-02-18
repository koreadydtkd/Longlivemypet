package com.mhj.longlivemypet;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

import java.text.SimpleDateFormat;

public class CommunityAdapter extends FirestorePagingAdapter<CommunityItem, CommunityAdapter.CommunityHolder> {
    String document, classification, title, content, userNick, date, imgURL;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    itemDetailListener listener;

    public CommunityAdapter(@NonNull FirestorePagingOptions<CommunityItem> options, itemDetailListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommunityHolder holder, final int position, @NonNull final CommunityItem item) {
        if(item.getClassification().equals("병원추천")){
            holder.textView_classification.setTextColor(Color.parseColor("#FE2E2E"));
        }else if(item.getClassification().equals("카페추천")){
            holder.textView_classification.setTextColor(Color.parseColor("#01A9DB"));
        }else if(item.getClassification().equals("맛집추천")){
            holder.textView_classification.setTextColor(Color.parseColor("#FFAA00"));
        }else if(item.getClassification().equals("궁금해요")){
            holder.textView_classification.setTextColor(Color.parseColor("#04B404"));
        }
        holder.textView_classification.setText(item.getClassification());
        holder.textView_title.setText(item.getTitle());
        holder.textView_userNick.setText(item.getNick());
        holder.textView_date.setText(dateFormat.format(item.getDate()));
        holder.textView_commentCount.setText(item.getCommentCount() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                document = getItem(position).getId();
                Log.d("CommunityAdapter", document);
                classification = item.classification;
                title = item.title;
                userNick = item.nick;
                content = item.content;
                date = dateFormat.format(item.getDate());
                imgURL = item.imgURL;
                listener.itemDetail(document, classification, title, userNick, content, date, imgURL);
            }
        });
    }

    @NonNull
    @Override
    public CommunityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.community_item, parent, false);
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

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {
            case LOADING_INITIAL:
                Log.d("TEST1111111", "!!!!!!!!!");
            case LOADING_MORE:
                Log.d("TEST2222222", "@@@@@@@@@@");
            case LOADED:
                Log.d("TEST3333333", "##########");
            case ERROR:
                Log.d("TEST4444444", "$$$$$$$$$$");
        }
    }

    interface itemDetailListener{
        void itemDetail(String document, String classification, String title, String userNick, String content, String date, String imgURL);
    }

}