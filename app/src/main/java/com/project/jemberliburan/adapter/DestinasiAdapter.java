package com.project.jemberliburan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.jemberliburan.R;
import com.project.jemberliburan.activity.DetailDestinasiActivity;
import com.project.jemberliburan.model.Destinasi;

import java.util.List;

public class DestinasiAdapter extends RecyclerView.Adapter<DestinasiAdapter.ViewHolder> {

    private Context context;
    private List<Destinasi> destinasiList;

    public DestinasiAdapter(Context context, List<Destinasi> destinasiList) {
        this.context = context;
        this.destinasiList = destinasiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destinasi, parent, false);
        return new ViewHolder(view);
    }

    public void updateList(List<Destinasi> newList) {
        this.destinasiList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destinasi item = destinasiList.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.nameTextView.setText(item.getName());
        holder.addressTextView.setText(item.getAddress());
        holder.ratingTextView.setText("⭐ " + item.getRating());

        // Set klik listener untuk card
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailDestinasiActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("imageResId", item.getImageResId());
            intent.putExtra("address", item.getAddress());
            intent.putExtra("rating", item.getRating());
            intent.putExtra("wisata_id", item.getWisataId());
            intent.putExtra("deskripsi", item.getDeskripsi());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return destinasiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView addressTextView;
        TextView ratingTextView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.destinasiImage);
            nameTextView = itemView.findViewById(R.id.destinasiName);
            addressTextView = itemView.findViewById(R.id.destinasiAddress);
            ratingTextView = itemView.findViewById(R.id.destinasiRating);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
