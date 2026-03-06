package com.project.jemberliburan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// Import library lainnya
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.jemberliburan.model.RiwayatTiket;
import com.project.jemberliburan.R;
import com.project.jemberliburan.adapter.RiwayatTiketAdapter;
import com.project.jemberliburan.connection.Db_Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CekTiketActivity extends AppCompatActivity {

    private EditText editTextCari;
    private Button buttonCekTiket;
    private RecyclerView recyclerViewTiket;
    private TextView textViewPesan;
    private ProgressBar progressBarLoading; // Tambahkan ini

    private RiwayatTiketAdapter adapter;
    private ArrayList<RiwayatTiket> tiketList;

    private String userEmail; // Variabel untuk menyimpan email pengguna

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_tiket);

        // Inisialisasi UI Components
        editTextCari = findViewById(R.id.editTextCari);
        buttonCekTiket = findViewById(R.id.buttonCekTiket);
        recyclerViewTiket = findViewById(R.id.recyclerViewTiket);
        textViewPesan = findViewById(R.id.textViewPesan);
        progressBarLoading = findViewById(R.id.progressBar); // Inisialisasi ProgressBar

        // Setup RecyclerView
        recyclerViewTiket.setLayoutManager(new LinearLayoutManager(this));
        tiketList = new ArrayList<>();
        adapter = new RiwayatTiketAdapter(this, tiketList);
        recyclerViewTiket.setAdapter(adapter);

        // Ambil email dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        userEmail = preferences.getString("email", null);

        if (userEmail == null) {
            // Jika email tidak ditemukan, arahkan pengguna ke login
            Toast.makeText(this, "Anda perlu login terlebih dahulu.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CekTiketActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Tambahkan logging untuk memverifikasi email
        Log.d("CekTiketActivity", "User Email: " + userEmail);

        // Listener untuk tombol cek tiket
        buttonCekTiket.setOnClickListener(v -> {
            String query = editTextCari.getText().toString().trim();
            if (!query.isEmpty()) {
                // Tentukan apakah input adalah order_id atau destinasi berdasarkan prefix
                String jenisCari;
                if (query.startsWith("order-")) {
                    jenisCari = "order_id";
                } else {
                    jenisCari = "destinasi";
                }
                fetchTiket(query, jenisCari);
            } else {
                Toast.makeText(CekTiketActivity.this, "Masukkan Order ID atau Destinasi.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fungsi untuk memeriksa apakah string adalah angka (numeric).
     *
     * @param str String yang akan diperiksa.
     * @return true jika string adalah angka, false jika tidak.
     */
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("\\d+");
    }

    private void fetchTiket(String query, String jenisCari) {
        // Menonaktifkan tombol dan menyembunyikan pesan
        buttonCekTiket.setEnabled(false);
        textViewPesan.setVisibility(View.GONE);
        recyclerViewTiket.setVisibility(View.GONE); // Sembunyikan RecyclerView saat loading
        tiketList.clear();
        adapter.notifyDataSetChanged();

        // Tampilkan ProgressBar
        progressBarLoading.setVisibility(View.VISIBLE);

        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Terjadi kesalahan dalam encoding query.", Toast.LENGTH_SHORT).show();
            buttonCekTiket.setEnabled(true);
            progressBarLoading.setVisibility(View.GONE); // Sembunyikan ProgressBar
            return;
        }

        // Membuat URL dengan parameter yang sesuai
        String url = Db_Contract.urlCekTiket;
        try {
            String encodedEmail = URLEncoder.encode(userEmail, "UTF-8");
            if (jenisCari.equals("order_id") || jenisCari.equals("destinasi")) {
                url += "?email=" + encodedEmail + "&" + jenisCari + "=" + encodedQuery;
            } else {
                // Jika jenisCari tidak dikenali, hanya sertakan email
                url += "?email=" + encodedEmail;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Terjadi kesalahan dalam encoding email.", Toast.LENGTH_SHORT).show();
            buttonCekTiket.setEnabled(true);
            progressBarLoading.setVisibility(View.GONE); // Sembunyikan ProgressBar
            return;
        }

        // Tambahkan logging untuk memverifikasi URL yang dibangun
        Log.d("CekTiketActivity", "URL yang dikirim: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("CekTiketActivity", "Response dari server: " + response);
                    try {
                        if (response.trim().startsWith("[")) {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    String fetchedOrderId = obj.getString("order_id");
                                    String destinasi = obj.getString("destinasi");
                                    String email = obj.getString("email");
                                    String tanggalKunjungan = obj.getString("tanggal_kunjungan");
                                    int jumlahTiket = obj.getInt("jumlah_tiket");
                                    String totalBayar = obj.getString("total_bayar");
                                    String statusTiket = obj.getString("status_tiket");
                                    String paymentUrl = obj.has("payment_url") && !obj.isNull("payment_url") ? obj.getString("payment_url") : null;

                                    RiwayatTiket tiket = new RiwayatTiket(fetchedOrderId, destinasi, email, tanggalKunjungan, jumlahTiket, totalBayar, statusTiket, paymentUrl);
                                    tiketList.add(tiket);
                                }
                                adapter.notifyDataSetChanged();
                                recyclerViewTiket.setVisibility(View.VISIBLE);
                                textViewPesan.setVisibility(View.GONE);
                            } else {
                                recyclerViewTiket.setVisibility(View.GONE);
                                textViewPesan.setVisibility(View.VISIBLE);
                                textViewPesan.setText("Tidak ada data ditemukan untuk " +
                                        (jenisCari.equals("order_id") ? "Order ID: " + query : "Destinasi: " + query));
                            }
                        } else if (response.trim().startsWith("{")) {
                            // Respons berupa JSONObject, mungkin error
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("message")) {
                                recyclerViewTiket.setVisibility(View.GONE);
                                textViewPesan.setVisibility(View.VISIBLE);
                                textViewPesan.setText(jsonObject.getString("message"));
                            } else if (jsonObject.has("error")) {
                                recyclerViewTiket.setVisibility(View.GONE);
                                textViewPesan.setVisibility(View.VISIBLE);
                                textViewPesan.setText(jsonObject.getString("error"));
                            } else {
                                recyclerViewTiket.setVisibility(View.GONE);
                                textViewPesan.setVisibility(View.VISIBLE);
                                textViewPesan.setText("Terjadi kesalahan dalam mengambil data.");
                            }
                        } else {
                            // Respons tidak dikenali
                            recyclerViewTiket.setVisibility(View.GONE);
                            textViewPesan.setVisibility(View.VISIBLE);
                            textViewPesan.setText("Terjadi kesalahan dalam mengambil data.");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CekTiketActivity.this, "Terjadi kesalahan saat parsing data.", Toast.LENGTH_SHORT).show();
                    }
                    // Sembunyikan ProgressBar setelah respons diterima
                    progressBarLoading.setVisibility(View.GONE);
                    buttonCekTiket.setEnabled(true);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(CekTiketActivity.this, "Terjadi kesalahan saat mengambil data.", Toast.LENGTH_SHORT).show();
                    textViewPesan.setVisibility(View.VISIBLE);
                    textViewPesan.setText("Terjadi kesalahan server.");
                    // Sembunyikan ProgressBar setelah terjadi kesalahan
                    progressBarLoading.setVisibility(View.GONE);
                    buttonCekTiket.setEnabled(true);
                });

        // Set retry policy jika diperlukan
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Menambahkan permintaan ke RequestQueue
        requestQueue.add(stringRequest);
    }
}
