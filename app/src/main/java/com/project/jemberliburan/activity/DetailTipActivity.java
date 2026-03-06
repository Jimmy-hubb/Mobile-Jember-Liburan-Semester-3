package com.project.jemberliburan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.project.jemberliburan.R;

public class DetailTipActivity extends AppCompatActivity { // Ubah warisan jika perlu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_tip);

        // Ambil ID dari Intent
        int tipId = getIntent().getIntExtra("tip_id", -1);

        // Inisialisasi elemen layout
        ImageView image = findViewById(R.id.image);
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        ImageView backButton = findViewById(R.id.icon_back);

        // Tambahkan logika untuk tombol kembali
        backButton.setOnClickListener(v -> finish());

        // Tentukan konten berdasarkan ID
        switch (tipId) {
            case 0: // TipsTrip1
                title.setText("Kenapa liburan sering terasa mahal?");
                description.setText("Liburan tidak selalu identik dengan biaya besar. Dengan perencanaan yang matang, Anda bisa menikmati pengalaman liburan yang seru tanpa harus menguras dompet. Mulailah dengan menyusun anggaran, mencari promo tiket transportasi atau akomodasi, dan memilih destinasi yang sesuai dengan budget. Selain itu, bawalah bekal makanan ringan dan manfaatkan aplikasi perjalanan untuk mendapatkan diskon tambahan. Semua ini bisa membantu Anda menjalani liburan hemat namun tetap menyenangkan.\n" +
                        "\n");
                image.setImageResource(R.drawable.img_detailtip1);
                break;
            case 1: // TipsTrip2
                title.setText("Pernah lupa barang penting saat liburan?");
                description.setText("Mengemas barang bawaan dengan cerdas adalah kunci perjalanan yang nyaman. Buat daftar kebutuhan agar Anda tidak lupa barang penting, seperti dokumen perjalanan, pakaian yang sesuai, dan peralatan mandi. Pertimbangkan juga faktor cuaca dan durasi liburan untuk menentukan apa saja yang harus dibawa. Jangan lupa gunakan koper atau tas yang ringkas namun muat banyak, sehingga perjalanan Anda lebih terorganisir dan bebas repot.\n" +
                        "\n");
                image.setImageResource(R.drawable.img_detailtip2);
                break;
            case 2: // TipsTrip3
                title.setText("Bagaimana cara menyesuaikan liburan dengan musim?");
                description.setText("Perjalanan yang sukses membutuhkan penyesuaian dengan kondisi musim di tempat tujuan. Misalnya, saat musim dingin, pastikan Anda membawa pakaian hangat, sementara musim panas membutuhkan perlindungan ekstra dari sinar matahari. Ketahui juga aktivitas yang cocok untuk setiap musim, seperti ski di musim dingin atau snorkeling di musim panas. Dengan persiapan yang tepat, Anda bisa menikmati setiap musim tanpa khawatir terhambat oleh cuaca.\n" +
                        "\n");
                image.setImageResource(R.drawable.img_detailtip3);
                break;
            default:
                title.setText("Tips Tidak Ditemukan");
                description.setText("ID tidak valid.");
                image.setImageResource(R.drawable.img_default);
                break;
        }
    }
}
