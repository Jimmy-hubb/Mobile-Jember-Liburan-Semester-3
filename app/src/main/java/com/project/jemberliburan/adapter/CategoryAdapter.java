package com.project.jemberliburan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.jemberliburan.R;
import com.project.jemberliburan.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private OnCategoryClickListener listener;

    // Interface untuk mendeteksi klik pada kategori
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    // Constructor adapter
    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_category.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        // Atur visibilitas elemen
        if ("topdestinasi".equals(category.getName())) {
            // Top Destinasi: hanya tampilkan ikon, sembunyikan teks
            holder.categoryIcon.setVisibility(View.VISIBLE);
            holder.categoryIcon.setImageResource(category.getIconResId());
            holder.categoryName.setVisibility(View.GONE); // Sembunyikan nama
        } else {
            // Kategori lainnya: tampilkan teks dan ikon
            holder.categoryName.setVisibility(View.VISIBLE);
            holder.categoryName.setText(category.getName());
            holder.categoryIcon.setVisibility(View.VISIBLE);
            holder.categoryIcon.setImageResource(category.getIconResId());
        }

        // Ubah warna background dan elemen berdasarkan status isSelected
        if (category.isSelected()) {
            // Saat dipilih: background menjadi primary_color, teks dan ikon menjadi putih
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.primary_colour));
            holder.categoryName.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
            holder.categoryIcon.setColorFilter(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        } else {
            // Saat tidak dipilih: background menjadi putih, teks dan ikon menjadi default
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
            holder.categoryName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.font_colour));
            holder.categoryIcon.setColorFilter(null); // Hapus filter warna
        }

        // Listener untuk klik item
        holder.itemView.setOnClickListener(v -> {
            // Reset semua kategori lainnya agar tidak dipilih
            for (Category cat : categoryList) {
                cat.setSelected(false);
            }
            // Tandai kategori ini sebagai terpilih
            category.setSelected(true);
            // Notifikasi perubahan
            notifyDataSetChanged();
            // Beritahu listener klik
            listener.onCategoryClick(category);
        });
    }



    @Override
    public int getItemCount() {
        // Kembalikan jumlah kategori dalam list
        return categoryList.size();
    }

    // ViewHolder untuk memegang referensi komponen UI
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryIcon;
        CardView cardView; // Tambahkan referensi CardView

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.CategoryCardView); // Referensi CardView
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
        }
    }
}
