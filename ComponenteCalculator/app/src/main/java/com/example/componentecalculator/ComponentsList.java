package com.example.componentecalculator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

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
    }
}