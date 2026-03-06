package com.project.jemberliburan.model;

public class Profile {
    private int iconResId;  // ID resource ikon
    private String title;   // Judul profil
    private int actionResId; // ID resource ikon tindakan (misalnya panah)

    public Profile(int iconResId, String title, int actionResId) {
        this.iconResId = iconResId;
        this.title = title;
        this.actionResId = actionResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getActionResId() {
        return actionResId;
    }
}
