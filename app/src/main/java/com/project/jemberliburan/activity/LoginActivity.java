package com.project.jemberliburan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.jemberliburan.connection.Db_Contract;
import com.project.jemberliburan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etx_username, etx_password;
    private Button btn_lgn;
    private TextView tx_register, tx_lupapassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Cek apakah sudah login
        SharedPreferences preferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // Langsung buka NavigasiActivity jika sudah login
            Intent intent = new Intent(LoginActivity.this, NavigasiActivity.class);
            startActivity(intent);
            finish();
            return; // Keluar dari onCreate untuk mencegah layout login tampil
        }

        setContentView(R.layout.activity_login);

        etx_username = findViewById(R.id.etx_username);
        etx_password = findViewById(R.id.etx_password);
        btn_lgn = findViewById(R.id.btn_lgn);
        tx_register = findViewById(R.id.tx_register);
        tx_lupapassword = findViewById(R.id.tx_lupapassword);

        // Ambil username dari Intent jika tersedia
        String passedUsername = getIntent().getStringExtra("username");
        if (passedUsername != null) {
            etx_username.setText(passedUsername); // Isi otomatis username
        }

        // Login button click event
        btn_lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etx_username.getText().toString().trim();
                String password = etx_password.getText().toString().trim();
                Log.d("LoginData", "Username: " + username + ", Password: " + password);

                if (!username.isEmpty() && !password.isEmpty()) {
                    if (isNetworkAvailable()) {
                        String url = Db_Contract.urlLoginActivity;
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        Log.d("LoginURL", "URL: " + url);

                        // Buat POST request
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                            Log.d("LoginResponse", "Response: " + response);

                            try {
                                // Mengambil data JSON dari response
                                JSONObject jsonResponse = new JSONObject(response);
                                String status = jsonResponse.getString("status");

                                if (status.equals("success")) {
                                    // Ambil data langsung dari jsonResponse
                                    int userId = jsonResponse.getInt("user_id");
                                    String usernameDB = jsonResponse.getString("username");
                                    String email = jsonResponse.getString("email");
                                    String fotoProfil = jsonResponse.getString("foto_profil");
                                    String noTelp = jsonResponse.has("no_telp") ? jsonResponse.getString("no_telp") : "No. Telepon Tidak Ditemukan";
                                    String alamat = jsonResponse.has("alamat") ? jsonResponse.getString("alamat") : "Alamat Tidak Ditemukan";
                                    String jenisKelamin = jsonResponse.has("jenis_kelamin") ? jsonResponse.getString("jenis_kelamin") : "Tidak Diketahui";

                                    Log.d("LoginSuccess", "UserID: " + userId + ", Username: " + usernameDB + ", email: " + email);
                                    Log.d("LoginSuccess", "FotoProfil: " + fotoProfil + ", NoTelp: " + noTelp + ", Alamat: " + alamat + ", JenisKelamin: " + jenisKelamin);

                                    // Simpan data ke SharedPreferences
                                    SharedPreferences preferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.putString("username", usernameDB); // Menyimpan username dari database
                                    editor.putString("email", email); // Menyimpan email
                                    editor.putInt("user_id", userId); // Menyimpan UserID
                                    editor.putString("foto_profil", fotoProfil); // Menyimpan Foto Profil
                                    editor.putString("phone", noTelp); // Menyimpan Nomor Telepon
                                    editor.putString("address", alamat); // Menyimpan Alamat
                                    editor.putString("gender", jenisKelamin); // Menyimpan Jenis Kelamin
                                    editor.apply();

                                    // Log SharedPreferences untuk debugging
                                    Log.d("SharedPreferences", "user_id: " + userId);
                                    Log.d("SharedPreferences", "username: " + usernameDB);
                                    Log.d("SharedPreferences", "email: " + email);
                                    Log.d("SharedPreferences", "foto_profil: " + fotoProfil);
                                    Log.d("SharedPreferences", "phone: " + noTelp);
                                    Log.d("SharedPreferences", "address: " + alamat);
                                    Log.d("SharedPreferences", "gender: " + jenisKelamin);

                                    // Pindah ke halaman utama setelah login
                                    Intent intent = new Intent(LoginActivity.this, NavigasiActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Menampilkan pesan kesalahan
                                    Toast.makeText(LoginActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "Terjadi kesalahan dalam memproses data", Toast.LENGTH_SHORT).show();
                            }
                        }, error -> {
                            Log.e("VolleyError", error.toString());
                            Toast.makeText(LoginActivity.this, "Terjadi kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                // Mengirimkan username dan password ke server
                                Map<String, String> params = new HashMap<>();
                                params.put("username", username);
                                params.put("password", password);

                                return params;
                            }
                        };

                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                10000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        requestQueue.add(stringRequest);
                    } else {
                        Toast.makeText(LoginActivity.this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Username dan Password tidak boleh kosong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tx_register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tx_lupapassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    // Fungsi untuk memeriksa apakah perangkat terhubung ke internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
