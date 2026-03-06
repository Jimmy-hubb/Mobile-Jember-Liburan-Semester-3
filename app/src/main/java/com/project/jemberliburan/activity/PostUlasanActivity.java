package com.project.jemberliburan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.project.jemberliburan.R;
import com.project.jemberliburan.connection.Db_Contract;
import com.project.jemberliburan.connection.MySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostUlasanActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText editTextKomentar;
    private Button buttonSubmitUlasan;
    private Button buttonQuickMessage1, buttonQuickMessage2, buttonQuickMessage3, buttonQuickMessage4, buttonQuickMessage5;

    private int wisataId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ulasan);

        ratingBar = findViewById(R.id.ratingBar);
        editTextKomentar = findViewById(R.id.editTextComment);
        buttonSubmitUlasan = findViewById(R.id.buttonSubmitUlasan);
        buttonQuickMessage1 = findViewById(R.id.buttonQuickMessage1);
        buttonQuickMessage2 = findViewById(R.id.buttonQuickMessage2);
        buttonQuickMessage3 = findViewById(R.id.buttonQuickMessage3);
        buttonQuickMessage4 = findViewById(R.id.buttonQuickMessage4);
        buttonQuickMessage5 = findViewById(R.id.buttonQuickMessage5);

        // Ambil wisata_id dari intent
        wisataId = getIntent().getIntExtra("wisata_id", -1);
        if (wisataId == -1) {
            Toast.makeText(this, "ID Destinasi tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Listener tombol pesan cepat
        buttonQuickMessage1.setOnClickListener(v -> editTextKomentar.setText("Bagus Sekali!"));
        buttonQuickMessage2.setOnClickListener(v -> editTextKomentar.setText("Cukup Memuaskan"));
        buttonQuickMessage3.setOnClickListener(v -> editTextKomentar.setText("Perlu Peningkatan"));
        buttonQuickMessage4.setOnClickListener(v -> editTextKomentar.setText("Luar Biasa"));
        buttonQuickMessage5.setOnClickListener(v -> editTextKomentar.setText("Biasa Saja"));

        buttonSubmitUlasan.setOnClickListener(v -> submitUlasan());
    }

    private void submitUlasan() {
        float rating = ratingBar.getRating();
        String komentar = editTextKomentar.getText().toString().trim();

        if (rating < 1 || rating > 5) {
            Toast.makeText(this, "Rating harus antara 1-5", Toast.LENGTH_SHORT).show();
            return;
        }

        if (komentar.isEmpty()) {
            Toast.makeText(this, "Komentar tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1 || wisataId == -1) {
            Toast.makeText(this, "Data tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat JSON object untuk dikirim ke server
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("wisata_id", wisataId);
        params.put("rating", rating);
        params.put("komentar", komentar);

        String url = Db_Contract.urlAddReview;
        Log.d("PostUlasanActivity", "Sending JSON: " + new Gson().toJson(params));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, new JSONObject(params),
                response -> {
                    Log.d("PostUlasanActivity", "Response: " + response.toString());
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            Toast.makeText(this, "Ulasan berhasil dikirim", Toast.LENGTH_SHORT).show();

                            // Kirim hasil kembali ke UlasanActivity
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            String message = response.getString("message");
                            Toast.makeText(this, "Gagal: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("PostUlasanActivity", "Response Parsing Error: " + e.getMessage());
                        Toast.makeText(this, "Kesalahan dalam respons server.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("PostUlasanActivity", "Volley Error: " + error);
                    if (error.networkResponse != null) {
                        Log.e("PostUlasanActivity", "Error Response Code: " + error.networkResponse.statusCode);
                        if (error.networkResponse.data != null) {
                            Log.e("PostUlasanActivity", "Error Data: " + new String(error.networkResponse.data));
                        }
                    }
                    Toast.makeText(this, "Gagal mengirim ulasan.", Toast.LENGTH_SHORT).show();
                }
        );

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
