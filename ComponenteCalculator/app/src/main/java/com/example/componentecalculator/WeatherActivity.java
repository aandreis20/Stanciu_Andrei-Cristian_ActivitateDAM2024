package com.example.componentecalculator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WeatherActivity extends AppCompatActivity {
    Executor executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    String text = "";
    String cityKey = "";
    static String apiKey = "TxM94PiAb3mL0ZXErgoU5tHItQZg0eJL";

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

        EditText citySearch = findViewById(R.id.EditCity);
        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            String cityUrl = makeCityUrl(citySearch.getText().toString());
            requestWeather(cityUrl);
        });
    }

    @NonNull
    private JSONObject temperature(String response, int index) throws JSONException {
        JSONObject forecast = new JSONObject(response);
        JSONArray dailies = forecast.getJSONArray("DailyForecasts");
        JSONObject day = dailies.getJSONObject(index);
        return day.getJSONObject("Temperature");
    }

    @NonNull
    private String minTemperature(@NonNull JSONObject object) throws JSONException {
        JSONObject minimum = object.getJSONObject("Minimum");
        return minimum.getString("Value");
    }

    @NonNull
    private String maxTemperature(@NonNull JSONObject object) throws JSONException {
        JSONObject maximum = object.getJSONObject("Maximum");
        return maximum.getString("Value");
    }

    @NonNull
    private String makeCityUrl(String city) {
        return "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + apiKey + "&q=" + city;
    }

    private void handleWeatherRequest(String weatherUrl) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
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

                    text = generateWeatherText(response2.toString());

                    handler.post(() -> {
                        TextView cityKeyTextView = findViewById(R.id.cityKey);
                        cityKeyTextView.setText(text);
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
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
        });
    }

    private void requestWeather(String cityUrl) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(cityUrl);
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
                    cityKey = object.getString("Key");

                    String weatherUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + cityKey + "?apikey=" + apiKey + "&metric=true";

                    handleWeatherRequest(weatherUrl);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
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
        });

    }

    @NonNull
    private String generateWeatherText(String jsonResponse) throws JSONException {
        JSONObject temperatureObject = temperature(jsonResponse, 0);

        String valueMin = minTemperature(temperatureObject);
        String valueMax = maxTemperature(temperatureObject);

        return "Temperatura min: " + valueMin + "  Temperatura max: " + valueMax;
    }
}