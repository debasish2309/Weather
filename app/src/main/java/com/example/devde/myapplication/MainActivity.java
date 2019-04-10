package com.example.devde.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.devde.myapplication.Model.Weather;
import com.example.devde.myapplication.Model.mainWeather;
import com.example.devde.myapplication.Rest.ApiClient;
import com.example.devde.myapplication.Rest.ApiInterface;

import java.awt.font.TextAttribute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String Tag = MainActivity.class.getSimpleName();
    private static final String API_KEY = "4585021ff24103527b96c064a7b0aee5";
    public static final String lat = "35";
    public static final String lon = "139";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);

        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        Call<mainWeather> call = apiservice.getCurrentWeather(lat,lon,API_KEY);
        call.enqueue(new Callback<mainWeather>() {
            @Override
            public void onResponse(Call<mainWeather> call, Response<mainWeather> response) {
                mainWeather weather =response.body();
                assert  weather != null;
                textView.setText(weather.getCoord().getLon());

            }

            @Override
            public void onFailure(Call<mainWeather> call, Throwable t) {

            }
        });


    }
}
