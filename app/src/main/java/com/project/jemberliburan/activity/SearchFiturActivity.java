package com.project.jemberliburan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.jemberliburan.R;
import com.project.jemberliburan.adapter.SearchFiturAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFiturActivity extends AppCompatActivity {
    private EditText searchInput;
    private RecyclerView recyclerViewSearchResults;
    private SearchFiturAdapter searchFiturAdapter;
    private List<String> allFeatures; // Semua fitur untuk pencarian
    private List<String> filteredFeatures; // Fitur yang sudah difilter
    private ImageView icon_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_fitur);

        // Inisialisasi View
        searchInput = findViewById(R.id.searchInput);
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);
        ImageView backButton = findViewById(R.id.icon_back);

        // Tambahkan listener untuk icon_back
        backButton.setOnClickListener(v -> finish());

        // Daftar semua fitur (bisa diambil dari database, API, dll.)
        allFeatures = getAllFeatures();

        // Filter awal (tampilkan semua)
        filteredFeatures = new ArrayList<>(allFeatures);

        // Setup adapter dan RecyclerView
        searchFiturAdapter = new SearchFiturAdapter(filteredFeatures, this::onFeatureClicked);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setAdapter(searchFiturAdapter);

        // Tangkap data fitur dari intent
        String featureName = getIntent().getStringExtra("feature_name");
        if (featureName != null && !featureName.isEmpty()) {
            searchInput.setText(featureName);
            filterResults(featureName);
        }

        // Tambahkan listener untuk pencarian dinamis
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak diperlukan saat ini
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterResults(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak diperlukan saat ini
            }
        });
    }

    private void filterResults(String query) {
        filteredFeatures = allFeatures.stream()
                .filter(feature -> feature.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        searchFiturAdapter.updateData(filteredFeatures); // Memperbarui data di adapter
    }

    private List<String> getAllFeatures() {
        // Contoh daftar fitur (nanti bisa diganti dengan data dinamis)
        return Arrays.asList(
                "Jelajahi Destinasi",
                "Tentang Saya",
                "Cek Tiket",
                "Riwayat",
                "Ulasan",
                "Bantuan"
        );
    }

    private void onFeatureClicked(String feature) {
        Intent intent;

        switch (feature) {
            case "Jelajahi Destinasi":
                intent = new Intent(this, BantuanActivity.class);
                break;
            case "Tentang Saya":
                intent = new Intent(this, TentangSayaActivity.class);
                break;
            case "Cek Tiket":
                intent = new Intent(this, BantuanActivity.class);
                break;
            case "Riwayat":
                intent = new Intent(this, UlasanActivity.class);
                break;
            case "Ulasan":
                intent = new Intent(this, UlasanActivity.class);
                break;
            case "Bantuan":
                intent = new Intent(this, BantuanActivity.class);
                break;
            default:
                Toast.makeText(this, "Fitur belum tersedia", Toast.LENGTH_SHORT).show();
                return; // Jangan lanjutkan jika tidak ada mapping
        }

        // Mulai activity
        startActivity(intent);
    }
}
