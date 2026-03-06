package com.project.jemberliburan.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.net.URLEncoder;
import java.util.ArrayList;

public class RiwayatTiketActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTiket;
    private TextView textViewPesan;
    private View progressBarRiwayat; // Jika Anda menambahkan ProgressBar

    private RiwayatTiketAdapter adapter;
    private ArrayList<RiwayatTiket> tiketList;

    // Variabel untuk menyimpan email pengguna yang sudah login
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_tiket);

        // Inisialisasi UI Components
        recyclerViewTiket = findViewById(R.id.recyclerViewRiwayatTiket);
        textViewPesan = findViewById(R.id.textViewPesan);
        // progressBarRiwayat = findViewById(R.id.progressBarRiwayat); // Jika menggunakan ProgressBar

        // Setup RecyclerView
        recyclerViewTiket.setLayoutManager(new LinearLayoutManager(this));
        tiketList = new ArrayList<>();
        adapter = new RiwayatTiketAdapter(this, tiketList);
        recyclerViewTiket.setAdapter(adapter);

        // Ambil email pengguna yang sudah login
        userEmail = getUserEmailFromSession();

        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User email tidak ditemukan. Silakan login terlebih dahulu.", Toast.LENGTH_LONG).show();
            Log.d("RiwayatTiketActivity", "Email pengguna tidak ditemukan.");
            // Redirect ke halaman login atau tindakan lain
            finish();
            return;
        } else {
            Log.d("RiwayatTiketActivity", "Email pengguna ditemukan: " + userEmail);
        }

        // Ambil semua tiket terkait email
        fetchAllTiket(userEmail);
    }

    /**
     * Fungsi untuk mengambil email pengguna dari sesi login.
     *
     * @return Email pengguna.
     */
    private String getUserEmailFromSession() {
        SharedPreferences prefs = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String email = prefs.getString("email", null); // Gunakan key "email" lowercase
        Log.d("RiwayatTiketActivity", "Email yang diambil dari SharedPreferences: " + email);
        return email;
    }

    private void fetchAllTiket(String email) {
        // Tampilkan ProgressBar jika menggunakan
        // progressBarRiwayat.setVisibility(View.VISIBLE);

        String encodedEmail = "";
        try {
            encodedEmail = URLEncoder.encode(email, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Terjadi kesalahan dalam encoding email.", Toast.LENGTH_SHORT).show();
            // progressBarRiwayat.setVisibility(View.GONE);
            return;
        }

        String url = Db_Contract.urlRiwayatTiket + "?email=" + encodedEmail;
        Log.d("RiwayatTiketURL", "URL: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("API_Response", response);
                    try {
                        if (response.trim().startsWith("[")) {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    String fetchedOrderId = obj.getString("order_id");
                                    String destinasi = obj.getString("destinasi");
                                    String emailFetched = obj.getString("email");
                                    String tanggalKunjungan = obj.getString("tanggal_kunjungan");
                                    int jumlahTiket = obj.getInt("jumlah_tiket");
                                    String totalBayar = obj.getString("total_bayar");
                                    String statusTiket = obj.getString("status_tiket");
                                    String paymentUrl = obj.has("payment_url") && !obj.isNull("payment_url") ? obj.getString("payment_url") : null;

                                    RiwayatTiket tiket = new RiwayatTiket(fetchedOrderId, destinasi, emailFetched, tanggalKunjungan, jumlahTiket, totalBayar, statusTiket, paymentUrl);
                                    tiketList.add(tiket);
                                }
                                adapter.notifyDataSetChanged();
                                recyclerViewTiket.setVisibility(View.VISIBLE);
                                textViewPesan.setVisibility(View.GONE);
                            } else {
                                recyclerViewTiket.setVisibility(View.GONE);
                                textViewPesan.setVisibility(View.VISIBLE);
                                textViewPesan.setText("Tidak ada data ditemukan.");
                            }
                        } else {
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
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RiwayatTiketActivity.this, "Terjadi kesalahan saat parsing data.", Toast.LENGTH_SHORT).show();
                    }
                    // progressBarRiwayat.setVisibility(View.GONE);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(RiwayatTiketActivity.this, "Terjadi kesalahan saat mengambil data.", Toast.LENGTH_SHORT).show();
                    recyclerViewTiket.setVisibility(View.GONE);
                    textViewPesan.setVisibility(View.VISIBLE);
                    textViewPesan.setText("Terjadi kesalahan server.");
                    // progressBarRiwayat.setVisibility(View.GONE);
                });

        // Menambahkan permintaan ke RequestQueue
        requestQueue.add(stringRequest);
    }
}
