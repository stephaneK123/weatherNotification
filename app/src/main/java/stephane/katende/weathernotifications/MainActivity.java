/**
 * Weather Notification App - send a notification if certain weather conditions are met
 * Created by Stephane Katande, Thomas K, and Xavier Clark
 * 2021
 * <p>
 * OpenWeatherMaps Api Key = a1ab34e7fdfac19880f3782401882278
 */
package stephane.katende.weathernotifications;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import stephane.katende.weathernotifications.Settings.PermissionFragment;
import stephane.katende.weathernotifications.Startup.RequestSingleton;
import stephane.katende.weathernotifications.Startup.SetLocationFragment;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class MainActivity extends AppCompatActivity implements LocationListener {
    FragmentManager _myFragmentManager;
    FragmentContainerView _myFragmentView;
    private final String NOTIFICATION_CHANNELID = "stephane.katende.weathernotifications";
    String mState, mCity;
    private final int notificationId = 1;
    NotificationCompat.Builder _myNotificationBuilder;
    CoordinatorLayout coordinatorLayout;
    Location myLocation;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;

    //SharedPref Keys
    private static final String ALERT_PREF_NAME = "alertPrefs";
    private static final String ALERT_ARRAY_PREF = "alertArray";
    private static final String CACHED_API_RESPONSE = "cachedJSONObject";
    private static final String API_KEY = "a1ab34e7fdfac19880f3782401882278";
    private String zip; //TODO - default, should change (be part of sharedprefs)
    private String country; //TODO - maybe have changeable? Or doesn't matter if using lat/lon

    private static final String LAT_KEY = "latkey", LOG_KEY = "logkey", STATE_KEY = "statekey", CITY_KEY = "citykey"; //is the location fragment already showed?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WeatherNotifications);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        handleEssentials();
        getMyLocation();
        setContentView(R.layout.activity_main);
        _myNotificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNELID);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        //Get shared layout stuff
        SharedPreferences prefs = getSharedPreferences(ALERT_PREF_NAME, Context.MODE_PRIVATE);
        // JSONObject apiResponse = prefs.get
        zip = "05401";
        country = "us";

        updateForecastScreen(updateApiData());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleEssentials();
    }

    /**
     * Create a notification, always uses the same channel id (updates on the same notification)
     *
     * @param title   the title of the notification
     * @param content the body content of the notification
     * @param icon    the icon of the notification
     */
    private void createNotification(String title, String content, int icon, int largeIcon) {
        //pending intent for when notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //building the notification
        _myNotificationBuilder.setSmallIcon(icon);
        _myNotificationBuilder.setContentTitle(title);
        _myNotificationBuilder.setContentText(content);
        _myNotificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        // _myNotificationBuilder.setLargeIcon(largeIcon);    //to set up
        _myNotificationBuilder.setContentIntent(pendingIntent);
        _myNotificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        _myNotificationBuilder.setCategory(NotificationCompat.CATEGORY_REMINDER);
        _myNotificationBuilder.setAutoCancel(true); //removes notification when it is pressed


        //create a channel for device O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNELID, "Get alerts notification", IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(notificationId, _myNotificationBuilder.build());
    }


    @Override
    public void onBackPressed() {
        //bring back nav
        coordinatorLayout.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }

    /**
     * Returns a JSONObject from an open weather map API call using the user's zip (us only)
     * Also calls the checkTests method if the api call successfully responds
     *
     * @return a JSONObject, probably a huge one with the four day forecast in it. Returns null if api call fails
     * TODO - Maybe swap to lat/long?
     * ***CURRENTLY FAILS - WAIT UNTIL STUDENT ACCOUNT IS APPROVED (SHOULD BE BY 5/4/2021)***
     */
    public JSONObject updateApiData() {
        String url = "https://pro.openweathermap.org/data/2.5/forecast/hourly?zip=" + zip + "," + country + "&appid=" + API_KEY;

        final JSONObject[] jsonObj = new JSONObject[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("api", response.toString());
                checkTests(response);
                jsonObj[0] = response;
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("api", "shit's broke, sorry");
                jsonObj[0] = null;
            }
        });

        // Access the RequestQueue through our singleton class.
        RequestSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        return jsonObj[0];

    }

    /**
     * Check the current values against all of the user-set tests and create notifications when they're true
     * Only runs if the api successfully responds.
     *
     * @param response the api response, a huge 4 day hourly forecast. Probably overkill but we'll only use the first part.
     */
    private void checkTests(JSONObject response) {

    }

    /**
     * update all of the textviews in ForecastFragment using the api response (or a cached version).
     *
     * @param response the api response. May be null, if so will used cached JSONObject or default (all '0's);
     */
    public void updateForecastScreen(JSONObject response) {

    }

    /**
     * Check if the location permissions are enabled
     */
    public void handleEssentials() {
        //are permissions enabled?
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//permissions not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        //is location enabled?
        boolean gpsOn = false, networkOn = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            gpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {

        }

        if (!gpsOn && !networkOn) {
            new AlertDialog.Builder(this)
                    .setTitle("Please enable GPS")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Nah", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }).show();
        }
    }

    private void getMyLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, this);
        } catch (SecurityException e) {
        }

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        myLocation = location;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            sharedPreferences.edit()
                    .putString(CITY_KEY, addressList.get(0).getLocality())
                    .putString(STATE_KEY, String.valueOf(addressList.get(0).getAdminArea()))
                    .putString(LAT_KEY, String.valueOf(location.getLatitude()))
                    .putString(LOG_KEY, String.valueOf(location.getLongitude())).
                    apply();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}