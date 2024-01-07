package com.example.newsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsItems;
    private Context context;

    public NewsAdapter(Context context, List<NewsItem> newsItems) {
        this.context = context;
        this.newsItems = newsItems;

    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsItems.get(position);
        holder.newsTitle.setText(newsItem.getTitle());

        class CustomDialogHelper {

            private Context context;
            private Dialog customDialog;

            public CustomDialogHelper(Context context) {
                this.context = context;
                initDialog();
            }

            private void initDialog() {
                customDialog = new Dialog(context);
                customDialog.setContentView(R.layout.custom_dialog);

                TextView newsTitle = customDialog.findViewById(R.id.newsTitle);
                TextView newsDescription = customDialog.findViewById(R.id.newsDescription);
                ImageView newsImage = customDialog.findViewById(R.id.newsImage);
                TextView newsSource = customDialog.findViewById(R.id.newsSource);

                newsTitle.setText(newsItem.getTitle());
                newsDescription.setText(newsItem.getDescription());
                newsSource.setText(newsItem.getUrl());
                Picasso.get()
                        .load(newsItem.getUrlToImage())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(newsImage);

            }

            public void show() {
                customDialog.show();
            }

            public void dismiss() {
                customDialog.dismiss();
            }

            public boolean isShowing() {
                return customDialog.isShowing();
            }
        }

        CustomDialogHelper newsDialog = new CustomDialogHelper(context);

        Picasso.get()
                .load(newsItem.getUrlToImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .resize(100, 100)
                .onlyScaleDown()
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNews(newsItem.getUrl());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                newsDialog.show();
                return true;
            }
        });
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    // Hide the custom dialog when the click is released
                    newsDialog.dismiss();
                }
                return false;
            }
        });
    }

    private void openNews(String url) {
        Intent intent = new Intent(context, NewsWebActivity.class);
        intent.putExtra(NewsWebActivity.URL, url);
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        ImageView image;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            image = itemView.findViewById(R.id.imageView);

        }
    }
}
