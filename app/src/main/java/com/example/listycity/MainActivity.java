package com.example.listycity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    EditText cityInput;
    Button btnAddCity, btnDeleteCity, btnConfirm;

    ArrayList<String> dataList;
    ArrayAdapter<String> cityAdapter;

    int selectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);
        cityInput = findViewById(R.id.city_input);
        btnAddCity = findViewById(R.id.btn_add_city);
        btnDeleteCity = findViewById(R.id.btn_delete_city);
        btnConfirm = findViewById(R.id.btn_confirm);

        String[] cities = {
                "Edmonton", "Vancouver", "Moscow",
                "Sydney", "Berlin", "Vienna"
        };

        dataList = new ArrayList<>(Arrays.asList(cities));

        cityAdapter = new ArrayAdapter<>(
                this,
                R.layout.content,
                dataList
        );

        cityList.setAdapter(cityAdapter);

        cityList.setOnItemClickListener((parent, view, position, id) -> {
            selectedIndex = position;
        });

        btnAddCity.setOnClickListener(v -> {
            cityInput.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.VISIBLE);
        });

        btnConfirm.setOnClickListener(v -> {
            String newCity = cityInput.getText().toString();

            if (!newCity.isEmpty()) {
                dataList.add(newCity);
                cityAdapter.notifyDataSetChanged();
                cityInput.setText("");
            }

            cityInput.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
        });

        btnDeleteCity.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                dataList.remove(selectedIndex);
                cityAdapter.notifyDataSetChanged();
                selectedIndex = -1;
            }
        });
    }
}
