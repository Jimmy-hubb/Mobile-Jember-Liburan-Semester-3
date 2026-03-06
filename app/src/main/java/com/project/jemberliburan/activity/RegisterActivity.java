package com.project.jemberliburan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.jemberliburan.connection.Db_Contract;
import com.project.jemberliburan.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etx_email, etx_username, etx_password;
    private Button btn_register;
    private TextView tx_login;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        etx_email = findViewById(R.id.etx_email); // Menggunakan id yang benar untuk email
        etx_username = findViewById(R.id.etx_username);
        etx_password = findViewById(R.id.etx_password);
        btn_register = findViewById(R.id.btn_register);
        tx_login = findViewById(R.id.tx_login);
        img_back = findViewById(R.id.img_back);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etx_email.getText().toString().trim();
                String username = etx_username.getText().toString().trim();
                String password = etx_password.getText().toString().trim();

                // Cek jika semua field terisi
                if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    String url = Db_Contract.urlRegisterActivity;

                    // Membuat StringRequest untuk Volley
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("ServerResponse", response); // Log respons dari server
                                    // Respons sukses
                                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                                    // Pindah ke halaman login
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish(); // Tutup activity registrasi
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Respons error, tambahkan log lebih detail
                                    Toast.makeText(RegisterActivity.this, "Registrasi gagal: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            // Menyiapkan parameter untuk request
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("username", username);
                            params.put("password", password);
                            return params;
                        }
                    };

                    // Mengatur Retry Policy
                    int socketTimeout = 5000; // 5 detik timeout
                    int maxRetries = 3; // Coba maksimal 3 kali
                    float backoffMultiplier = 1.5f; // Penundaan retry akan bertambah
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, maxRetries, backoffMultiplier);
                    stringRequest.setRetryPolicy(policy);

                    // Menambahkan request ke queue Volley
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                } else {
                    // Pesan jika ada field yang kosong
                    Toast.makeText(RegisterActivity.this, "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Aksi saat tombol login diklik
        tx_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke halaman login
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke halaman login
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
