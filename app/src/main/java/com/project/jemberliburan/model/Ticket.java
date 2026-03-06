package com.project.jemberliburan.model;

public class Ticket {
    private int imageResId;
    private String title;
    private String description;
    private int type; // Jenis data

    public Ticket(int imageResId, String title, String description, int type) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }
}

