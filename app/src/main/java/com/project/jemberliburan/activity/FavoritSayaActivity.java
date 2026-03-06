package com.project.jemberliburan.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.jemberliburan.R;
import com.project.jemberliburan.adapter.DestinasiAdapter;
import com.project.jemberliburan.model.Destinasi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoritSayaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavorit;
    private List<Destinasi> favoriteList;
    private DestinasiAdapter destinasiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorit_saya);
        ImageView backButton = findViewById(R.id.icon_back);

        // Tambahkan listener untuk icon_back
        backButton.setOnClickListener(v -> finish());

        // Inisialisasi RecyclerView dan List
        recyclerViewFavorit = findViewById(R.id.recyclerViewFavorites);
        favoriteList = new ArrayList<>();

        // Load data favorit dari SharedPreferences
        loadFavorites();

        recyclerViewFavorit.setLayoutManager(new GridLayoutManager(this, 2));

        // Setup RecyclerView
        destinasiAdapter = new DestinasiAdapter(this, favoriteList);
        recyclerViewFavorit.setAdapter(destinasiAdapter);
    }

    private void loadFavorites() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        Gson gson = new Gson();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getValue() instanceof String) {
                String json = (String) entry.getValue();
                Type destinasiType = new TypeToken<Destinasi>() {}.getType();
                Destinasi destinasi = gson.fromJson(json, destinasiType);

                if (destinasi != null) {
                    favoriteList.add(destinasi);
                } else {
                    Log.e("FavoritSayaActivity", "Gagal parsing JSON: " + json);
                }
            }
        }
    }
}
