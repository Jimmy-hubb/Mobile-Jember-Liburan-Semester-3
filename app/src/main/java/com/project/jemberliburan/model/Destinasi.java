package com.project.jemberliburan.model;

public class Destinasi {
    private String name;
    private int imageResId;
    private String address;
    private double rating;
    private int wisataId;
    private String deskripsi;

    public Destinasi(String name, int imageResId, String address, double rating, int wisataId, String deskripsi) {
        this.name = name;
        this.imageResId = imageResId;
        this.address = address;
        this.rating = rating;
        this.wisataId = wisataId;
        this.deskripsi = deskripsi;
    }

    // Getter Methods
    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public int getWisataId() {
        return wisataId;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    // Setter Methods (Jika diperlukan)
    public void setName(String name) {
        this.name = name;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setWisataId(int wisataId) {
        this.wisataId = wisataId;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
