package com.mhj.longlivemypet;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>  {
    ArrayList<SearchDTO> items = new ArrayList<SearchDTO>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.new_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SearchDTO item = items.get(position);
        holder.setItem(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SearchDTO item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNews_title, textNews_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNews_title = itemView.findViewById(R.id.textNews_title);
            textNews_description = itemView.findViewById(R.id.textNews_description);
        }

        public void setItem(SearchDTO item) {
            textNews_title.setText(Html.fromHtml(item.getTitle()));
            textNews_description.setText(Html.fromHtml(item.getDescription()));
        }

    }
}
