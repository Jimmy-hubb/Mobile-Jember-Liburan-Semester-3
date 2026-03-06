package com.project.jemberliburan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.jemberliburan.R;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {

    // Daftar icon tip
    private final int[] tipIcons = {
            R.drawable.img_tipstrip1,
            R.drawable.img_tipstrip2,
            R.drawable.img_tipstrip3
    };

    private final OnTipClickListener clickListener;

    // Listener untuk klik item tip
    public interface OnTipClickListener {
        void onTipClick(int position);
    }

    // Constructor
    public TipAdapter(OnTipClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_tip
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        // Bind data ke ViewHolder
        holder.bind(tipIcons[position], position);
    }

    @Override
    public int getItemCount() {
        return tipIcons.length;
    }

    class TipViewHolder extends RecyclerView.ViewHolder {
        private final ImageView tipIconImageView;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tipIconImageView = itemView.findViewById(R.id.item_icon); // Pastikan ID sama di item_tip.xml
        }

        public void bind(int tipIcon, int position) {
            tipIconImageView.setImageResource(tipIcon);
            tipIconImageView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onTipClick(position);
                }
            });
        }
    }
}
