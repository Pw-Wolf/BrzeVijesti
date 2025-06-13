package com.sasaadamovic.brzevijesti;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Dodajte ovaj import
import android.widget.TextView; // Dodajte ovaj import
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StocksFragment extends Fragment implements StockAdapter.OnItemClickListener {

    private RecyclerView stocksRecyclerView;
    private StockAdapter stockAdapter;
    private List<String> stockSymbols;
    private NewsApiService newsApiService;
    private ExecutorService executorService;
    private AppDatabase db;
    private FavoriteStockDao favoriteStockDao;

    public StocksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stocks, container, false);

        stocksRecyclerView = view.findViewById(R.id.stocksRecyclerView);
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        stockSymbols = new ArrayList<>(Arrays.asList(
                "AAPL", "MSFT", "GOOGL", "AMZN", "NVDA", "TSLA"
//                , "META", "BRK.B", "JPM", "V", "JNJ", "WMT", "PG", "UNH", "HD", "MA", "XOM", "CVX", "BAC", "PFE"
        ));

        stockAdapter = new StockAdapter(stockSymbols, this);
        stocksRecyclerView.setAdapter(stockAdapter);

        newsApiService = new NewsApiService();
        executorService = Executors.newSingleThreadExecutor();

//        db = Room.databaseBuilder(getContext(),
//                AppDatabase.class, "brzevijesti-db").fallbackToDestructiveMigration() // <-- DODAJ OVU LINIJU
//                .build();
        db = AppDatabase.getInstance(getContext().getApplicationContext());

        favoriteStockDao = db.favoriteStockDao();

        fetchAllStockQuotes();

        return view;
    }

    private void fetchAllStockQuotes() {
        for (int i = 0; i < stockSymbols.size(); i++) {
            final int position = i;
            final String symbol = stockSymbols.get(i);
            executorService.execute(() -> {
                try {
                    NewsApiService.StockQuote quote = newsApiService.getStockQuote(symbol);
                    // Provjerite je li fragment još uvijek pripojen aktivnosti prije ažuriranja UI-ja
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (isAdded()) { // Ponovna provjera unutar runOnUiThread
                                RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                                if (viewHolder instanceof StockAdapter.StockViewHolder) {
                                    if (quote != null) {
                                        ((StockAdapter.StockViewHolder) viewHolder).stockPrice.setText(String.format("Current Price: %.2f", quote.getCurrentPrice()));
                                    } else {
                                        ((StockAdapter.StockViewHolder) viewHolder).stockPrice.setText("N/A"); // Prikaži N/A ako nema podataka
                                    }
                                }
                                // Provjerite status favorita i ažurirajte gumb
                                checkFavoriteStatus(symbol, position);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (isAdded()) {
                                Toast.makeText(getContext(), "Error fetching quote for " + symbol + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    private void checkFavoriteStatus(String symbol, int position) {
        executorService.execute(() -> {
            boolean isFav = favoriteStockDao.isFavorite(symbol);
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (isAdded()) {
                        RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                        if (viewHolder instanceof StockAdapter.StockViewHolder) {
                            Button favoriteButton = ((StockAdapter.StockViewHolder) viewHolder).favoriteButton;
                            if (isFav) {
                                favoriteButton.setText("Remove from Favorites");
                            } else {
                                favoriteButton.setText("Add to Favorites");
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onFavoriteClick(String symbol, int position) {
        toggleFavoriteStock(symbol, symbol, position);
    }

    private void toggleFavoriteStock(String symbol, String companyName, int position) {
        executorService.execute(() -> {
            boolean isCurrentlyFavorite = favoriteStockDao.isFavorite(symbol);
            if (isCurrentlyFavorite) {
                favoriteStockDao.deleteBySymbol(symbol);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (isAdded()) {
                            Toast.makeText(getContext(), symbol + " removed from favorites", Toast.LENGTH_SHORT).show();
                            RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                            if (viewHolder instanceof StockAdapter.StockViewHolder) {
                                ((StockAdapter.StockViewHolder) viewHolder).favoriteButton.setText("Add to Favorites");
                            }
                        }
                    });
                }
            } else {
                FavoriteStock newFavorite = new FavoriteStock(symbol, companyName);
                favoriteStockDao.insert(newFavorite);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (isAdded()) {
                            Toast.makeText(getContext(), symbol + " added to favorites", Toast.LENGTH_SHORT).show();
                            RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                            if (viewHolder instanceof StockAdapter.StockViewHolder) {
                                ((StockAdapter.StockViewHolder) viewHolder).favoriteButton.setText("Remove from Favorites");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow(); // Koristite shutdownNow() za prekidanje zadataka odmah
        }
    }
}