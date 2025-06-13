package com.sasaadamovic.brzevijesti;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        ));

        stockAdapter = new StockAdapter(stockSymbols, this);
        stocksRecyclerView.setAdapter(stockAdapter);

        newsApiService = new NewsApiService();
        executorService = Executors.newSingleThreadExecutor();
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
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (isAdded()) {
                                RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                                if (viewHolder instanceof StockAdapter.StockViewHolder) {
                                    if (quote != null) {
                                        // ISPRAVAK: Korištenje string resursa
                                        ((StockAdapter.StockViewHolder) viewHolder).stockPrice.setText(getString(R.string.stock_current_price, quote.getCurrentPrice()));
                                    } else {
                                        // ISPRAVAK: Korištenje string resursa
                                        ((StockAdapter.StockViewHolder) viewHolder).stockPrice.setText(getString(R.string.stock_price_na));
                                    }
                                }
                                checkFavoriteStatus(symbol, position);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (isAdded()) {
                                // ISPRAVAK: Korištenje string resursa
                                Toast.makeText(getContext(), getString(R.string.error_fetching_quote, symbol, e.getMessage()), Toast.LENGTH_SHORT).show();
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
                                // ISPRAVAK: Korištenje string resursa
                                favoriteButton.setText(getString(R.string.remove_from_favorites));
                            } else {
                                // ISPRAVAK: Korištenje string resursa
                                favoriteButton.setText(getString(R.string.add_to_favorites));
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

    @SuppressLint("SetTextI18n")
    private void toggleFavoriteStock(String symbol, String companyName, int position) {
        executorService.execute(() -> {
            boolean isCurrentlyFavorite = favoriteStockDao.isFavorite(symbol);
            if (isCurrentlyFavorite) {
                favoriteStockDao.deleteBySymbol(symbol);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (isAdded()) {
                            // ISPRAVAK: Korištenje string resursa
                            Toast.makeText(getContext(), getString(R.string.removed_from_favorites, symbol), Toast.LENGTH_SHORT).show();
                            RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                            if (viewHolder instanceof StockAdapter.StockViewHolder) {
                                // ISPRAVAK: Korištenje string resursa
                                ((StockAdapter.StockViewHolder) viewHolder).favoriteButton.setText(getString(R.string.add_to_favorites));
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
                            // ISPRAVAK: Korištenje string resursa
                            Toast.makeText(getContext(), getString(R.string.added_to_favorites, symbol), Toast.LENGTH_SHORT).show();
                            RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                            if (viewHolder instanceof StockAdapter.StockViewHolder) {
                                // ISPRAVAK: Korištenje string resursa
                                ((StockAdapter.StockViewHolder) viewHolder).favoriteButton.setText(getString(R.string.remove_from_favorites));
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
            executorService.shutdownNow();
        }
    }
}