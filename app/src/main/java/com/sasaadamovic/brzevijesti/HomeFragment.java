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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsArticle> newsArticleList;
    private NewsApiService newsApiService;
    private ExecutorService executorService;

    private static final int MAX_NEWS_TO_DISPLAY = 10;

    public HomeFragment() {
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
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String toDate = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String fromDate = dateFormat.format(calendar.getTime());

        executorService.execute(() -> {
            try {
                List<NewsApiService.GeneralNewsArticle> generalNews = newsApiService.getMarketNews("general", fromDate, toDate);
                if (generalNews != null && !generalNews.isEmpty()) {
                    List<NewsArticle> convertedNews = new ArrayList<>();
                    for (NewsApiService.GeneralNewsArticle item : generalNews) {
                        convertedNews.add(new NewsArticle(
                                item.getCategory(),
                                item.getDatetime(),
                                item.getHeadline(),
                                item.getId(),
                                item.getImage(),
                                null,
                                item.getSource(),
                                item.getSummary(),
                                item.getUrl()
                        ));
                    }

                    final List<NewsArticle> limitedNews;
                    if (convertedNews.size() > MAX_NEWS_TO_DISPLAY) {
                        limitedNews = convertedNews.subList(0, MAX_NEWS_TO_DISPLAY);
                    } else {
                        limitedNews = convertedNews;
                    }

                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (isAdded()) {
                                newsArticleList.clear();
                                newsArticleList.addAll(limitedNews);
                                newsAdapter.notifyDataSetChanged();
                            }
                        });
                    }
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
            executorService.shutdownNow();
        }
    }
}