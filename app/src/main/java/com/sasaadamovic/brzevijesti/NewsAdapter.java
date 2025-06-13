package com.sasaadamovic.brzevijesti;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsArticle> newsList;

    public NewsAdapter(List<NewsArticle> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle newsArticle = newsList.get(position);

        holder.newsHeadline.setText(newsArticle.getHeadline());
        holder.newsSummary.setText(newsArticle.getSummary());


        Date date = new Date(newsArticle.getDatetime() * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        holder.newsSourceDate.setText("Source: " + newsArticle.getSource() + " | Date: " + dateFormat.format(date));

        if (newsArticle.getImage() != null && !newsArticle.getImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(newsArticle.getImage())

                    .into(holder.newsImage);
        }

        holder.readMoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsArticle.getUrl()));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView newsHeadline;
        TextView newsSummary;
        TextView newsSourceDate;
        Button readMoreButton;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsHeadline = itemView.findViewById(R.id.news_headline);
            newsSummary = itemView.findViewById(R.id.news_summary);
            newsSourceDate = itemView.findViewById(R.id.news_source_date);
            readMoreButton = itemView.findViewById(R.id.news_read_more_button);
        }
    }
}