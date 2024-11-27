package com.example.componentecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Component> components;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        components = new ArrayList<>();
        components.add(new Component("Intel Pentium", "CPU", 100, true, 5));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
