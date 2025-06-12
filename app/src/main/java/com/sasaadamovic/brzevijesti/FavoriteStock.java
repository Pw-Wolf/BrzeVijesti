package com.sasaadamovic.brzevijesti;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_stocks")
public class FavoriteStock {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String symbol;
    private String companyName; // Možete dodati i druge relevantne podatke

    public FavoriteStock(String symbol, String companyName) {
        this.symbol = symbol;
        this.companyName = companyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}