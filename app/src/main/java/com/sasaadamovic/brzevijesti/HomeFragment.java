package com.sasaadamovic.brzevijesti;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsArticle> newsArticleList; // Koristite NewsArticle za prikaz
    private NewsApiService newsApiService;
    private ExecutorService executorService;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsArticleList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsArticleList);
        newsRecyclerView.setAdapter(newsAdapter);

        newsApiService = new NewsApiService();
        executorService = Executors.newSingleThreadExecutor();

        fetchMarketNews();

        return view;
    }

    private void fetchMarketNews() {
        executorService.execute(() -> {
            try {
                List<NewsApiService.GeneralNewsArticle> generalNews = newsApiService.getMarketNews("general"); // Dohvati opće vijesti
                if (generalNews != null && !generalNews.isEmpty()) {
                    List<NewsArticle> convertedNews = new ArrayList<>();
                    for (NewsApiService.GeneralNewsArticle item : generalNews) {
                        // Pretvorite GeneralNewsArticle u NewsArticle jer NewsAdapter koristi NewsArticle
                        convertedNews.add(new NewsArticle(
                                item.getCategory(),
                                item.getDatetime(),
                                item.getHeadline(),
                                item.getId(),
                                item.getImage(),
                                null, // related field is not in GeneralNewsArticle
                                item.getSource(),
                                item.getSummary(),
                                item.getUrl()
                        ));
                    }
                    getActivity().runOnUiThread(() -> {
                        newsArticleList.clear();
                        newsArticleList.addAll(convertedNews);
                        newsAdapter.notifyDataSetChanged();
                    });
                } else {
                    Log.e(TAG, "No general news found or error fetching.");
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching general news: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}