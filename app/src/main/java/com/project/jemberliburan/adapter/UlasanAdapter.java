// UlasanAdapter.java
package com.project.jemberliburan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.jemberliburan.R;
import com.project.jemberliburan.activity.UlasanActivity;
import com.project.jemberliburan.model.Ulasan;

import java.util.ArrayList;
import java.util.List;

public class UlasanAdapter extends RecyclerView.Adapter<UlasanAdapter.ViewHolder> {
    private final Context context;
    private List<Ulasan> ulasanList;
    private boolean showNamaDestinasi;
    private boolean disableClick;

    public UlasanAdapter(Context context, List<Ulasan> ulasanList, boolean showNamaDestinasi, boolean disableClick) {
        this.context = context;
        this.ulasanList = (ulasanList != null) ? ulasanList : new ArrayList<>();
        this.showNamaDestinasi = showNamaDestinasi;
        this.disableClick = disableClick;
    }


    // Metode untuk mengupdate daftar ulasan
    public void setUlasanList(List<Ulasan> ulasanList) {
        this.ulasanList = ulasanList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ulasan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ulasan ulasan = ulasanList.get(position);

        // Set nama destinasi
        holder.namaDestinasi.setText(ulasan.getNamaDestinasi() != null ? ulasan.getNamaDestinasi() : "Nama destinasi tidak tersedia");

        // Set nama pengguna, rating, komentar, dll.
        holder.userName.setText(ulasan.getNamaUser() != null ? ulasan.getNamaUser() : "Pengguna Tidak Dikenal");
        holder.rating.setText("\u2B50 " + ulasan.getRating());
        holder.comment.setText(ulasan.getKomentar());
        holder.date.setText(ulasan.getTanggalUlasan() != null ? ulasan.getTanggalUlasan() : "Tanggal Tidak Tersedia");

        // Tampilkan atau sembunyikan nama destinasi berdasarkan showNamaDestinasi
        if (showNamaDestinasi) {
            holder.namaDestinasi.setVisibility(View.VISIBLE);
            holder.namaDestinasi.setText(ulasan.getNamaDestinasi());
        } else {
            holder.namaDestinasi.setVisibility(View.GONE);
        }

        // Atur apakah item bisa diklik atau tidak berdasarkan disableClick
        if (!disableClick) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, UlasanActivity.class);
                intent.putExtra("wisata_id", ulasan.getId()); // Kirim ID ulasan
                context.startActivity(intent);
            });
            holder.itemView.setClickable(true); // Pastikan elemen dapat diklik jika disableClick = false
        } else {
            holder.itemView.setOnClickListener(null); // Hilangkan listener
            holder.itemView.setClickable(false); // Pastikan elemen tidak dapat diklik
        }
        // Set foto profil
        if (ulasan.getFotoProfil() != null && !ulasan.getFotoProfil().isEmpty()) {
            Glide.with(context)
                    .load(ulasan.getFotoProfil())
                    .placeholder(R.drawable.placeholder_profile)
                    .error(R.drawable.placeholder_profile)
                    .circleCrop()
                    .into(holder.profileImage);
        } else {
            holder.profileImage.setImageResource(R.drawable.placeholder_profile);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UlasanActivity.class);
            intent.putExtra("wisata_id", ulasan.getWisataId()); // Kirim wisata_id
            intent.putExtra("disable_click", true); // Kirim flag untuk menonaktifkan klik
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return (ulasanList != null) ? ulasanList.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, rating, comment, date, namaDestinasi; // Tambahkan namaDestinasi
        CardView cardView;
        ImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardviewUlasan);
            userName = itemView.findViewById(R.id.textViewUsername);
            rating = itemView.findViewById(R.id.textViewRating);
            comment = itemView.findViewById(R.id.textViewComment);
            date = itemView.findViewById(R.id.textViewDate);
            namaDestinasi = itemView.findViewById(R.id.textViewNamaDestinasi); // Inisialisasi namaDestinasi
            profileImage = itemView.findViewById(R.id.imageViewProfile);
        }
    }

}
