package com.project.jemberliburan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.jemberliburan.model.Category;
import com.project.jemberliburan.R;
import com.project.jemberliburan.activity.DetailDestinasiActivity;
import com.project.jemberliburan.activity.SearchDestinasiActivity;
import com.project.jemberliburan.adapter.CategoryAdapter;
import com.project.jemberliburan.adapter.DestinasiAdapter;
import com.project.jemberliburan.model.Destinasi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DestinasiFragment extends Fragment {
    private RecyclerView recyclerViewKategoriDestinasi;
    private RecyclerView recyclerViewDestinasi;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destinasi, container, false);

        // Inisialisasi RecyclerView untuk kategori destinasi
        recyclerViewKategoriDestinasi = view.findViewById(R.id.recyclerViewKategoriDestinasi);
        recyclerViewDestinasi = view.findViewById(R.id.recyclerViewDestinasi);
        CardView jelajahiDestinasiCardView = view.findViewById(R.id.JelajahiDestinasiCardView);

        jelajahiDestinasiCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchDestinasiActivity.class);
            startActivity(intent);
        });

        // Inisialisasi data kategori
        categoryList = getCategoryList();

        // Atur adapter untuk kategori
        categoryAdapter = new CategoryAdapter(categoryList, this::onCategorySelected);
        recyclerViewKategoriDestinasi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewKategoriDestinasi.setAdapter(categoryAdapter);

        // Set data default (misalnya kategori pertama)
        if (!categoryList.isEmpty()) {
            onCategorySelected(categoryList.get(0));
        }

        return view;
    }


    private List<Category> getCategoryList() {
        // Membuat daftar kategori
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Semua", 0, true));
        categories.add(new Category("Gunung", 0, false));
        categories.add(new Category("Pantai", 0, false));
        categories.add(new Category("Bukit", 0, false));
        categories.add(new Category("Air Terjun", 0, false));
        return categories;
    }

    private void onCategorySelected(Category category) {
        // Memperbarui daftar destinasi berdasarkan kategori yang dipilih
        List<Destinasi> destinasiList;

        switch (category.getName()) {
            case "Semua":
                destinasiList = Arrays.asList(
                        new Destinasi("Pantai Papuma", R.drawable.img_papuma, "Wuluhan, Jember", 4.9, 1, "Pantai Papuma merupakan salah satu pantai pasir putih di selatan Jawa Timur. Pantai Tanjung Papuma terletak di desa Lojejer, Wuluhan, sekitar 45 menit dari pusat Kabupaten Jember. Pantai Papuma merupakan singkatan dari 'Pasir Putih Malikan'. Dikutip dari laman Perhutani, kata 'Tanjung' disematkan dalam nama Pantai Papuma untuk menandakan posisi Pantai Papuma yang menjorok ke laut, tepatnya ke arah barat daya. Pantai Papuma dinobatkan sebagai salah satu pantai terindah di Indonesia pada TripAdvisor Traveler Choice 2015 lalu, bersanding dengan pantai-pantai kenamaan di Bali."),
                        new Destinasi("Pantai Teluk Love", R.drawable.img_teluklove, "Ambulu, Jember", 4.8, 3, "Pantai Teluk Love adalah destinasi wisata yang unik dan memikat di Kabupaten Jember, Jawa Timur, Indonesia. Sesuai dengan namanya, teluk ini memiliki bentuk menyerupai simbol hati (love) jika dilihat dari ketinggian, yang membuatnya sangat populer di kalangan wisatawan, terutama pasangan yang mencari suasana romantis."),
                        new Destinasi("Pantai Watu Ulo", R.drawable.img_watuulo, "Ambulu, Jember", 4.8, 4, "Pantai Watu Ulo adalah salah satu destinasi wisata terkenal di Kabupaten Jember, Jawa Timur. Nama 'Watu Ulo' dalam bahasa Jawa berarti 'batu ular,' yang mengacu pada deretan batu memanjang di tepi pantai yang menyerupai bentuk tubuh ular. Pantai ini tidak hanya menawarkan keindahan alam, tetapi juga cerita legenda yang menambah daya tariknya."),
                        new Destinasi("Bukit SJ88", R.drawable.img_sj88, "Wuluhan, Jember", 4.8, 5, "Bukit SJ88 adalah destinasi wisata alam yang menarik di Kabupaten Jember, Jawa Timur. Tempat ini populer karena keindahan panoramanya yang memukau, terutama pemandangan perbukitan hijau yang asri dan udara segar yang menenangkan. Lokasi ini menjadi salah satu pilihan wisatawan untuk menikmati suasana alam yang damai sekaligus aktivitas seru di luar ruangan."),
                        new Destinasi("Rembangan", R.drawable.img_rembangan, "Arjasa, Jember", 4.8, 2, "Rembangan berada pada ketinggian 650 meter di atas permukaan laut (mdpl). Tempat wisata yang berada di kaki Gunung Argopuro ini memiliki suhu udara yang dapat mencapai 14 derajat Celcius. Pengunjung dapat memanfaatkan tempat wisata ini untuk bersantai maupun healing."),
                        new Destinasi("Gunung Gambir", R.drawable.img_gambir, "Sumberbaru, Jember", 4.8, 6, "Gunung Gambir adalah destinasi wisata alam yang terkenal di Kabupaten Jember, Jawa Timur. Tempat ini dikenal sebagai kawasan agrowisata yang memadukan keindahan pegunungan dengan perkebunan teh yang asri, menjadikannya tempat yang ideal untuk bersantai dan menikmati suasana alam."),
                        new Destinasi("Air Terjun Tancak", R.drawable.img_airterjun_tancak, "Panti, Jember", 4.8, 7, "Air Terjun Tancak adalah salah satu destinasi wisata alam yang menakjubkan di Kabupaten Jember, Jawa Timur. Dengan keindahan air terjun yang tinggi dan suasana hutan yang asri, tempat ini menjadi pilihan favorit bagi pecinta alam dan wisatawan yang mencari pengalaman petualangan yang menenangkan."),
                        new Destinasi("Air Terjun Antrokan", R.drawable.img_airterjun_antrokan, "Tanggul, Jember", 4.8, 8, "Air Terjun Antrokan adalah salah satu destinasi wisata alam yang tersembunyi di Kabupaten Jember, Jawa Timur. Tempat ini menawarkan keindahan air terjun yang alami dan suasana yang tenang, jauh dari keramaian, sehingga cocok untuk wisatawan yang ingin menikmati kedamaian di tengah alam.")
                );
                break;

            case "Gunung":
                destinasiList = Arrays.asList(
                        new Destinasi("Gunung Gambir", R.drawable.img_gambir, "Sumberbaru, Jember", 4.8, 6, "Gunung Gambir adalah destinasi wisata alam yang terkenal di Kabupaten Jember, Jawa Timur. Tempat ini dikenal sebagai kawasan agrowisata yang memadukan keindahan pegunungan dengan perkebunan teh yang asri, menjadikannya tempat yang ideal untuk bersantai dan menikmati suasana alam.")
                );
                break;

            case "Pantai":
                destinasiList = Arrays.asList(
                        new Destinasi("Pantai Papuma", R.drawable.img_papuma, "Wuluhan, Jember", 4.9, 1, "Pantai Papuma merupakan salah satu pantai pasir putih di selatan Jawa Timur. Pantai Tanjung Papuma terletak di desa Lojejer, Wuluhan, sekitar 45 menit dari pusat Kabupaten Jember. Pantai Papuma merupakan singkatan dari 'Pasir Putih Malikan'. Dikutip dari laman Perhutani, kata 'Tanjung' disematkan dalam nama Pantai Papuma untuk menandakan posisi Pantai Papuma yang menjorok ke laut, tepatnya ke arah barat daya. Pantai Papuma dinobatkan sebagai salah satu pantai terindah di Indonesia pada TripAdvisor Traveler Choice 2015 lalu, bersanding dengan pantai-pantai kenamaan di Bali."),
                        new Destinasi("Pantai Watu Ulo", R.drawable.img_watuulo, "Ambulu, Jember", 4.8, 4, "Pantai Watu Ulo adalah salah satu destinasi wisata terkenal di Kabupaten Jember, Jawa Timur. Nama 'Watu Ulo' dalam bahasa Jawa berarti 'batu ular,' yang mengacu pada deretan batu memanjang di tepi pantai yang menyerupai bentuk tubuh ular. Pantai ini tidak hanya menawarkan keindahan alam, tetapi juga cerita legenda yang menambah daya tariknya.")
                );
                break;

            case "Bukit":
                destinasiList = Arrays.asList(
                        new Destinasi("Bukit SJ88", R.drawable.img_sj88, "Wuluhan, Jember", 4.8, 5, "Bukit SJ88 adalah destinasi wisata alam yang menarik di Kabupaten Jember, Jawa Timur. Tempat ini populer karena keindahan panoramanya yang memukau, terutama pemandangan perbukitan hijau yang asri dan udara segar yang menenangkan. Lokasi ini menjadi salah satu pilihan wisatawan untuk menikmati suasana alam yang damai sekaligus aktivitas seru di luar ruangan."),
                        new Destinasi("Rembangan", R.drawable.img_rembangan, "Arjasa, Jember", 4.8, 2, "Rembangan berada pada ketinggian 650 meter di atas permukaan laut (mdpl). Tempat wisata yang berada di kaki Gunung Argopuro ini memiliki suhu udara yang dapat mencapai 14 derajat Celcius. Pengunjung dapat memanfaatkan tempat wisata ini untuk bersantai maupun healing.")
                );
                break;

            case "Air Terjun":
                destinasiList = Arrays.asList(
                        new Destinasi("Air Terjun Tancak", R.drawable.img_airterjun_tancak, "Panti, Jember", 4.8, 7, "Air Terjun Tancak adalah salah satu destinasi wisata alam yang menakjubkan di Kabupaten Jember, Jawa Timur. Dengan keindahan air terjun yang tinggi dan suasana hutan yang asri, tempat ini menjadi pilihan favorit bagi pecinta alam dan wisatawan yang mencari pengalaman petualangan yang menenangkan."),
                        new Destinasi("Air Terjun Antrokan", R.drawable.img_airterjun_antrokan, "Tanggul, Jember", 4.8, 8, "Air Terjun Antrokan adalah salah satu destinasi wisata alam yang tersembunyi di Kabupaten Jember, Jawa Timur. Tempat ini menawarkan keindahan air terjun yang alami dan suasana yang tenang, jauh dari keramaian, sehingga cocok untuk wisatawan yang ingin menikmati kedamaian di tengah alam.")
                );
                break;



        default:
                // Default list jika tidak ada kategori yang cocok
                destinasiList = new ArrayList<>();
                break;
        }

        // Atur adapter untuk RecyclerView destinasi
        recyclerViewDestinasi.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Tetap gunakan adapter yang sama
        DestinasiAdapter destinasiAdapter = new DestinasiAdapter(requireContext(), destinasiList);
        recyclerViewDestinasi.setAdapter(destinasiAdapter);
    }
}
