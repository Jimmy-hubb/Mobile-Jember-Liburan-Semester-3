package com.project.jemberliburan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ResetPasswordActivity";
    private EditText etxEmail, etxNewPassword, etxConfirmPassword;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        // Inisialisasi elemen UI
        etxEmail = findViewById(R.id.etx_email);
        etxNewPassword = findViewById(R.id.etx_new_password);
        etxConfirmPassword = findViewById(R.id.etx_confirm_password);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        // Mengatur email dari Intent jika tersedia
        String email = getIntent().getStringExtra("email");
        if (email != null) {
            etxEmail.setText(email);
        }

        // Tombol untuk reset password
        btnResetPassword.setOnClickListener(v -> {
            String userEmail = etxEmail.getText().toString().trim();
            String newPassword = etxNewPassword.getText().toString().trim();
            String confirmPassword = etxConfirmPassword.getText().toString().trim();

            // Validasi input
            if (validateInputs(userEmail, newPassword, confirmPassword)) {
                resetPassword(userEmail, newPassword);
            }
        });
    }

    /**
     * Validasi input dari pengguna
     */
    private boolean validateInputs(String email, String newPassword, String confirmPassword) {
        if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "Kata sandi harus memiliki minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Metode untuk mengirim permintaan reset password ke server
     */
    private void resetPassword(String email, String newPassword) {
        String url = Db_Contract.urlResetPassword;
        Log.d(TAG, "Sending reset password request for: " + email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Server response: " + response);
                    handleServerResponse(response);
                },
                this::handleVolleyError
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Parameter yang dikirim ke server
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", newPassword);
                return params;
            }
        };

        // Menambahkan pengaturan retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, // Timeout dalam milidetik
                0,    // Tidak ada retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Tambahkan permintaan ke antrean Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Tangani respons sukses dari server
     */
    private void handleServerResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.optString("status");
            String message = jsonResponse.optString("message");
            String username = jsonResponse.optString("username"); // Ambil username dari server

            if ("success".equalsIgnoreCase(status)) {
                Toast.makeText(this, "Kata sandi berhasil diubah!", Toast.LENGTH_SHORT).show();

                // Intent ke LoginActivity
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                intent.putExtra("username", username); // Kirim username ke LoginActivity
                startActivity(intent);

                finish(); // Menutup ResetPasswordActivity
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error: " + e.getMessage());
            Toast.makeText(this, "Kesalahan parsing data dari server", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Tangani error dari server atau jaringan
     */
    private void handleVolleyError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data);
                JSONObject errorJson = new JSONObject(errorData);
                String message = errorJson.optString("message", "Terjadi kesalahan.");
                Log.e(TAG, "Error Response: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "Error parsing error response: " + e.getMessage());
                Toast.makeText(this, "Kesalahan jaringan.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "Error: " + error.getMessage());
            Toast.makeText(this, "Kesalahan jaringan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
