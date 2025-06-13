package com.sasaadamovic.brzevijesti;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "favorite_stocks")
public class FavoriteStock {

    @PrimaryKey
    @NonNull
    private String symbol;
    private String name;

    public FavoriteStock(@NonNull String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}