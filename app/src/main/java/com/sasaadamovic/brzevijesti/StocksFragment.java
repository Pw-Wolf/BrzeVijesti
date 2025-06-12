package com.sasaadamovic.brzevijesti;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StocksFragment extends Fragment {

    private TextView stockSymbolTextView;
    private TextView stockPriceTextView;
    private TextView stockHighPriceTextView;
    private TextView stockLowPriceTextView;
    private TextView stockOpenPriceTextView;
    private TextView stockPreviousClosePriceTextView;
    private Button addToFavoritesButton;

    private NewsApiService newsApiService;
    private ExecutorService executorService;
    private AppDatabase db;
    private FavoriteStockDao favoriteStockDao;

    private String currentStockSymbol = "AAPL"; // Simbol dionice koju trenutno prikazujemo

    public StocksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stocks, container, false);

        stockSymbolTextView = view.findViewById(R.id.stockSymbolTextView);
        stockPriceTextView = view.findViewById(R.id.stockPriceTextView);
        stockHighPriceTextView = view.findViewById(R.id.stockHighPriceTextView);
        stockLowPriceTextView = view.findViewById(R.id.stockLowPriceTextView);
        stockOpenPriceTextView = view.findViewById(R.id.stockOpenPriceTextView);
        stockPreviousClosePriceTextView = view.findViewById(R.id.stockPreviousClosePriceTextView);
        addToFavoritesButton = view.findViewById(R.id.addToFavoritesButton);

        newsApiService = new NewsApiService();
        executorService = Executors.newSingleThreadExecutor();

        // Inicijalizacija Room baze podataka
        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "brzevijesti-db").build();
        favoriteStockDao = db.favoriteStockDao();


        fetchStockQuote(currentStockSymbol); // Primjer za Apple dionicu
        checkFavoriteStatus(currentStockSymbol);

        addToFavoritesButton.setOnClickListener(v -> {
            toggleFavoriteStock(currentStockSymbol, "Apple Inc."); // Trebali biste dohvatiti pravo ime tvrtke
        });

        return view;
    }

    private void fetchStockQuote(String symbol) {
        executorService.execute(() -> {
            try {
                NewsApiService.StockQuote quote = newsApiService.getStockQuote(symbol);
                if (quote != null) {
                    getActivity().runOnUiThread(() -> {
                        stockSymbolTextView.setText(symbol);
                        stockPriceTextView.setText(String.format("Current Price: %.2f", quote.getCurrentPrice()));
                        stockHighPriceTextView.setText(String.format("High Price: %.2f", quote.getHighPrice()));
                        stockLowPriceTextView.setText(String.format("Low Price: %.2f", quote.getLowPrice()));
                        stockOpenPriceTextView.setText(String.format("Open Price: %.2f", quote.getOpenPrice()));
                        stockPreviousClosePriceTextView.setText(String.format("Previous Close: %.2f", quote.getPreviousClosePrice()));
                    });
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to fetch stock quote for " + symbol, Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error fetching stock quote: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void checkFavoriteStatus(String symbol) {
        executorService.execute(() -> {
            boolean isFav = favoriteStockDao.isFavorite(symbol);
            getActivity().runOnUiThread(() -> {
                if (isFav) {
                    addToFavoritesButton.setText("Remove from Favorites");
                } else {
                    addToFavoritesButton.setText("Add to Favorites");
                }
            });
        });
    }

    private void toggleFavoriteStock(String symbol, String companyName) {
        executorService.execute(() -> {
            boolean isCurrentlyFavorite = favoriteStockDao.isFavorite(symbol);
            if (isCurrentlyFavorite) {
                // Ukloni iz favorita
                favoriteStockDao.delete(new FavoriteStock(symbol, companyName)); // Potrebno je dohvati ID za brisanje ili pronaći po simbolu
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), symbol + " removed from favorites", Toast.LENGTH_SHORT).show();
                    addToFavoritesButton.setText("Add to Favorites");
                });
            } else {
                // Dodaj u favorite
                favoriteStockDao.insert(new FavoriteStock(symbol, companyName));
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), symbol + " added to favorites", Toast.LENGTH_SHORT).show();
                    addToFavoritesButton.setText("Remove from Favorites");
                });
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