package com.project.jemberliburan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.jemberliburan.R;
import com.project.jemberliburan.model.Ticket;
import java.util.ArrayList;
import java.util.List;

public class TiketAdapter extends RecyclerView.Adapter<TiketAdapter.TiketViewHolder> {

    private Context context;
    private List<Ticket> ticketList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor Adapter
    public TiketAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.ticketList = generateDummyData(); // Panggil data dummy
    }

    @NonNull
    @Override
    public TiketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tiket, parent, false);
        return new TiketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TiketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);

        // Set data ke tampilan item
        holder.imageViewIcon.setImageResource(ticket.getImageResId());
        holder.textViewTitle.setText(ticket.getTitle());
        holder.textViewDescription.setText(ticket.getDescription());

        // Klik pada item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    // ViewHolder
    static class TiketViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewIcon;
        TextView textViewTitle, textViewDescription;

        public TiketViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }

    // Data Dummy
    private List<Ticket> generateDummyData() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket(R.drawable.icon_cektiket, "Cek Tiket", "Ayo Cek Tiketmu", 1));
        tickets.add(new Ticket(R.drawable.icon_riwayattiket, "Riwayat Tiket", "Cek Perjalananmu", 2));
        return tickets;
    }
}

