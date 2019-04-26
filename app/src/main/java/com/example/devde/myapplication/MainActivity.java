package com.example.devde.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.squareup.picasso.Picasso;

import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private static final String Tag = MainActivity.class.getSimpleName();
    private static final String API_KEY = "4585021ff24103527b96c064a7b0aee5";
    public static final String url = "https://openweathermap.org/img/w/";
    TextView textView;
    ImageView imageicon, imageicon2;
    TextView locationCurrent, temprature, date, windspeed, probablity, time;
    TextView humidity, sunset, sunrise, precipitation, pressure, uvIndex, windspeed2, maxTemp, mintemp;
    FusedLocationProviderClient client;

    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        imageicon = findViewById(R.id.iv_temp_icon);
        imageicon2 = findViewById(R.id.iv_tempreatureicon);

        geocoder = new Geocoder(this, Locale.getDefault());

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
                    probablity.setText("lat : "+location.getLatitude() + " , "+"lng : "+location.getLongitude());

                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        String address = addresses.get(0).getAddressLine(0);
                        String area = addresses.get(0).getLocality();
                        String city = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalcode = addresses.get(0).getPostalCode();

                        locationCurrent.setText(address );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
                    Call<mainWeather> call = apiservice.getCurrentWeather(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),API_KEY);
                    call.enqueue(new Callback<mainWeather>() {
                        @Override
                        public void onResponse(Call<mainWeather> call, Response<mainWeather> response) {
                            mainWeather weather =response.body();
                            assert  weather != null;
                            float currentTemp = (float) (Double.valueOf(weather.getMain().getTemp()) - 273.15);
                            temprature.setText(String.format("%.2f",currentTemp) + "°C");
                            DateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
                            String dates = df.format(Calendar.getInstance().getTime());
                            date.setText(dates);
                            windspeed.setText(weather.getWind().getSpeed() + "m/s");
                    //        probablity.setText(weather.getSys().getCountry());
                            DateFormat tf = new SimpleDateFormat("h:mm a");
                            String times = tf.format(Calendar.getInstance().getTime());
                            time.setText(times);
                            humidity.setText(weather.getMain().getHumidity() + "%");
                            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                            String sunrisetime = formatter.format(new Date(Long.parseLong(weather.getSys().getSunrise())));
                            sunrise.setText(sunrisetime);
                            String sunsettime = formatter.format(new Date(Long.parseLong(weather.getSys().getSunset())));
                            sunset.setText(sunsettime);
                            precipitation.setText(weather.getWeather().get(0).getMain());
                            pressure.setText(weather.getMain().getPressure() + "hpa");
                            uvIndex.setText(weather.getWeather().get(0).getDescription());
                            windspeed2.setText(weather.getWind().getSpeed() +"m/s");
                            float maxtemp = (float) (Double.valueOf(weather.getMain().getTemp_max()) - 273.15);
                            maxTemp.setText(String.format("%.2f",maxtemp) + "°C");
                            float minTemp = (float) (Double.valueOf(weather.getMain().getTemp_min()) - 273.15);
                            mintemp.setText(String.format("%.2f",minTemp) + "°C");
                            Picasso.get()
                                    .load(url + weather.getWeather().get(0).getIcon()+".png")
                                    .into(imageicon);
                            Picasso.get()
                                    .load(url + weather.getWeather().get(0).getIcon()+".png")
                                    .into(imageicon2);
                            Log.i("!!!icon",weather.getWeather().get(0).getIcon());



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
