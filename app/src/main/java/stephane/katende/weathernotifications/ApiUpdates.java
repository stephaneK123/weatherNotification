package stephane.katende.weathernotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import stephane.katende.weathernotifications.Startup.AlertObject;
import stephane.katende.weathernotifications.Startup.RequestSingleton;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class ApiUpdates extends Service {
    private final String NOTIFICATION_CHANNELID = "stephane.katende.weathernotifications";
    private final int notificationId = 1;
    NotificationCompat.Builder _myNotificationBuilder;
    ArrayList<AlertObject> alertArray;
    private double lat;
    private double lon;
    private String API_KEY = "a1ab34e7fdfac19880f3782401882278";
    private static final String ALERT_ARRAY_PREF = "alertArray";
    private static final String LAT_KEY = "latkey", LOG_KEY = "logkey";
    SharedPreferences prefs;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lat = prefs.getFloat(LAT_KEY, (float) 0.0);
        lon = prefs.getFloat(LOG_KEY, (float) 0.0);
        Gson gson = new Gson();
        String json = prefs.getString(ALERT_ARRAY_PREF, "[{\"_operand\":\"gt\",\"_testValue\":100.0,\"_weatherCond\":\"temp\"}]"); //todo set default alert
        alertArray = gson.fromJson(json, new TypeToken<List<AlertObject>>() {}.getType()); //very strange but works

        _myNotificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNELID);

        Timer timer = new Timer();
        TimerTask apiUpdate = new TimerTask() {
            @Override
            public void run() {
                try {
                    checkTests(alertArray, lat, lon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(apiUpdate, 01, 1000*60*60);

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }



    /**
     * Check the current values against all of the user-set tests and create notifications when they're true
     * Only runs if the api successfully responds.
     */
    private void checkTests(ArrayList<AlertObject> alertsArray, double lat, double lon) throws JSONException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&cnt=36&units=imperial&appid=" + API_KEY; //TODO CHANGE TO 96

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("api2", response.toString());
                if (alertsArray == null) {return;}
                for (int i = 0; i < alertsArray.size(); i++) {
                    Log.i("test","test");
                    try {
                        double v;
                        switch (alertsArray.get(i).getWeatherCond()) {
                            case "temp":
                                v = response.getJSONObject("main").getDouble("temp");
                                if (alertsArray.get(i).checkTestValue(v)) {
                                    createNotification("weatherAlert", alertsArray.get(i).toString(), R.drawable.notification_icon, R.drawable.notification_icon);
                                }
                                break;
                            case "feelsLike":
                                v = response.getJSONObject("main").getDouble("feels_like");
                                if (alertsArray.get(i).checkTestValue(v)) {
                                    createNotification("weatherAlert", alertsArray.get(i).toString(), R.drawable.notification_icon, R.drawable.notification_icon);
                                }
                                break;
                            case "humidity":
                                v = response.getJSONObject("main").getDouble("humidity");
                                if (alertsArray.get(i).checkTestValue(v)) {
                                    createNotification("weatherAlert", alertsArray.get(i).toString(), R.drawable.notification_icon, R.drawable.notification_icon);
                                }
                                break;
                            case "windSpeed":
                                v = response.getJSONObject("wind").getDouble("speed");
                                if (alertsArray.get(i).checkTestValue(v)) {
                                    createNotification("weatherAlert", alertsArray.get(i).toString(), R.drawable.notification_icon, R.drawable.notification_icon);
                                }
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("api", error.toString());
                return;
            }
        });

        // Access the RequestQueue through our singleton class.
        RequestSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
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
}