package rs.ac.bg.etf.running.workouts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;
import rs.ac.bg.etf.running.MainActivity;
import rs.ac.bg.etf.running.rest.OpenWeatherMapService;

import javax.inject.Inject;

public class LifecycleAwareLocator implements DefaultLifecycleObserver {

    @Inject
    public LifecycleAwareLocator() {

    }

    public void getLocation(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(MainActivity.LOG_TAG, "permission not granted");
            return;
        }

        FusedLocationProviderClient locationProviderClient =
                LocationServices.getFusedLocationProviderClient(context);

        Task<Location> task = locationProviderClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                new CancellationTokenSource().getToken());

        task.addOnSuccessListener(location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d(MainActivity.LOG_TAG, "lat:" + latitude + " lon:" + longitude);

                OpenWeatherMapService openWeatherMapService = new OpenWeatherMapService();
                openWeatherMapService.getCurrentWeather(latitude, longitude);
            }
        });
    }
}
