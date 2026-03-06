package com.project.jemberliburan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.jemberliburan.R;
import java.util.List;

public class SearchFiturAdapter extends RecyclerView.Adapter<SearchFiturAdapter.ViewHolder> {
    private List<String> searchResults;
    private OnItemClickListener onItemClickListener;

    // Constructor
    public SearchFiturAdapter(List<String> searchResults, OnItemClickListener onItemClickListener) {
        this.searchResults = searchResults;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_search_fitur_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String result = searchResults.get(position);
        holder.textView.setText(result);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(result));
    }

    @Override
    public int getItemCount() {
        return searchResults != null ? searchResults.size() : 0;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.searchResultText);
        }
    }

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public void updateData(List<String> newSearchResults) {
        this.searchResults = newSearchResults;
        notifyDataSetChanged(); // Memastikan RecyclerView memperbarui tampilan
    }

}
