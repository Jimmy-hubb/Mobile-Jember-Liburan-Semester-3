package com.project.jemberliburan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.jemberliburan.connection.Db_Contract;
import com.project.jemberliburan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    private EditText emailEditText;
    private Button sendVerificationButton;
    private boolean canSendRequest = true; // Mengatur delay pengiriman ulang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.emailEditText);
        sendVerificationButton = findViewById(R.id.sendVerificationButton);

        // Tombol kirim verifikasi
        sendVerificationButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            // Validasi email lebih kuat
            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid.", Toast.LENGTH_SHORT).show();
            } else if (!canSendRequest) {
                Toast.makeText(this, "Harap tunggu sebelum mengirim ulang kode verifikasi.", Toast.LENGTH_SHORT).show();
            } else {
                sendVerificationCode(email);
            }
        });
    }

    /**
     * Metode untuk mengirim kode verifikasi
     */
    private void sendVerificationCode(String email) {
        // Batasi pengiriman dengan mengatur canSendRequest ke false
        canSendRequest = false;
        sendVerificationButton.setEnabled(false); // Blok tombol

        String url = Db_Contract.urlSendVerificationCode; // Endpoint untuk pengiriman kode
        Log.d(TAG, "Sending verification code to: " + email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Server response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        handleSuccessResponse(jsonResponse, email);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        Toast.makeText(this, "Kesalahan parsing data dari server.", Toast.LENGTH_SHORT).show();
                    }
                },
                this::handleVolleyError
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

            @Override
            public DefaultRetryPolicy getRetryPolicy() {
                return new DefaultRetryPolicy(
                        5000, // Timeout dalam milidetik
                        0,    // Tidak ada retry
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                );
            }
        };

        // Tambahkan permintaan ke antrian
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        // Tambahkan delay 60 detik sebelum pengguna dapat mengirim ulang permintaan
        new Handler().postDelayed(() -> {
            canSendRequest = true;
            sendVerificationButton.setEnabled(true); // Aktifkan tombol kembali
        }, 60000); // 60 detik
    }

    /**
     * Tangani respons sukses dari server
     */
    private void handleSuccessResponse(JSONObject jsonResponse, String email) {
        String status = jsonResponse.optString("status");
        String message = jsonResponse.optString("message");

        if ("success".equalsIgnoreCase(status)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, VerificationCodeActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else if ("error".equalsIgnoreCase(status) && message.contains("Email tidak terdaftar")) {
            // Tampilkan pesan dan arahkan ke halaman registrasi
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, RegisterActivity.class); // Ganti dengan activity registrasi Anda
            startActivity(intent);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Tangani error dari server atau jaringan
     */
    private void handleVolleyError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 429) {
            Log.w(TAG, "Too Many Requests. Waiting before retry...");
            Toast.makeText(this, "Terlalu banyak permintaan. Tunggu sebelum mencoba lagi.", Toast.LENGTH_SHORT).show();
        } else if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
            // Jika error 404 dari server
            Toast.makeText(this, "Email tidak terdaftar. Silakan daftar terlebih dahulu.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, RegisterActivity.class); // Ganti dengan activity registrasi Anda
            startActivity(intent);
        } else {
            Log.e(TAG, "Error: " + error.getMessage());
            Toast.makeText(this, "Kesalahan jaringan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Tetapkan ulang canSendRequest menjadi true agar pengguna bisa mencoba lagi
        canSendRequest = true;
        sendVerificationButton.setEnabled(true);
    }
}
