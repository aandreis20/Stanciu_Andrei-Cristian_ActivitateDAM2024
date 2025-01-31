package com.example.componentecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Component> components;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("components");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MainActivity.this, 
                    "Baza de date Firebase a fost actualizată!", 
                    Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, 
                    "Eroare la citirea din Firebase: " + error.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });

        components = new ArrayList<>();
        components.add(new Component("Intel Pentium", "CPU", 100, true, 5));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button favoritesButton = findViewById(R.id.favorites_button);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavoritesList.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_add_component) {
                Intent intentAddComponent = new Intent(getApplicationContext(), AddComponent.class);
                startActivityForResult(intentAddComponent, 403);
                return true;
            } else if(item.getItemId() == R.id.nav_components_list) {
                Intent intentComponentsList = new Intent(getApplicationContext(), ComponentsList.class);
                intentComponentsList.putParcelableArrayListExtra("components", (ArrayList<? extends Parcelable>) components);
                startActivity(intentComponentsList);
                return true;
            } else if(item.getItemId() == R.id.nav_images) {
                Intent intentImages = new Intent(getApplicationContext(), ImagesList.class);
                startActivity(intentImages);
                return true;
            } else if(item.getItemId() == R.id.nav_weather) {
                Intent intentWeather = new Intent(getApplicationContext(), WeatherActivity.class);
                startActivity(intentWeather);
                return true;
            } else if(item.getItemId() == R.id.nav_firebase_list) {
                Intent intent = new Intent(MainActivity.this, FirebaseListActivity.class);
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 403 && resultCode == RESULT_OK && data != null) {
            Component component = data.getParcelableExtra("component");
            if (component != null) {
                components.add(component);
            }
        }
    }
}
