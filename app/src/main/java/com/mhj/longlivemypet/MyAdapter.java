package com.mhj.longlivemypet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {
    ArrayList<AskItem> items = new ArrayList<AskItem>();

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.ask_item, parent, false);
        return new MyAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        AskItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(AskItem item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_title, textView_question, textView_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_question = itemView.findViewById(R.id.textView_question);
            textView_date = itemView.findViewById(R.id.textView_date);
        }

        public void setItem(AskItem item) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss a");
            textView_title.setText(item.getTitle());
            textView_question.setText(item.getQuestion());
            textView_date.setText("작성일 : " + dateFormat.format(item.getDate()));
        }
    }

}
