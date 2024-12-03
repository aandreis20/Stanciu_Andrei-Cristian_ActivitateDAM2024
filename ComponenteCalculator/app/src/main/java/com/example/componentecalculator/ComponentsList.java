package com.example.componentecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
    private int modifiedId = 0;
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

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    components = componentDatabase.getDaoObject().getComponents();
                } finally {
                    handler.post(() -> {
                        adapter = new ComponentAdapter(components, getApplicationContext(), R.layout.row_item);
                        listView.setAdapter(adapter);
                    });
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent modifyIntent = new Intent(getApplicationContext(), AddComponent.class);
                modifyIntent.putExtra("component", components.get(position));
                modifiedId = position;
                startActivityForResult(modifyIntent, 403);
                Toast.makeText(ComponentsList.this, components.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                components.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==403)
            {
                components.set(modifiedId,data.getParcelableExtra("component"));
                adapter.notifyDataSetChanged();
            }
        }
    }
}