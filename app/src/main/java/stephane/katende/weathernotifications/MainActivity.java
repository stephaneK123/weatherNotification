/**
 * Weather Notification App - send a notification if certain weather conditions are met
 * Created by Stephane Katande, Thomas K, and Xavier Clark
 * 2021
 *
 * OpenWeatherMaps Api Key = a1ab34e7fdfac19880f3782401882278
 */
package stephane.katende.weathernotifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import stephane.katende.weathernotifications.Startup.RequestSingleton;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {
    FragmentManager _myFragmentManager;
    FragmentContainerView _myFragmentView;
    private final String NOTIFICATION_CHANNELID = "stephane.katende.weathernotifications";
    private final int notificationId = 1;
    NotificationCompat.Builder _myNotificationBuilder;
    CoordinatorLayout coordinatorLayout;

    //SharedPref Keys
    private static final String ALERT_PREF_NAME = "alertPrefs";
    private static final String ALERT_ARRAY_PREF = "alertArray";
    private static final String CACHED_API_RESPONSE = "cachedJSONObject";

    private static final String API_KEY = "a1ab34e7fdfac19880f3782401882278";
    private String zip; //TODO - default, should change (be part of sharedprefs)
    private String country; //TODO - maybe have changeable? Or doesn't matter if using lat/lon


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WeatherNotifications);
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
     * @return a JSONObject, probably a huge one with the four day forecast in it. Returns null if api call fails
     * TODO - Maybe swap to lat/long?
     * ***CURRENTLY FAILS - WAIT UNTIL STUDENT ACCOUNT IS APPROVED (SHOULD BE BY 5/4/2021)***
     */
    public JSONObject updateApiData(){
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
                Log.e("api","shit's broke, sorry");
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
     * @param response the api response, a huge 4 day hourly forecast. Probably overkill but we'll only use the first part.
     */
    private void checkTests(JSONObject response) {

    }

    /**
     * update all of the textviews in ForecastFragment using the api response (or a cached version).
     * @param response the api response. May be null, if so will used cached JSONObject or default (all '0's);
     */
    public void updateForecastScreen(JSONObject response){

    }
}