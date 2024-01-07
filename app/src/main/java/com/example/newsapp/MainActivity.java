package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    private interface NewsApiService {
        @GET("top-headlines?country=us&apiKey=d3deed210e934d2691d36c850ffe20f3")
        Call<ApiResponse> getNews();

        class ApiResponse {
            private List<NewsItem> articles;

            public List<NewsItem> getArticles() {
                return articles;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchData();
    }

    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService apiService = retrofit.create(NewsApiService.class);

        Call<NewsApiService.ApiResponse> call = apiService.getNews();

        call.enqueue(new Callback<NewsApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiService.ApiResponse> call, Response<NewsApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewsApiService.ApiResponse apiResponse = response.body();
                    List<NewsItem> newsItems = apiResponse.getArticles();

                    // Set up RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    // Create and set adapter
                    NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this, newsItems);
                    recyclerView.setAdapter(newsAdapter);

                    Toast.makeText(MainActivity.this, "Your news is ready", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsApiService.ApiResponse> call, Throwable t) {
                Log.e("Retrofit", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Unable to fetch news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}