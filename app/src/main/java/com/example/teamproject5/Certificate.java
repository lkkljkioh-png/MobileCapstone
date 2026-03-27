package com.example.teamproject5;

public class Certificate {

    private final int id;
    private final String name;
    private final String description;
    private final String category;
    private boolean favorite;

    public Certificate(int id, String name, String description, String category, boolean favorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}