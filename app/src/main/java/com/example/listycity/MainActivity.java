package com.example.listycity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AddCityFragment.AddCityDialogListener {

    ListView cityList;

    ArrayList<City> dataList;
    CityArrayAdapter cityAdapter;

    // Firestore
    private FirebaseFirestore db;
    private CollectionReference citiesRef;
    private ListenerRegistration citiesListener;

    @Override
    public void addCity(City city) {
        // Write to Firestore (NOT local list)
        Map<String, Object> doc = new HashMap<>();
        doc.put("name", city.getName());
        doc.put("province", city.getProvince());

        citiesRef.add(doc)
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to add city", Toast.LENGTH_SHORT).show()
                );
        // List updates automatically via snapshot listener
    }

    private void deleteCityFromFirestore(City city) {
        if (city.getDocId() == null) return;

        citiesRef.document(city.getDocId()).delete()
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to delete city", Toast.LENGTH_SHORT).show()
                );
        // List updates automatically via snapshot listener
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);

        dataList = new ArrayList<>();
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // Firestore setup
        db = FirebaseFirestore.getInstance();
        citiesRef = db.collection("cities");

        // Real-time sync with Firestore (persistence after restart)
        citiesListener = citiesRef.addSnapshotListener((snap, e) -> {
            if (e != null || snap == null) return;

            dataList.clear();

            for (QueryDocumentSnapshot doc : snap) {
                String name = doc.getString("name");
                String province = doc.getString("province");

                // handle old docs that may not have province
                if (province == null) province = "";

                if (name != null) {
                    dataList.add(new City(doc.getId(), name, province));
                }
            }

            cityAdapter.notifyDataSetChanged();
        });

        // Long press any row to delete (persistent delete)
        cityList.setOnItemLongClickListener((parent, view, position, id) -> {
            City c = dataList.get(position);
            deleteCityFromFirestore(c);
            return true;
        });

        // FAB opens AddCity dialog
        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(v -> new AddCityFragment().show(getSupportFragmentManager(), "Add City"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (citiesListener != null) citiesListener.remove();
    }
}