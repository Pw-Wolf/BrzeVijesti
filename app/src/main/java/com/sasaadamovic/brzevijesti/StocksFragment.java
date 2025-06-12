package com.sasaadamovic.brzevijesti; // Your package name

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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.widget.Button;

public class StocksFragment extends Fragment implements StockAdapter.OnItemClickListener {

    private RecyclerView stocksRecyclerView;
    private StockAdapter stockAdapter;
    private List<String> stockSymbols; // Lista simbola dionica
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

        // Lista dionica koje želite prikazati
        stockSymbols = new ArrayList<>(Arrays.asList(
                "AAPL", "MSFT", "GOOGL", "AMZN", "NVDA", "TSLA"
//                , "META", "AMD", "INTC", "CSCO"
        ));

        stockAdapter = new StockAdapter(stockSymbols, this);
        stocksRecyclerView.setAdapter(stockAdapter);

        newsApiService = new NewsApiService();
        executorService = Executors.newSingleThreadExecutor();

        // Inicijalizacija Room baze podataka
        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "brzevijesti-db").build();
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
                    getActivity().runOnUiThread(() -> {
                        if (quote != null) {
                            // Pronađite ViewHolder za ovu poziciju i ažurirajte TextView
                            RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                            if (viewHolder instanceof StockAdapter.StockViewHolder) {
                                ((StockAdapter.StockViewHolder) viewHolder).stockPrice.setText(String.format("Current Price: %.2f", quote.getCurrentPrice()));
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to fetch quote for " + symbol, Toast.LENGTH_SHORT).show();
                        }
                        // Provjerite status favorita i ažurirajte gumb
                        checkFavoriteStatus(symbol, position);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error fetching quote for " + symbol + ": " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    private void checkFavoriteStatus(String symbol, int position) {
        executorService.execute(() -> {
            boolean isFav = favoriteStockDao.isFavorite(symbol);
            getActivity().runOnUiThread(() -> {
                RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                if (viewHolder instanceof StockAdapter.StockViewHolder) {
                    Button favoriteButton = ((StockAdapter.StockViewHolder) viewHolder).favoriteButton;
                    if (isFav) {
                        favoriteButton.setText("Remove from Favorites");
                    } else {
                        favoriteButton.setText("Add to Favorites");
                    }
                }
            });
        });
    }

    @Override
    public void onFavoriteClick(String symbol, int position) {
        // U ovoj jednostavnoj implementaciji, nemamo ime tvrtke direktno iz API-ja za quote.
        // U stvarnoj aplikaciji, trebali biste dohvatiti ime tvrtke za simbol.
        // Ovdje ćemo koristiti simbol kao privremeno ime tvrtke.
        toggleFavoriteStock(symbol, symbol, position);
    }

    private void toggleFavoriteStock(String symbol, String companyName, int position) {
        executorService.execute(() -> {
            boolean isCurrentlyFavorite = favoriteStockDao.isFavorite(symbol);
            if (isCurrentlyFavorite) {
                // Ukloni iz favorita
                FavoriteStock stockToRemove = new FavoriteStock(symbol, companyName);
                // Da biste obrisali, morate dohvati objekt s ID-jem ili koristiti Query za brisanje po simbolu
                // Da biste obrisali, morate dohvati objekt s ID-jem ili koristiti Query za brisanje po simbolu
                // Za ovu implementaciju, pretpostavit ćemo da Room može obrisati objekt na temelju primarnog ključa (koji se generira automatski).
                // Ako ID nije postavljen, ovo može uzrokovati problem. Bolje je brisati pomoću simbola.
                favoriteStockDao.deleteBySymbol(symbol); // Koristite novu metodu za brisanje po simbolu

                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), symbol + " removed from favorites", Toast.LENGTH_SHORT).show();
                    // Ažurirajte gumb nakon promjene statusa favorita
                    RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                    if (viewHolder instanceof StockAdapter.StockViewHolder) {
                        ((StockAdapter.StockViewHolder) viewHolder).favoriteButton.setText("Add to Favorites");
                    }
                });
            } else {
                // Dodaj u favorite
                FavoriteStock newFavorite = new FavoriteStock(symbol, companyName);
                favoriteStockDao.insert(newFavorite);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), symbol + " added to favorites", Toast.LENGTH_SHORT).show();
                    // Ažurirajte gumb nakon promjene statusa favorita
                    RecyclerView.ViewHolder viewHolder = stocksRecyclerView.findViewHolderForAdapterPosition(position);
                    if (viewHolder instanceof StockAdapter.StockViewHolder) {
                        ((StockAdapter.StockViewHolder) viewHolder).favoriteButton.setText("Remove from Favorites");
                    }
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