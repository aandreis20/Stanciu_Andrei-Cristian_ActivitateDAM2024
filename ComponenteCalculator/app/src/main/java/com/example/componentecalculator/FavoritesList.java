package com.example.componentecalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoritesList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.favoritesListView);
        SharedPreferences sharedPreferences = getSharedPreferences("FavoritesList", MODE_PRIVATE);
        String json = sharedPreferences.getString("FavoritesList", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Component>>() {}.getType();
            List<Component> favoriteComponents = gson.fromJson(json, type);

            if (favoriteComponents != null && !favoriteComponents.isEmpty()) {
                ComponentAdapter adapter = new ComponentAdapter(favoriteComponents, getApplicationContext(), R.layout.row_item);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Nu sunt componente favorite", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Nu sunt componente favorite", Toast.LENGTH_LONG).show();
        }
    }
}