package com.example.componentecalculator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ImagesList extends AppCompatActivity {
    List<ComponentImage> imagesList = null;
    private ComponentImage componentImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_images_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imagesList = new ArrayList<>();
        ListView listView = findViewById(R.id.images);
        ImageAdapter adapter = new ImageAdapter(imagesList, getApplicationContext(), R.layout.image);
        listView.setAdapter(adapter);

        List<String> links = Arrays.asList(
                "https://view.livresq.com/view/60d30126977dd3000789e917/data/static/media/12.jpeg?b=224204",
                "https://devicebox.ro/wp-content/uploads/2014/01/54.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Gigabyte_G1.Sniper_M5_20130910.jpg/440px-Gigabyte_G1.Sniper_M5_20130910.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwWfJTyw7J0jVLm6_AE7FB2rHirP3fNLQvDA&s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnD5MZ9e4IZkBZkUe1fA-xtbBQwp-MR4noSw&s"
        );

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper()));

        for (int i = 0; i < links.size(); i++) {
            int finalI = i;
            executor.execute(() -> {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(links.get(finalI));
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    switch (finalI) {
                        case 0: componentImage = new ComponentImage("CPU", bitmap ,"https://en.wikipedia.org/wiki/Central_processing_unit");
                        case 1: componentImage = new ComponentImage("RAM", bitmap ,"https://en.wikipedia.org/wiki/Random-access_memory");
                        case 2: componentImage = new ComponentImage("Motherboard", bitmap, "https://en.wikipedia.org/wiki/Motherboard");
                        case 3: componentImage = new ComponentImage("GPU", bitmap, "https://en.wikipedia.org/wiki/Graphics_processing_unit");
                        case 4: componentImage = new ComponentImage("Computer Case", bitmap, "https://en.wikipedia.org/wiki/Computer_case");
                    }
                    if(componentImage != null) {
                        handler.post(() -> {
                            imagesList.add(componentImage);
                            adapter.notifyDataSetChanged();
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("link", imagesList.get(position).getLink());
                startActivity(intent);
            }
        });
    }
}