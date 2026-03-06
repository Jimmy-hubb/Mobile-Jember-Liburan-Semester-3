        package com.project.jemberliburan.fragment;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.project.jemberliburan.R;
        import com.project.jemberliburan.activity.CekTiketActivity;
        import com.project.jemberliburan.activity.RiwayatTiketActivity;
        import com.project.jemberliburan.adapter.TiketAdapter;
        public class TiketFragment extends Fragment implements TiketAdapter.OnItemClickListener {

            @Nullable
            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_tiket, container, false);

                // Inisialisasi RecyclerView
                RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTiket);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                // Set Adapter dengan listener
                TiketAdapter adapter = new TiketAdapter(getContext(), this);
                recyclerView.setAdapter(adapter);

                return view;
            }

            @Override
            public void onItemClick(int position) {
                // Gunakan switch case untuk berpindah aktivitas
                switch (position) {
                    case 0:
                        startActivity(new Intent(getContext(), CekTiketActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getContext(), RiwayatTiketActivity.class));
                        break;
                    default:
                        break;
                }
            }
        }
