package war.of.findbook.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.security.Provider;

public class Shared_Preference_Helper {

    private static final String APP_SETTINGS = "APP_SETTINGS";
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String TIME_LAST_LOCATION = "TIME_LAST_LOCATION";
    private static final String LATITUDE_LAST_LOCATION = "LATITUDE_LAST_LOCATION";
    private static final String LONGITUDE_LAST_LOCATION = "LONGITUDE_LAST_LOCATION";
    private final SharedPreferences sharedPreferences;
    private static Shared_Preference_Helper instance;


    public static Shared_Preference_Helper getInstance(Context context) {
        if (instance == null)
            instance = new Shared_Preference_Helper(context);

        return instance;
    }

    private Shared_Preference_Helper(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public void logOut() {
        sharedPreferences.edit().clear().apply();
    }

    public void storeUserID(String uid) {
        String uidToFirebaseKey=uid.replaceAll("@","_").replaceAll("\\.","_").replaceAll("\\$","_").replaceAll("\\+","_").replaceAll(" ","_");
         sharedPreferences.edit().putString(USER_ID, uidToFirebaseKey).commit();
    }


    public String getUID() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public void storeUserName(String userName) {
        sharedPreferences.edit().putString(USER_NAME, userName).commit();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, null);
    }

    public void setLastLocation(long timeStamp, double latitude, double longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME_LAST_LOCATION, timeStamp).apply();
        editor.putString(LATITUDE_LAST_LOCATION, String.valueOf(latitude)).apply();
        editor.putString(LONGITUDE_LAST_LOCATION, String.valueOf(longitude)).apply();
    }

    public Location getLastLocation() {
        Location lastLocationIKnow = null;
        long timeMillis = sharedPreferences.getLong(TIME_LAST_LOCATION, -1);
        double latitude = -1, longitude = -1;
        try {
            latitude = Double.parseDouble(sharedPreferences.getString(LATITUDE_LAST_LOCATION, ""));
        } catch (Exception e) {
            e.getMessage();
            e.getStackTrace();
        }
        try {
            longitude = Double.parseDouble(sharedPreferences.getString(LONGITUDE_LAST_LOCATION, ""));
        } catch (Exception e) {
            e.getMessage();
            e.getStackTrace();
        }
        if (timeMillis != -1 && latitude != -1 && longitude != -1) {
            lastLocationIKnow =   new Location(LocationManager.GPS_PROVIDER);

            lastLocationIKnow.setLatitude(latitude);
            lastLocationIKnow.setLongitude(longitude);
        }
        return lastLocationIKnow;
    }

}
