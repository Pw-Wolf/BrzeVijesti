package com.sasaadamovic.brzevijesti;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class NewsApiService {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    // Oprez: API token ne bi trebao biti hardkodiran u produkcijskoj aplikaciji!
    // Bolje ga je dohvatiti sa vlastitog backenda ili ga spremiti na sigurniji način.
    private final String FINNHUB_API_TOKEN = "cqofc19r01qk95831gi0cqofc19r01qk95831gig";

    public List<NewsArticle> getCompanyNews(String symbol, String fromDate, String toDate) throws IOException {
        String url = String.format("https://finnhub.io/api/v1/company-news?symbol=%s&from=%s&to=%s&token=%s",
                symbol, fromDate, toDate, FINNHUB_API_TOKEN);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            Type listType = new TypeToken<List<NewsArticle>>() {}.getType();
            return gson.fromJson(responseBody, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Vrati praznu listu u slučaju greške
        }
    }
}