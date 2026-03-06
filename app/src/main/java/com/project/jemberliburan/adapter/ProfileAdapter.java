package com.project.jemberliburan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.jemberliburan.R;
import com.project.jemberliburan.model.Profile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private Context context;
    private List<Profile> profileList;
    private OnItemClickListener listener; // Listener untuk menangani klik

    // Interface untuk menangani klik item
    public interface OnItemClickListener {
        void onItemClick(Profile profile);
    }

    // Konstruktor
    public ProfileAdapter(Context context, List<Profile> profileList, OnItemClickListener listener) {
        this.context = context;
        this.profileList = profileList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile item = profileList.get(position);

        // Set data ke ViewHolder
        holder.imageViewIcon.setImageResource(item.getIconResId());
        holder.textViewTitle.setText(item.getTitle());
        holder.imageViewAction.setImageResource(item.getActionResId());

        // Menetapkan klik listener pada item
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewTitle;
        ImageView imageViewAction;
        CardView cardView;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewProfileItem);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageViewAction = itemView.findViewById(R.id.imageViewAction);
        }
    }
}
