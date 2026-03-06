package com.project.jemberliburan.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.project.jemberliburan.R;

public class BantuanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bantuan);
        ImageView backButton = findViewById(R.id.icon_back);

        // Tambahkan listener untuk icon_back
        backButton.setOnClickListener(v -> finish());
    }
}