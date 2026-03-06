package com.project.jemberliburan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class UlasanActivity extends AppCompatActivity {
    private static final String TAG = "UlasanActivity";

    private RecyclerView recyclerViewUlasan;
    private UlasanAdapter ulasanAdapter;
    private List<Ulasan> ulasanList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int wisataId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulasan);


        wisataId = getIntent().getIntExtra("wisata_id", -1);
        Log.d(TAG, "Wisata ID diterima: " + wisataId);

        // Validasi ID destinasi
        if (wisataId == -1) {
            Toast.makeText(this, "ID Destinasi tidak valid", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ID Destinasi tidak valid, aktivitas akan ditutup.");
            finish();
            return;
        }

        // Inisialisasi Views
        recyclerViewUlasan = findViewById(R.id.recyclerViewUlasan);
        Button buttonTambahUlasan = findViewById(R.id.buttonTambahUlasan);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Tentukan apakah nama destinasi akan ditampilkan
        boolean showNamaDestinasi = wisataId == -1;
        boolean disableClick = true;

        ulasanList = new ArrayList<>(); // Pastikan ulasanList diinisialisasi
        ulasanAdapter = new UlasanAdapter(this, ulasanList, showNamaDestinasi, disableClick); // Parameter tambahan
        recyclerViewUlasan.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUlasan.setAdapter(ulasanAdapter);

        // Ambil ulasan dari server
        fetchUlasan();

        // Tambahkan aksi untuk tombol tambah ulasan
        buttonTambahUlasan.setOnClickListener(v -> {
            Intent intent = new Intent(UlasanActivity.this, PostUlasanActivity.class);
            intent.putExtra("wisata_id", wisataId); // Kirim wisata_id ke PostUlasanActivity
            startActivityForResult(intent, 100);
        });

        // Tambahkan aksi untuk SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d(TAG, "Swipe to refresh triggered.");
            fetchUlasan();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Log.d(TAG, "Ulasan berhasil ditambahkan, memuat ulang ulasan.");
            fetchUlasan();
        }
    }

    private void fetchUlasan() {
        String url = Db_Contract.urlGetReviews + "?wisata_id=" + wisataId;

        Log.d(TAG, "Fetching ulasan from URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Parsing JSON
                        Gson gson = new GsonBuilder().setLenient().create();
                        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                        String status = jsonResponse.get("status").getAsString();

                        if ("success".equals(status)) {
                            Type ulasanListType = new TypeToken<List<Ulasan>>() {
                            }.getType();
                            List<Ulasan> ulasanlist = gson.fromJson(jsonResponse.get("data"), ulasanListType);

                            if (ulasanList == null) {
                                ulasanList = new ArrayList<>();
                            }
                            ulasanList.clear();
                            ulasanList.addAll(ulasanlist);

                            // Refresh RecyclerView
                            ulasanAdapter.notifyDataSetChanged();
                            Log.d(TAG, "Total ulasan diterima: " + ulasanlist.size());
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
                    Toast.makeText(UlasanActivity.this, "Gagal mengambil data ulasan.", Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
