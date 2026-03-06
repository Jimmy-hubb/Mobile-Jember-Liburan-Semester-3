package com.project.jemberliburan.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.project.jemberliburan.connection.Db_Contract;
import com.project.jemberliburan.connection.MySingleton;
import com.project.jemberliburan.connection.VolleyMultipartRequest;
import com.project.jemberliburan.R;
import com.project.jemberliburan.dialog.ConfirmUpdateDialog;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TentangSayaActivity extends AppCompatActivity implements ConfirmUpdateDialog.ConfirmUpdateListener {

    private ImageView icon_back, imgProfile;
    private Button btnSave;
    private EditText etEmail, etUsername, etPhone, etAddress;
    private RadioGroup rgJenisKelamin;
    private RadioButton rbLaki, rbPerempuan;
    private ProgressBar progressBar;

    // URL untuk memperbarui data pengguna
    public static final String urlUpdateUsers = Db_Contract.urlUpdateUsers;

    private static final int STORAGE_PERMISSION_CODE = 100;
    private Uri imageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_saya);

        // Inisialisasi komponen
        icon_back = findViewById(R.id.icon_back);
        imgProfile = findViewById(R.id.imgProfile);
        btnSave = findViewById(R.id.btn_register);
        etEmail = findViewById(R.id.etx_email);
        etUsername = findViewById(R.id.etx_username);
        etPhone = findViewById(R.id.etx_notelpon);
        etAddress = findViewById(R.id.etx_alamat);
        rgJenisKelamin = findViewById(R.id.rg_jeniskelamin);
        rbLaki = findViewById(R.id.rb_laki);
        rbPerempuan = findViewById(R.id.rb_perempuan);
        progressBar = findViewById(R.id.progressBar);

        // Ambil data username, email, dan UserID dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String username = preferences.getString("username", ""); // Gunakan key "username" lowercase
        String email = preferences.getString("email", "");       // Gunakan key "email" lowercase
        int userId = preferences.getInt("user_id", -1);          // Gunakan key "user_id" lowercase
        String fotoProfil = preferences.getString("foto_profil", "uploads/default.png"); // Gunakan key "foto_profil" lowercase
        String noTelp = preferences.getString("phone", "");      // Gunakan key "phone" lowercase
        String alamat = preferences.getString("address", "");    // Gunakan key "address" lowercase
        String gender = preferences.getString("gender", "");     // Gunakan key "gender" lowercase

        Log.d("TentangSaya", "UserID: " + userId + ", Username: " + username + ", Email: " + email + ", FotoProfil: " + fotoProfil + ", Gender: " + gender);

        if (userId == -1) {
            Toast.makeText(this, "UserID tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TentangSayaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Tampilkan data di EditText (email tidak dapat diedit)
        etUsername.setText(username);
        etEmail.setText(email);
        etEmail.setEnabled(false);
        etPhone.setText(noTelp);
        etAddress.setText(alamat);

        // Muat gambar profil menggunakan Glide
        String imageUrl = Db_Contract.baseUrl + fotoProfil;
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.icon_addimage) // Ganti dengan gambar default yang sesuai
                .circleCrop() // Membuat gambar menjadi bundar
                .into(imgProfile);

        // Set jenis kelamin berdasarkan data yang disimpan
        if (gender.equals("Laki-laki")) {
            rbLaki.setChecked(true);
        } else if (gender.equals("Perempuan")) {
            rbPerempuan.setChecked(true);
        }

        // Listener untuk tombol kembali
        icon_back.setOnClickListener(v -> {
            // Kembali ke NavigasiActivity
            Intent intent = new Intent(TentangSayaActivity.this, NavigasiActivity.class);
            startActivity(intent);
            finish();
        });

        // Inisialisasi ActivityResultLauncher untuk permintaan izin
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Log.d("TentangSayaActivity", "Izin penyimpanan diberikan");
                        // Izin diberikan, buka file chooser
                        openFileChooser();
                    } else {
                        Log.d("TentangSayaActivity", "Izin penyimpanan ditolak");
                        // Izin ditolak, tampilkan toast
                        Toast.makeText(TentangSayaActivity.this, "Izin penyimpanan diperlukan untuk memilih gambar", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Listener untuk ImageView profil untuk memilih gambar
        imgProfile.setOnClickListener(v -> {
            // Cek izin penyimpanan
            if (ContextCompat.checkSelfPermission(TentangSayaActivity.this,
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            ? Manifest.permission.READ_MEDIA_IMAGES
                            : Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Tampilkan dialog permintaan izin
                showPermissionDialog();
            } else {
                openFileChooser();
            }
        });

        // Inisialisasi ActivityResultLauncher untuk mendapatkan hasil pemilihan gambar
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri sourceUri = result.getData().getData();
                            if (sourceUri != null) {
                                Log.d("TentangSayaActivity", "Gambar dipilih: " + sourceUri.toString());
                                startCrop(sourceUri);
                            } else {
                                Log.d("TentangSayaActivity", "Uri gambar null");
                            }
                        } else {
                            Log.d("TentangSayaActivity", "Tidak ada gambar yang dipilih");
                        }
                    }
                }
        );

        // Listener untuk tombol simpan
        btnSave.setOnClickListener(v -> {
            // Ambil data dari EditText dan RadioButton
            String emailInput = etEmail.getText().toString().trim(); // Email tetap diambil dari EditText yang dinonaktifkan
            String usernameInput = etUsername.getText().toString().trim();
            String noTelpInput = etPhone.getText().toString().trim();
            String alamatInput = etAddress.getText().toString().trim();

            // Validasi data
            if (usernameInput.isEmpty() || noTelpInput.isEmpty() || rgJenisKelamin.getCheckedRadioButtonId() == -1 || alamatInput.isEmpty()) {
                Toast.makeText(TentangSayaActivity.this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show();
            } else {
                // Tampilkan dialog konfirmasi update profil
                showConfirmUpdateDialog();
            }
        });
    }

    /**
     * Menampilkan dialog konfirmasi untuk menyimpan perubahan profil.
     */
    private void showConfirmUpdateDialog() {
        ConfirmUpdateDialog dialog = new ConfirmUpdateDialog();
        dialog.show(getSupportFragmentManager(), "ConfirmUpdateDialog");
    }

    /**
     * Implementasi dari ConfirmUpdateListener yang dipanggil saat konfirmasi update diterima.
     */
    @Override
    public void onConfirmUpdate() {
        // Ambil data dari EditText dan RadioButton
        String emailInput = etEmail.getText().toString().trim(); // Email tetap diambil dari EditText yang dinonaktifkan
        String usernameInput = etUsername.getText().toString().trim();
        String noTelpInput = etPhone.getText().toString().trim();
        String alamatInput = etAddress.getText().toString().trim();

        // Ambil pilihan jenis kelamin
        String jenisKelamin = "";
        int selectedId = rgJenisKelamin.getCheckedRadioButtonId();
        if (selectedId == R.id.rb_laki) {
            jenisKelamin = rbLaki.getText().toString();
        } else if (selectedId == R.id.rb_perempuan) {
            jenisKelamin = rbPerempuan.getText().toString();
        }

        // Panggil metode untuk mengupdate profil
        // Ambil userId dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);
        if (userId != -1) {
            updateProfile(userId, emailInput, usernameInput, noTelpInput, jenisKelamin, alamatInput);
        } else {
            Toast.makeText(this, "UserID tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TentangSayaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Menampilkan dialog untuk meminta izin penyimpanan.
     */
    private void showPermissionDialog() {
        Log.d("TentangSayaActivity", "Menampilkan dialog permintaan izin penyimpanan");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Izin Penyimpanan Diperlukan");
        builder.setMessage("Aplikasi ini memerlukan izin penyimpanan untuk memilih gambar profil. Apakah Anda ingin memberikannya?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("TentangSayaActivity", "Pengguna memilih untuk memberikan izin");
                // Minta izin penyimpanan
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("TentangSayaActivity", "Pengguna menolak izin");
                // Tampilkan toast jika izin tidak diberikan
                Toast.makeText(TentangSayaActivity.this, "Izin penyimpanan diperlukan untuk memilih gambar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Membuka galeri untuk memilih gambar.
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Pilih Gambar"));
    }

    /**
     * Memulai aktivitas pemangkasan gambar menggunakan uCrop.
     */
    private void startCrop(@NonNull Uri sourceUri) {
        String destinationFileName = "CroppedImage_" + System.currentTimeMillis() + ".jpg";
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true); // Menampilkan lapisan dimmed berbentuk bundar
        options.setShowCropGrid(false); // Menyembunyikan grid
        options.setToolbarTitle("Potong Gambar");
        options.setFreeStyleCropEnabled(false); // Menonaktifkan crop gaya bebas
        options.setCompressionQuality(80); // Kualitas kompresi

        Uri destinationUri = Uri.fromFile(new java.io.File(getCacheDir(), destinationFileName));

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Membuat aspek rasio 1:1 untuk bentuk bundar
                .withOptions(options)
                .start(TentangSayaActivity.this);
    }

    /**
     * Menangani hasil dari aktivitas pemangkasan gambar.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    Log.d("TentangSayaActivity", "Hasil pemangkasan: " + resultUri.toString());
                    imageUri = resultUri;
                    imgProfile.setImageURI(imageUri);
                    Glide.with(this)
                            .load(imageUri)
                            .circleCrop()  // Memastikan gambar tampil bulat
                            .into(imgProfile);
                } else {
                    Log.d("TentangSayaActivity", "Hasil pemangkasan Uri null");
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                Log.e("TentangSayaActivity", "Error saat pemangkasan: " + cropError.getMessage());
                Toast.makeText(this, "Gagal memotong gambar: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Mengupdate profil pengguna termasuk mengunggah gambar profil jika dipilih.
     */
    private void updateProfile(int userId, String email, String username, String noTelp, String jenisKelamin, String alamat) {
        Log.d("TentangSayaActivity", "Memulai proses update profil");
        // Tampilkan ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, urlUpdateUsers,
                response -> {
                    Log.d("TentangSayaActivity", "Menerima respons dari server");
                    progressBar.setVisibility(View.GONE);
                    String resultResponse = "";
                    try {
                        resultResponse = new String(response.data, "UTF-8");
                        Log.d("UpdateProfileResponse", "Response: " + resultResponse);

                        JSONObject jsonObject = new JSONObject(resultResponse);
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (success) {
                            // Update SharedPreferences
                            SharedPreferences preferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", username); // Gunakan key lowercase
                            editor.putString("email", email);       // Email tetap disimpan tanpa diubah
                            editor.putString("phone", noTelp);      // Simpan no_telp
                            editor.putString("gender", jenisKelamin); // Simpan jenis_kelamin
                            editor.putString("address", alamat);

                            // Jika server mengembalikan path gambar yang baru
                            if (jsonObject.has("foto_profil")) {
                                String fotoProfil = jsonObject.getString("foto_profil");
                                editor.putString("foto_profil", fotoProfil);
                            }

                            editor.apply();

                            Toast.makeText(TentangSayaActivity.this, message, Toast.LENGTH_SHORT).show();

                            // Muat ulang gambar profil menggunakan Glide jika ada path baru
                            if (jsonObject.has("foto_profil")) {
                                String fotoProfil = jsonObject.getString("foto_profil");
                                String imageUrl = Db_Contract.baseUrl + fotoProfil;
                                Glide.with(TentangSayaActivity.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.icon_addimage) // Ganti dengan gambar default yang sesuai
                                        .circleCrop() // Membuat gambar menjadi bundar
                                        .into(imgProfile);
                            }

                        } else {
                            Toast.makeText(TentangSayaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(TentangSayaActivity.this, "Terjadi kesalahan dalam memproses data", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TentangSayaActivity.this, "Terjadi kesalahan dalam memproses data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("TentangSayaActivity", "Error dari Volley: " + error.toString());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TentangSayaActivity.this, "Terjadi kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Set text parameters
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(userId));
                params.put("email", email);
                params.put("username", username);
                params.put("no_telp", noTelp);
                params.put("jenis_kelamin", jenisKelamin);
                params.put("alamat", alamat);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        Log.d("TentangSayaActivity", "Gambar diubah menjadi bitmap");
                        // Compress the image if necessary
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                        byte[] imageBytes = baos.toByteArray();
                        params.put("foto_profil", new DataPart("foto_profil.jpg", imageBytes, "image/jpeg"));
                        Log.d("TentangSayaActivity", "Gambar dikompresi dan dimasukkan ke parameter foto_profil");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(TentangSayaActivity.this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show();
                    }
                }
                return params;
            }
        };

        // Set Retry Policy jika diperlukan
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, // timeout dalam milidetik
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Tambahkan request ke RequestQueue menggunakan Singleton
        MySingleton.getInstance(this).addToRequestQueue(multipartRequest);
    }
}