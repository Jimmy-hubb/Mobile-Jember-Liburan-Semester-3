package com.project.jemberliburan.model;

public class Wellcome {
    private String title;
    private String description;
    private int imageResource;
    private int swipeImageResource; // Tambahkan ini

    public Wellcome(String title, String description, int imageResource, int swipeImageResource) {
        this.title = title;
        this.description = description;
        this.imageResource = imageResource;
        this.swipeImageResource = swipeImageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getSwipeImageResource() {
        return swipeImageResource;
    }
}

