package com.project.jemberliburan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.jemberliburan.R;
import com.project.jemberliburan.adapter.UlasanAdapter;
import com.project.jemberliburan.connection.Db_Contract;
import com.project.jemberliburan.model.Ulasan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DetailDestinasiActivity extends AppCompatActivity {

    private static final String TAG = "DetailDestinasiActivity";

    private boolean isFavorited = false; // Status favorit
    private SharedPreferences sharedPreferences;

    private String name;
    private int imageResId;
    private String address;
    private double rating;
    private int wisataId;
    private String deskripsi;

    // RecyclerView dan Adapter untuk ulasan
    private RecyclerView recyclerViewReviews;
    private UlasanAdapter ulasanAdapter;
    private TextView textViewLihatSelengkapnya;

    // RequestQueue untuk Volley
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_destinasi);

        // Inisialisasi Views
        ImageView imgFavoritSaya = findViewById(R.id.img_favoritsaya);
        ImageView imageView = findViewById(R.id.imageViewDetail);
        TextView textViewName = findViewById(R.id.textViewNameDetail);
        TextView textViewLocation = findViewById(R.id.textViewLocationDetail);
        TextView textViewRating = findViewById(R.id.textViewRatingDetail);
        TextView textViewDeskripsi = findViewById(R.id.textViewDeskripsiDetail);
        ImageView backButton = findViewById(R.id.icon_back);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        textViewLihatSelengkapnya = findViewById(R.id.textViewLihatSelengkapnya);

        // Inisialisasi RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Ambil data dari Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        imageResId = intent.getIntExtra("imageResId", R.drawable.img_default);
        address = intent.getStringExtra("address");
        rating = intent.getDoubleExtra("rating", 0.0);
        wisataId = intent.getIntExtra("wisata_id", -1); // Default nilai -1
        deskripsi = intent.getStringExtra("deskripsi");

        // Validasi ID Destinasi
        if (wisataId == -1) {
            Log.e(TAG, "Wisata ID tidak valid.");
            Toast.makeText(this, "Data destinasi tidak valid.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Log untuk debug
        Log.d(TAG, "Wisata ID diterima: " + wisataId);

        // Set data ke Views
        textViewName.setText(name);
        imageView.setImageResource(imageResId);
        textViewLocation.setText(address);
        textViewRating.setText("Rating: " + rating);
        textViewDeskripsi.setText(deskripsi != null ? deskripsi : "Deskripsi tidak tersedia.");

        // Tentukan apakah nama destinasi akan ditampilkan (false untuk DetailDestinasiActivity)
        boolean showNamaDestinasi = false;
        boolean disableClick = true;

        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        ulasanAdapter = new UlasanAdapter(this, new ArrayList<>(), showNamaDestinasi, disableClick);
        recyclerViewReviews.setAdapter(ulasanAdapter);


        // Muat ulasan dari API
        fetchUlasan();

        // Listener untuk tombol "Lihat Selengkapnya"
        textViewLihatSelengkapnya.setOnClickListener(v -> {
            Intent ulasanIntent = new Intent(DetailDestinasiActivity.this, UlasanActivity.class);
            ulasanIntent.putExtra("wisata_id", wisataId);
            startActivity(ulasanIntent);
        });

        // Listener untuk tombol back
        backButton.setOnClickListener(v -> finish());

        // Inisialisasi SharedPreferences untuk favorit
        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        isFavorited = sharedPreferences.contains(name);
        updateFavoriteIcon(imgFavoritSaya);

        // Listener untuk tombol favorit
        imgFavoritSaya.setOnClickListener(v -> {
            if (isFavorited) {
                removeFromFavorites(name);
            } else {
                saveToFavorites(name, imageResId, address, rating);
            }
            isFavorited = !isFavorited;
            updateFavoriteIcon(imgFavoritSaya);

            String message = isFavorited ? "Ditambahkan ke Favorit" : "Dihapus dari Favorit";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void saveToFavorites(String name, int imageResId, String address, double rating) {
        Gson gson = new Gson();
        FavoriteItem favorite = new FavoriteItem(name, imageResId, address, rating);

        String json = gson.toJson(favorite);
        sharedPreferences.edit().putString(name, json).apply();
    }

    private void removeFromFavorites(String name) {
        sharedPreferences.edit().remove(name).apply();
    }

    private void updateFavoriteIcon(ImageView imgFavoritSaya) {
        int color = isFavorited ? getResources().getColor(R.color.primary_colour) : getResources().getColor(R.color.font_colour);
        imgFavoritSaya.setColorFilter(color);
    }

    private void fetchUlasan() {
        String url = Db_Contract.urlGetReviews + "?wisata_id=" + wisataId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d(TAG, "Response JSON: " + response);

                        String status = response.getString("status");
                        if ("success".equals(status)) {
                            JSONArray data = response.getJSONArray("data");
                            List<Ulasan> allUlasan = new ArrayList<>();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                Ulasan ulasan = new Ulasan(
                                        obj.optInt("id"),
                                        obj.optInt("user_id"),
                                        obj.optString("nama_user"),
                                        obj.optString("foto_profil"),
                                        obj.optInt("rating"),
                                        obj.optString("komentar"),
                                        obj.optString("tanggal_ulasan"),
                                        obj.optString("namaDestinasi"),
                                        obj.optInt("wisata_id")

                                );
                                allUlasan.add(ulasan);
                            }

                            List<Ulasan> randomUlasan = getRandomUlasan(allUlasan, 2);
                            ulasanAdapter.setUlasanList(randomUlasan);
                        } else {
                            Log.e(TAG, "Error: " + response.getString("message"));
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing Error: ", e);
                        Toast.makeText(this, "Kesalahan parsing data ulasan.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley Error: " + error.getMessage());
                    Toast.makeText(this, "Gagal mengambil data ulasan.", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    private List<Ulasan> getRandomUlasan(List<Ulasan> allUlasan, int count) {
        List<Ulasan> randomUlasan = new ArrayList<>();
        Random random = new Random();
        while (randomUlasan.size() < count && !allUlasan.isEmpty()) {
            int index = random.nextInt(allUlasan.size());
            Ulasan ulasan = allUlasan.get(index);
            if (!randomUlasan.contains(ulasan)) {
                randomUlasan.add(ulasan);
            }
        }
        return randomUlasan;
    }

    private static class FavoriteItem {
        String name;
        int imageResId;
        String address;
        double rating;

        public FavoriteItem(String name, int imageResId, String address, double rating) {
            this.name = name;
            this.imageResId = imageResId;
            this.address = address;
            this.rating = rating;
        }
    }
}
