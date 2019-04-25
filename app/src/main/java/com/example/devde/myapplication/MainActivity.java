package com.example.devde.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devde.myapplication.Model.Weather;
import com.example.devde.myapplication.Model.mainWeather;
import com.example.devde.myapplication.Rest.ApiClient;
import com.example.devde.myapplication.Rest.ApiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.awt.font.TextAttribute;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private static final String Tag = MainActivity.class.getSimpleName();
    private static final String API_KEY = "4585021ff24103527b96c064a7b0aee5";
    static  String lat = "22.9219945";
    static String lon = "88.3820472";
    TextView textView;
    ImageView imageicon, imageicon2;
    TextView locationCurrent, temprature, date, windspeed, probablity, time;
    TextView humidity, sunset, sunrise, precipitation, pressure, uvIndex, windspeed2, maxTemp, mintemp;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        imageicon = findViewById(R.id.iv_temp_icon);
        imageicon2 = findViewById(R.id.iv_tempreatureicon);

        locationCurrent = findViewById(R.id.tv_Location);
        temprature = findViewById(R.id.tv_degree);
        date = findViewById(R.id.tv_date);
        windspeed = findViewById(R.id.tv_wind);
        probablity = findViewById(R.id.tv_probablety);
        time = findViewById(R.id.tv_time);
        humidity = findViewById(R.id.tv_humidity);
        sunrise = findViewById(R.id.tv_sunrise);
        sunset = findViewById(R.id.tv_sunset);
        precipitation = findViewById(R.id.tv_precipitation);
        pressure = findViewById(R.id.tv_pressure);
        uvIndex = findViewById(R.id.tv_uvIndex);
        windspeed2 = findViewById(R.id.tv_windSpeed);
        maxTemp = findViewById(R.id.tv_max_temp);
        mintemp = findViewById(R.id.tv_min_temp);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    locationCurrent.setText(location.getLatitude() + " , "+location.getLongitude());

                    ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
                    Call<mainWeather> call = apiservice.getCurrentWeather(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),API_KEY);
                    call.enqueue(new Callback<mainWeather>() {
                        @Override
                        public void onResponse(Call<mainWeather> call, Response<mainWeather> response) {
                            mainWeather weather =response.body();
                            assert  weather != null;
                            temprature.setText(weather.getMain().getTemp());
                            DateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
                            String dates = df.format(Calendar.getInstance().getTime());
                            date.setText(dates);
                            windspeed.setText(weather.getWind().getSpeed());
                            probablity.setText(weather.getSys().getCountry());
                            DateFormat tf = new SimpleDateFormat("h:mm a");
                            String times = tf.format(Calendar.getInstance().getTime());
                            time.setText(times);
                            humidity.setText(weather.getMain().getHumidity());
                            sunrise.setText(weather.getSys().getSunrise());
                            sunset.setText(weather.getSys().getSunset());
                            precipitation.setText(weather.getWeather().get(0).getMain());
                            pressure.setText(weather.getMain().getPressure());
                            uvIndex.setText(weather.getWeather().get(0).getDescription());
                            windspeed2.setText(weather.getWind().getSpeed());
                            maxTemp.setText(weather.getMain().getTemp_max());
                            mintemp.setText(weather.getMain().getTemp_min());
                        }
                        @Override
                        public void onFailure(Call<mainWeather> call, Throwable t) {

                        }
                    });

                }
            }
        });






    }
}
