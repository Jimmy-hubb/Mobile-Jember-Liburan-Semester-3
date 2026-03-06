package com.project.jemberliburan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.jemberliburan.R;

public class FiturAdapter extends RecyclerView.Adapter<FiturAdapter.FiturViewHolder> {

    // Daftar ikon fitur
    private final int[] fiturIcons = {
            R.drawable.icon_home_jelajahi,
            R.drawable.icon_home_saya,
            R.drawable.icon_home_cektiket,
            R.drawable.icon_home_riwayat,
            R.drawable.icon_home_ulasan,
            R.drawable.icon_home_bantuan
    };

    private final OnFiturClickListener clickListener;

    // Listener klik fitur
    public interface OnFiturClickListener {
        void onFiturClick(int position);
    }

    // Constructor
    public FiturAdapter(OnFiturClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FiturViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_fitur
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fitur, parent, false);
        return new FiturViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiturViewHolder holder, int position) {
        // Bind data ke ViewHolder
        holder.bind(fiturIcons[position], position);
    }

    @Override
    public int getItemCount() {
        return fiturIcons.length;
    }

    class FiturViewHolder extends RecyclerView.ViewHolder {
        private final ImageView fiturIconImageView;

        public FiturViewHolder(@NonNull View itemView) {
            super(itemView);
            fiturIconImageView = itemView.findViewById(R.id.item_icon); // Pastikan ID sesuai
        }

        public void bind(int fiturIcon, int position) {
            fiturIconImageView.setImageResource(fiturIcon);
            fiturIconImageView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onFiturClick(position);
                }
            });
        }
    }
}
