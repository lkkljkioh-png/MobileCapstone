package com.example.capstone_2_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    String[] certList = {
            "ADsP",
            "정보처리기사",
            "컴활 1급"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.cert_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                certList
        );

        listView.setAdapter(adapter);

        // 🔥 클릭하면 상세페이지 이동
        listView.setOnItemClickListener((parent, view, position, id) -> {

            String selectedCert = certList[position];

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("cert_name", selectedCert);

            startActivity(intent);
        });
    }
}