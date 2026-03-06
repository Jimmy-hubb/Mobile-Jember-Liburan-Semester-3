package com.project.jemberliburan.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.jemberliburan.connection.Db_Contract;
import com.project.jemberliburan.R;
import com.project.jemberliburan.activity.BantuanActivity;
import com.project.jemberliburan.activity.FavoritSayaActivity;
import com.project.jemberliburan.activity.LoginActivity;
import com.project.jemberliburan.activity.TentangSayaActivity;
import com.project.jemberliburan.activity.UlasanActivity;
import com.project.jemberliburan.adapter.ProfileAdapter;
import com.project.jemberliburan.model.Profile;
import com.project.jemberliburan.dialog.LogoutConfirmationDialog;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements LogoutConfirmationDialog.LogoutListener {

    private RecyclerView recyclerViewProfile;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;
    private TextView usernameTextView, emailTextView; // TextViews untuk menampilkan username dan email
    private ImageView logoProfile, profileImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Menghubungkan fragment dengan layout XML
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi RecyclerView
        recyclerViewProfile = view.findViewById(R.id.recyclerViewProfile);
        recyclerViewProfile.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi daftar data profil
        profileList = new ArrayList<>();
        profileList.add(new Profile(R.drawable.icon_favoritprofile, "Favorit Saya", R.drawable.icon_actionprofile));
        profileList.add(new Profile(R.drawable.icon_bantuanprofile, "Bantuan", R.drawable.icon_actionprofile));
        profileList.add(new Profile(R.drawable.icon_logoutprofile, "Log Out", R.drawable.icon_actionprofile));

        // Menghubungkan adapter dengan RecyclerView dengan listener
        profileAdapter = new ProfileAdapter(requireContext(), profileList, new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Profile profile) {
                if (profile.getTitle().equalsIgnoreCase("Log Out")) {
                    // Tampilkan dialog konfirmasi log out
                    showLogoutConfirmationDialog();
                } else if (profile.getTitle().equalsIgnoreCase("Favorit Saya")) {
                    // Implementasikan aksi Favorit Saya jika diperlukan
                    Intent intent = new Intent(getContext(), FavoritSayaActivity.class);
                    startActivity(intent);
                } else if (profile.getTitle().equalsIgnoreCase("Bantuan")) {
                    // Implementasikan aksi Bantuan jika diperlukan
                    Intent intent = new Intent(getContext(), BantuanActivity.class);
                    startActivity(intent);
                }
            }
        });
        recyclerViewProfile.setAdapter(profileAdapter);

        // Ambil username, email, fotoProfil dari SharedPreferences
        usernameTextView = view.findViewById(R.id.tx_username);
        emailTextView = view.findViewById(R.id.tx_email);
        profileImageView = view.findViewById(R.id.profileImageView); // Inisialisasi ImageView untuk foto profil

        SharedPreferences preferences = requireContext().getSharedPreferences("login_prefs", requireContext().MODE_PRIVATE);
        String username = preferences.getString("username", "Username Tidak Ditemukan");
        String email = preferences.getString("email", "Email Tidak Ditemukan");
        String fotoProfilPath = preferences.getString("foto_profil", ""); // Ambil path gambar profil

        // Set username dan email ke TextView
        usernameTextView.setText(username);
        emailTextView.setText(email);

        // Log data profil untuk debugging
        Log.d("ProfileFragment", "Username: " + username);
        Log.d("ProfileFragment", "Email: " + email);
        Log.d("ProfileFragment", "FotoProfil: " + fotoProfilPath);


        // Set gambar profil menggunakan Glide jika ada URL gambar
        if (!fotoProfilPath.isEmpty()) {
            // Log path gambar untuk debugging
            Log.d("ProfileFragment", "Path gambar profil: " + fotoProfilPath);

            // Misalnya, jika fotoProfilPath adalah URL
            String imageUrl = Db_Contract.baseUrl  + fotoProfilPath;

            Glide.with(this)
                    .load(imageUrl) // Mengambil gambar dari URL
                    .placeholder(R.drawable.icon_addimage) // Gambar default jika tidak ada
                    .circleCrop() // Membuat gambar menjadi bundar
                    .into(profileImageView); // Menampilkan gambar ke ImageView
        } else {
            Log.d("ProfileFragment", "Path gambar profil kosong.");
            // Jika tidak ada gambar, gunakan gambar default
            profileImageView.setImageResource(R.drawable.icon_addimage); // Gambar default
        }

        // Set aksi ketika logo profil diklik
        logoProfile = view.findViewById(R.id.logoProfile);
        logoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pindah ke TentangSayaActivity
                Intent intent = new Intent(requireContext(), TentangSayaActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Menampilkan dialog konfirmasi untuk log out menggunakan DialogFragment.
     */
    private void showLogoutConfirmationDialog() {
        LogoutConfirmationDialog dialog = new LogoutConfirmationDialog();
        // Tampilkan dialog menggunakan FragmentManager
        dialog.show(getChildFragmentManager(), "LogoutConfirmationDialog");
    }

    /**
     * Implementasi dari LogoutListener yang dipanggil saat konfirmasi log out.
     */
    @Override
    public void onLogoutConfirmed() {
        performLogout();
    }

    /**
     * Melakukan proses log out dengan membersihkan SharedPreferences dan mengalihkan ke LoginActivity.
     */
    private void performLogout() {
        // Hapus data login dari SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("login_prefs", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Pindah ke LoginActivity
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        // Tambahkan flag untuk mencegah kembali ke halaman sebelumnya
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Tutup fragment/activity saat ini
        requireActivity().finish();
    }
}
