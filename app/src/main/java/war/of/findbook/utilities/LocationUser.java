package war.of.findbook.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;


public class LocationUser extends AsyncTask<Void, Void, Void> {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private CallbackOnLocationUpdated mListener;
    private final Context context;

    public LocationUser(Context context, CallbackOnLocationUpdated callback) {
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        this.mListener = callback;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        updateLocationWithLastLocation();
        return null;
    }


    private void updateLocationWithLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (mListener != null)
                mListener.onFailPermission();
            return;
        }
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null)
                        saveLocation(location);
                    else
                        tryAgain();
                });
    }

    private void saveLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.d("LastLocation", "Lat " + latitude + ", Long " + longitude);
        long timeStamp = System.currentTimeMillis();
        Shared_Preference_Helper.getInstance(context).setLastLocation(timeStamp, latitude, longitude);
        if (mListener != null)
            mListener.onLocationUpdate(timeStamp, latitude, longitude);
    }

    private void tryAgain() {
        startLocationUpdates();
    }


    private void startLocationUpdates() {
        float everyMeter = 5;
        long updateLocationEveryTimeMillis = 5000;
        final LocationRequest mLocationRequest = new LocationRequest()
                .setFastestInterval(10000)                   // Was 1000
                .setInterval(updateLocationEveryTimeMillis)  // Was 1000
                .setSmallestDisplacement(everyMeter)         // Was 1
                .setPriority(PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;

        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null)
                return;

            for (Location location : locationResult.getLocations()) {
                saveLocation(location);
                mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            }
        }
    };

    public interface CallbackOnLocationUpdated {
        void onLocationUpdate(long timeStamp, double latitude, double longitude);

        void onFailPermission();
    }
}
