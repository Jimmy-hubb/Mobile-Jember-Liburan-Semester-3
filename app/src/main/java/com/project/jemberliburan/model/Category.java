package com.project.jemberliburan.model;

import java.util.Objects;


public class Category {
    private String name;         // Nama kategori
    private int iconResId;       // Resource ID untuk ikon
    private boolean showIcon;    // Apakah ikon harus ditampilkan
    private boolean isSelected;  // Status apakah kategori dipilih

    public Category(String name, int iconResId, boolean showIcon) {
        this.name = name;
        this.iconResId = iconResId;
        this.showIcon = showIcon;
        this.isSelected = false; // Default tidak dipilih
    }

    public Category() {
        // Default values
        this.name = "";
        this.iconResId = 0;
        this.showIcon = false;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getIconResId() {
        return iconResId;
    }


    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
    }


    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", iconResId=" + iconResId +
                ", showIcon=" + showIcon +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return iconResId == category.iconResId &&
                Objects.equals(name, category.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, iconResId);
    }
}
