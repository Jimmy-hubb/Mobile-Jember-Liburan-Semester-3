package com.project.jemberliburan.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.jemberliburan.R;
import com.project.jemberliburan.adapter.DestinasiAdapter;
import com.project.jemberliburan.model.Destinasi;

import java.util.ArrayList;
import java.util.List;

public class SearchDestinasiActivity extends AppCompatActivity {

    private EditText searchInput;
    private RecyclerView recyclerViewSearchResults;
    private List<Destinasi> destinasiList;
    private DestinasiAdapter destinasiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_destinasi);

        // Inisialisasi elemen layout
        searchInput = findViewById(R.id.searchInput);
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);
        ImageView backButton = findViewById(R.id.icon_back);

        // Tombol kembali
        backButton.setOnClickListener(v -> finish());

        // Data dummy destinasi dengan wisataId yang valid
        destinasiList = new ArrayList<>();
        destinasiList.add(new Destinasi("Pantai Papuma", R.drawable.img_papuma, "Wuluhan, Jember", 4.9, 1, "Pantai Papuma merupakan salah satu pantai pasir putih di selatan Jawa Timur. Pantai Tanjung Papuma terletak di desa Lojejer, Wuluhan, sekitar 45 menit dari pusat Kabupaten Jember. Pantai Papuma merupakan singkatan dari \"Pasir Putih Malikan\". Dikutip dari laman Perhutani, kata \"Tanjung\" disematkan dalam nama Pantai Papuma untuk menandakan posisi Pantai Papuma yang menjorok ke laut, tepatnya ke arah barat daya. Pantai Papuma dinobatkan sebagai salah satu pantai terindah di Indonesia pada TripAdvisor Traveler Choice 2015 lalu, bersanding dengan pantai-pantai kenamaan di Bali."));
        destinasiList.add(new Destinasi("Rembangan", R.drawable.img_rembangan, "Arjasa, Jember", 4.8, 2, "Rembangan berada pada ketinggian 650 meter di atas permukaan laut (mdpl). Tempat wisata yang berada di kaki Gunung Argopuro ini memiliki suhu udara yang dapat mencapai 14 derajat Celcius. Pengunjung dapat memanfaatkan tempat wisata ini untuk bersantai maupun healing."));
        destinasiList.add(new Destinasi("Pantai Teluk Love", R.drawable.img_teluklove, "Ambulu, Jember", 4.8, 3, "Pantai Teluk Love adalah destinasi wisata yang unik dan memikat di Kabupaten Jember, Jawa Timur, Indonesia. Sesuai dengan namanya, teluk ini memiliki bentuk menyerupai simbol hati (love) jika dilihat dari ketinggian, yang membuatnya sangat populer di kalangan wisatawan, terutama pasangan yang mencari suasana romantis."));
        destinasiList.add(new Destinasi("Pantai Watu Ulo", R.drawable.img_watuulo, "Ambulu, Jember", 4.8, 4, "Pantai Watu Ulo adalah salah satu destinasi wisata terkenal di Kabupaten Jember, Jawa Timur. Nama \"Watu Ulo\" dalam bahasa Jawa berarti \"batu ular,\" yang mengacu pada deretan batu memanjang di tepi pantai yang menyerupai bentuk tubuh ular. Pantai ini tidak hanya menawarkan keindahan alam, tetapi juga cerita legenda yang menambah daya tariknya."));
        destinasiList.add(new Destinasi("Bukit SJ88", R.drawable.img_sj88, "Wuluhan, Jember", 4.8, 5, "Bukit SJ88 adalah destinasi wisata alam yang menarik di Kabupaten Jember, Jawa Timur. Tempat ini populer karena keindahan panoramanya yang memukau, terutama pemandangan perbukitan hijau yang asri dan udara segar yang menenangkan. Lokasi ini menjadi salah satu pilihan wisatawan untuk menikmati suasana alam yang damai sekaligus aktivitas seru di luar ruangan."));
        destinasiList.add(new Destinasi("Gunung Gambir", R.drawable.img_gambir, "Sumberbaru, Jember", 4.8, 6, "Gunung Gambir adalah destinasi wisata alam yang terkenal di Kabupaten Jember, Jawa Timur. Tempat ini dikenal sebagai kawasan agrowisata yang memadukan keindahan pegunungan dengan perkebunan teh yang asri, menjadikannya tempat yang ideal untuk bersantai dan menikmati suasana alam."));
        destinasiList.add(new Destinasi("Air Terjun Tancak", R.drawable.img_airterjun_tancak, "Panti, Jember", 4.8, 7, "Air Terjun Tancak adalah salah satu destinasi wisata alam yang menakjubkan di Kabupaten Jember, Jawa Timur. Dengan keindahan air terjun yang tinggi dan suasana hutan yang asri, tempat ini menjadi pilihan favorit bagi pecinta alam dan wisatawan yang mencari pengalaman petualangan yang menenangkan."));
        destinasiList.add(new Destinasi("Air Terjun Antrokan", R.drawable.img_airterjun_antrokan, "Tanggul, Jember", 4.8, 8, "Air Terjun Antrokan adalah salah satu destinasi wisata alam yang tersembunyi di Kabupaten Jember, Jawa Timur. Tempat ini menawarkan keindahan air terjun yang alami dan suasana yang tenang, jauh dari keramaian, sehingga cocok untuk wisatawan yang ingin menikmati kedamaian di tengah alam."));

        // Setup RecyclerView dengan GridLayout
        recyclerViewSearchResults.setLayoutManager(new GridLayoutManager(this, 2));
        destinasiAdapter = new DestinasiAdapter(this, destinasiList);
        recyclerViewSearchResults.setAdapter(destinasiAdapter);

        // Tambahkan listener untuk pencarian
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak diperlukan
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDestinasi(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak diperlukan
            }
        });
    }

    private void filterDestinasi(String query) {
        List<Destinasi> filteredList = new ArrayList<>();
        for (Destinasi item : destinasiList) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Update adapter dengan hasil filter
        destinasiAdapter.updateList(filteredList);

        // Tampilkan pesan jika tidak ada hasil
        if (filteredList.isEmpty() && !query.isEmpty()) {
            Toast.makeText(this, "Destinasi tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }
}
