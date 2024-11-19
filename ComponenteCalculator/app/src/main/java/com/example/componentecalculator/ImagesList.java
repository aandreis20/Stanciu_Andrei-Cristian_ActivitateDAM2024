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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ImagesList extends AppCompatActivity {
    List<ComponentImage> imagesList = null;

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

        List<Bitmap> images = new ArrayList<>();
        List<String> links = new ArrayList<>();
        links.add("https://view.livresq.com/view/60d30126977dd3000789e917/data/static/media/12.jpeg?b=224204");
        links.add("https://devicebox.ro/wp-content/uploads/2014/01/54.jpg");
        links.add("https://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Gigabyte_G1.Sniper_M5_20130910.jpg/440px-Gigabyte_G1.Sniper_M5_20130910.jpg");
        links.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwWfJTyw7J0jVLm6_AE7FB2rHirP3fNLQvDA&s");
        links.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnD5MZ9e4IZkBZkUe1fA-xtbBQwp-MR4noSw&s");

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (String link : links) {
                        HttpURLConnection connection = null;
                        try {
                            URL url = new URL(link);
                            connection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = connection.getInputStream();
                            synchronized (images) {
                                images.add(BitmapFactory.decodeStream(inputStream));
                            }
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                    }
                } finally {
                    handler.post(() -> {
                        imagesList = new ArrayList<>();
                        imagesList.add(new ComponentImage("CPU", images.get(0), "https://en.wikipedia.org/wiki/Central_processing_unit"));
                        imagesList.add(new ComponentImage("RAM", images.get(1), "https://en.wikipedia.org/wiki/Random-access_memory"));
                        imagesList.add(new ComponentImage("Motherboard", images.get(2), "https://en.wikipedia.org/wiki/Motherboard"));
                        imagesList.add(new ComponentImage("GPU", images.get(3), "https://en.wikipedia.org/wiki/Graphics_processing_unit"));
                        imagesList.add(new ComponentImage("Computer case", images.get(4), "https://en.wikipedia.org/wiki/Computer_case"));

                        ListView listView = findViewById(R.id.images);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                                intent.putExtra("link", imagesList.get(position).getLink());
                                startActivity(intent);
                            }
                        });
                        ImageAdapter adapter = new ImageAdapter(imagesList, getApplicationContext(), R.layout.image);
                        listView.setAdapter(adapter);
                    });
                }
            }
        });
    }
}