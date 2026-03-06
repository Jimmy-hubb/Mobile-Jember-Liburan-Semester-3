package com.project.jemberliburan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.project.jemberliburan.model.Wellcome;
import com.project.jemberliburan.R;
import com.project.jemberliburan.activity.LoginActivity;

import java.util.List;

public class WellcomeSliderAdapter extends RecyclerView.Adapter<WellcomeSliderAdapter.WellcomeViewHolder> {

    private Context context;
    private List<Wellcome> wellcomeItems;
    private ViewPager2 viewPager; // Referensi ViewPager2

    public WellcomeSliderAdapter(Context context, List<Wellcome> wellcomeItems, ViewPager2 viewPager) {
        this.context = context;
        this.wellcomeItems = wellcomeItems;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public WellcomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wellcome, parent, false);
        return new WellcomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WellcomeViewHolder holder, int position) {
        Wellcome item = wellcomeItems.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
        holder.mainImageView.setImageResource(item.getImageResource());
        holder.swipeImageView.setImageResource(item.getSwipeImageResource());

        // Menangani klik pada swipeImageView berdasarkan posisi
        holder.swipeImageView.setOnClickListener(v -> {
            if (position < wellcomeItems.size() - 1) {
                // Berpindah ke halaman berikutnya
                viewPager.setCurrentItem(position + 1, true);
            } else {
                // Berpindah ke LoginActivity jika ini adalah halaman terakhir
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);

                // Opsional: Hentikan WellcomeActivity agar tidak kembali ke sini
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wellcomeItems.size();
    }

    public static class WellcomeViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, descriptionTextView;
        ImageView mainImageView, swipeImageView;

        public WellcomeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            mainImageView = itemView.findViewById(R.id.mainImageView);
            swipeImageView = itemView.findViewById(R.id.swipeImageView);
        }
    }
}
