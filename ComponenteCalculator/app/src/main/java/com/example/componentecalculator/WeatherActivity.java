package com.example.componentecalculator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WeatherActivity extends AppCompatActivity {
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        EditText citySearch = findViewById(R.id.EditCity);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = citySearch.getText().toString();
                String apiKey = "9YzUUfT2jINdALL8auzA2qWKxBGCOnuq";

                String urlString = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + apiKey + "&q=" + city;

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            URL url = new URL(urlString);
                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(10000);
                            connection.setReadTimeout(10000);

                            int responseCode = connection.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }

                                JSONArray jsonResponse = new JSONArray(response.toString());
                                JSONObject object = jsonResponse.getJSONObject(0);
                                String cityKey = object.getString("Key");

                                String weatherUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + cityKey + "?apikey=9YzUUfT2jINdALL8auzA2qWKxBGCOnuq&metric=true";

                                try {
                                    URL url2 = new URL(weatherUrl);
                                    connection = (HttpURLConnection) url2.openConnection();
                                    int responseCode2 = connection.getResponseCode();
                                    if (responseCode2 == HttpURLConnection.HTTP_OK) {
                                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                        StringBuilder response2 = new StringBuilder();
                                        String line2;
                                        while ((line2 = reader.readLine()) != null) {
                                            response2.append(line2);
                                        }
                                        JSONObject object1 = new JSONObject(response2.toString());
                                        JSONArray array1 = object1.getJSONArray("DailyForecasts");

                                        JSONObject object2 = array1.getJSONObject(0);
                                        JSONObject object3 = object2.getJSONObject("Temperature");
                                        JSONObject minimum = object3.getJSONObject("Minimum");
                                        JSONObject maximum = object3.getJSONObject("Maximum");
                                        String valueMin = minimum.getString("Value");
                                        String valueMax = maximum.getString("Value");

                                        text = "Temperatura min: " + valueMin + "  Temperatura max: " + valueMax;
                                    }
                                } finally {
                                    handler.post(() -> {
                                        TextView cityKeyTextView = findViewById(R.id.cityKey);
                                        cityKeyTextView.setText(text);
                                    });
                                }
                            }
                        } catch (Exception e) {
                            handler.post(() -> {
                                TextView cityKeyTextView = findViewById(R.id.cityKey);
                                cityKeyTextView.setText("Error: " + e.getMessage());
                                throw new RuntimeException(e.getMessage());
                            });
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (Exception ignored) {}
                            }
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                    }
                });
            }
        });
    }
}