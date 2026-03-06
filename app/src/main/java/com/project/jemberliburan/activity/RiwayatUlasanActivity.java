package com.project.jemberliburan.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.project.jemberliburan.R;
import com.project.jemberliburan.adapter.UlasanAdapter;
import com.project.jemberliburan.connection.Db_Contract;
import com.project.jemberliburan.connection.MySingleton;
import com.project.jemberliburan.model.Ulasan;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RiwayatUlasanActivity extends AppCompatActivity {
    private static final String TAG = "RiwayatUlasanActivity";

    private RecyclerView recyclerView;
    private UlasanAdapter ulasanAdapter;
    private List<Ulasan> ulasanList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_ulasan);

        // Inisialisasi komponen
        recyclerView = findViewById(R.id.recyclerViewRiwayatUlasan);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        boolean showNamaDestinasi = true; // Tampilkan nama destinasi di halaman riwayat ulasan
        boolean disableClick = false; // Aktifkan klik untuk halaman riwayat

// Inisialisasi RecyclerView
        ulasanList = new ArrayList<>();
        ulasanAdapter = new UlasanAdapter(this, ulasanList, showNamaDestinasi, disableClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ulasanAdapter);



        // Ambil data riwayat ulasan
        fetchRiwayatUlasan();

        // Tambahkan aksi refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchRiwayatUlasan();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void fetchRiwayatUlasan() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "User tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String url = Db_Contract.urlGetReviewsByUser + "?user_id=" + userId;

        Log.d(TAG, "Fetching riwayat ulasan from URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Log.d(TAG, "Response JSON: " + response);

                        Gson gson = new GsonBuilder().setLenient().create();
                        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

                        // Validasi status respons API
                        String status = jsonResponse.get("status").getAsString();
                        if ("success".equals(status)) {
                            // Parsing data ulasan
                            Type ulasanListType = new TypeToken<List<Ulasan>>() {}.getType();
                            List<Ulasan> fetchedUlasan = gson.fromJson(jsonResponse.get("data"), ulasanListType);

                            // Clear list dan tambahkan data baru
                            ulasanList.clear();
                            ulasanList.addAll(fetchedUlasan);

                            ulasanAdapter.notifyDataSetChanged();
                        } else {
                            String message = jsonResponse.get("message").getAsString();
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "JSON Parsing Error: ", e);
                        Toast.makeText(this, "Kesalahan saat memproses data ulasan.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley Error: " + error.toString());
                    Toast.makeText(RiwayatUlasanActivity.this, "Gagal mengambil data riwayat ulasan.", Toast.LENGTH_SHORT).show();
                });

        // Tambahkan request ke antrian Volley
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    }
