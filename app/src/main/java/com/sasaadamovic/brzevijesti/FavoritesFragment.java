package com.sasaadamovic.brzevijesti;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteStocksAdapter adapter;
    private AppDatabase db;
    private List<FavoriteStock> favoriteStocks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(getContext().getApplicationContext());

        loadFavoriteStocks();

        return view;
    }

    private void loadFavoriteStocks() {
        new Thread(() -> {
            favoriteStocks = db.favoriteStockDao().getAll();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    // Prilagodba poziva konstruktora adaptera
                    adapter = new FavoriteStocksAdapter(favoriteStocks,
                            symbol -> {
                                // Klik na element, ako trebaš
                            },
                            stock -> showEditDialog(stock),
                            stock -> removeFavorite(stock) // <-- Novi listener za brisanje
                    );
                    recyclerView.setAdapter(adapter);
                });
            }
        }).start();
    }

    // NOVA METODA za brisanje favorita
    private void removeFavorite(FavoriteStock stock) {
        new Thread(() -> {
            db.favoriteStockDao().deleteBySymbol(stock.getSymbol());
            // Ponovno učitaj favorite da osvježiš prikaz
            loadFavoriteStocks();
        }).start();
    }

    private void showEditDialog(FavoriteStock stock) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Promijeni naslov");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(stock.getName());
        builder.setView(input);

        builder.setPositiveButton("Spremi", (dialog, which) -> {
            String newName = input.getText().toString();
            stock.setName(newName);
            updateStock(stock);
        });
        builder.setNegativeButton("Odustani", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateStock(FavoriteStock stock) {
        new Thread(() -> {
            db.favoriteStockDao().update(stock);
            loadFavoriteStocks();
        }).start();
    }
}