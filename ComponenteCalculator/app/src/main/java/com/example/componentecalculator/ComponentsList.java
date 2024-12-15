package com.example.componentecalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ComponentsList extends AppCompatActivity {
    private List<Component> components = null;
    private ComponentAdapter adapter = null;
    private ComponentDatabase componentDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_components_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ListView listView = findViewById(R.id.componentsListView);
        componentDatabase = Room.databaseBuilder(this, ComponentDatabase.class, "ComponentsDB").build();
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                components = componentDatabase.getDaoObject().getComponents();
            } finally {
                handler.post(() -> {
                    adapter = new ComponentAdapter(components, this, R.layout.row_item);
                    listView.setAdapter(adapter);
                });
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("ComponentsList", "Item clicked: " + position);
            Toast.makeText(this, "Componenta a fost adăugată la favorite!", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveToFavorites(Component component) {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("Favorites_list", null);
        Type type = new TypeToken<ArrayList<Component>>() {}.getType();
        ArrayList<Component> favoritesList = gson.fromJson(json, type);

        if (favoritesList == null) {
            favoritesList = new ArrayList<>();
        }

        favoritesList.add(component);

        String updatedJson = gson.toJson(favoritesList);
        editor.putString("Favorites_list", updatedJson);
        editor.apply();

        Toast.makeText(this, "favorite component", Toast.LENGTH_SHORT).show();
    }
}