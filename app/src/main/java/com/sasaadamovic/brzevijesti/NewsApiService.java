package com.sasaadamovic.brzevijesti;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class NewsApiService {

    private static final String TAG = "NewsApiService";

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private final String FINNHUB_API_TOKEN = "cqofc19r01qk95831gi0cqofc19r01qk95831gig";

    public static class GeneralNewsArticle {
        @SerializedName("category")
        private String category;
        @SerializedName("datetime")
        private long datetime;
        @SerializedName("headline")
        private String headline;
        @SerializedName("id")
        private long id;
        @SerializedName("image")
        private String image;
        @SerializedName("source")
        private String source;
        @SerializedName("summary")
        private String summary;
        @SerializedName("url")
        private String url;

        // Getteri
        public String getCategory() { return category; }
        public long getDatetime() { return datetime; }
        public String getHeadline() { return headline; }
        public long getId() { return id; }
        public String getImage() { return image; }
        public String getSource() { return source; }
        public String getSummary() { return summary; }
        public String getUrl() { return url; }
    }

    // Klasa za cijene dionica
    public static class StockQuote {
        @SerializedName("c")
        private float currentPrice;
        @SerializedName("h")
        private float highPrice;
        @SerializedName("l")
        private float lowPrice;
        @SerializedName("o")
        private float openPrice;
        @SerializedName("pc")
        private float previousClosePrice;

        // Getteri
        public float getCurrentPrice() { return currentPrice; }
        public float getHighPrice() { return highPrice; }
        public float getLowPrice() { return lowPrice; }
        public float getOpenPrice() { return openPrice; }
        public float getPreviousClosePrice() { return previousClosePrice; }
    }

    public List<NewsArticle> getCompanyNews(String symbol, String fromDate, String toDate) throws IOException {
        String url = String.format("https://finnhub.io/api/v1/company-news?symbol=%s&from=%s&to=%s&token=%s",
                symbol, fromDate, toDate, FINNHUB_API_TOKEN);

        Log.d(TAG, "Fetching company news from URL: " + url); // Logirajte URL

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Log.e(TAG, "Unexpected code " + response.code() + " for URL: " + url);
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            Log.d(TAG, "Company news API Response: " + responseBody);
            Type listType = new TypeToken<List<NewsArticle>>() {}.getType();
            return gson.fromJson(responseBody, listType);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching company news: " + e.getMessage(), e);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<GeneralNewsArticle> getMarketNews(String category, String fromDate, String toDate) throws IOException {
        String url = String.format("https://finnhub.io/api/v1/news?category=%s&minSentiment=0.7&from=%s&to=%s&token=%s",
                category, fromDate, toDate, FINNHUB_API_TOKEN);

        Log.d(TAG, "Fetching market news from URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Log.e(TAG, "Unexpected code " + response.code() + " for URL: " + url);
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            Log.d(TAG, "Market news API Response: " + responseBody);
            Type listType = new TypeToken<List<GeneralNewsArticle>>() {}.getType();
            return gson.fromJson(responseBody, listType);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching market news: " + e.getMessage(), e);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public StockQuote getStockQuote(String symbol) throws IOException {
        String url = String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s",
                symbol, FINNHUB_API_TOKEN);

        Log.d(TAG, "Fetching stock quote from URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Log.e(TAG, "Unexpected code " + response.code() + " for URL: " + url);
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            Log.d(TAG, "Stock quote API Response: " + responseBody);
            return gson.fromJson(responseBody, StockQuote.class);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching stock quote: " + e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }
}