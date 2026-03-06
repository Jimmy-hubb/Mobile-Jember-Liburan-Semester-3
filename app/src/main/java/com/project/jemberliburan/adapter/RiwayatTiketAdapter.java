package com.project.jemberliburan.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.jemberliburan.model.RiwayatTiket;
import com.project.jemberliburan.R;

import java.util.List;

public class RiwayatTiketAdapter extends RecyclerView.Adapter<RiwayatTiketAdapter.RiwayatTiketViewHolder> {

    private Context context;
    private List<RiwayatTiket> riwayatTiketList;

    public RiwayatTiketAdapter(Context context, List<RiwayatTiket> riwayatTiketList) {
        this.context = context;
        this.riwayatTiketList = riwayatTiketList;
    }

    @NonNull
    @Override
    public RiwayatTiketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_riwayat_tiket, parent, false);
        return new RiwayatTiketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatTiketViewHolder holder, int position) {
        RiwayatTiket riwayatTiket = riwayatTiketList.get(position);

        // Menetapkan data ke TextViews tanpa menambahkan label
        holder.textViewOrderId.setText(riwayatTiket.getOrderId());
        holder.textViewDestinasi.setText(riwayatTiket.getDestinasi());
        holder.textViewEmail.setText(riwayatTiket.getEmail());
        holder.textViewTanggal.setText(riwayatTiket.getTanggalKunjungan());
        holder.textViewJumlahTiket.setText(String.valueOf(riwayatTiket.getJumlahTiket()));
        holder.textViewTotalBayar.setText("Rp. " + riwayatTiket.getTotalBayar());
        holder.textViewStatusTiket.setText(riwayatTiket.getStatusTiket());

        // Menentukan warna indikator dan teks berdasarkan status tiket
        String status = riwayatTiket.getStatusTiket();
        switch (status.toLowerCase()) {
            case "belum dibayar":
                holder.viewStatusIndicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.status_unpaid));
                holder.textViewStatusTiket.setTextColor(ContextCompat.getColor(context, R.color.status_unpaid));
                break;
            case "transaksi sukses":
                holder.viewStatusIndicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.status_paid));
                holder.textViewStatusTiket.setTextColor(ContextCompat.getColor(context, R.color.status_paid));
                break;
            case "pending":
                holder.viewStatusIndicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.status_pending));
                holder.textViewStatusTiket.setTextColor(ContextCompat.getColor(context, R.color.status_pending));
                break;
            default:
                // Warna default untuk status lainnya
                holder.viewStatusIndicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white));
                holder.textViewStatusTiket.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
        }

        // Menentukan apakah tombol "Bayar" ditampilkan
        if (status.equalsIgnoreCase("Belum Dibayar") && riwayatTiket.getPaymentUrl() != null) {
            holder.buttonBayar.setVisibility(View.VISIBLE);
            holder.buttonBayar.setOnClickListener(v -> {
                // Membuka URL pembayaran di browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(riwayatTiket.getPaymentUrl()));
                context.startActivity(browserIntent);
            });
        } else {
            holder.buttonBayar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return riwayatTiketList.size();
    }

    public static class RiwayatTiketViewHolder extends RecyclerView.ViewHolder {

        TextView textViewJudul;
        TextView textViewOrderId, textViewDestinasi, textViewEmail, textViewTanggal, textViewJumlahTiket, textViewTotalBayar, textViewStatusTiket;
        Button buttonBayar; // Tombol Bayar
        View viewStatusIndicator; // Indikator Warna

        public RiwayatTiketViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewJudul = itemView.findViewById(R.id.textViewJudul);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewDestinasi = itemView.findViewById(R.id.textViewDestinasi);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewTanggal = itemView.findViewById(R.id.textViewTanggal);
            textViewJumlahTiket = itemView.findViewById(R.id.textViewJumlahTiket);
            textViewTotalBayar = itemView.findViewById(R.id.textViewTotalBayar);
            textViewStatusTiket = itemView.findViewById(R.id.textViewStatusTiket);
            buttonBayar = itemView.findViewById(R.id.buttonBayar); // Tombol Bayar
            viewStatusIndicator = itemView.findViewById(R.id.viewStatusIndicator); // Indikator Warna
        }
    }
}
