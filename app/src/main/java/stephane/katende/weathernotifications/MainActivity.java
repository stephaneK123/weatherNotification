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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import stephane.katende.weathernotifications.Forcast.ForecastViewHelper;
import stephane.katende.weathernotifications.Startup.AlertObject;
import stephane.katende.weathernotifications.Startup.RequestSingleton;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class MainActivity extends AppCompatActivity implements LocationListener {
    FragmentManager _myFragmentManager;
    FragmentContainerView _myFragmentView;
    private final String NOTIFICATION_CHANNELID = "stephane.katende.weathernotifications";
    private final int notificationId = 1;
    NotificationCompat.Builder _myNotificationBuilder;
    CoordinatorLayout coordinatorLayout;
    Location myLocation;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;

    //SharedPref Keys
    private static final String ALERT_ARRAY_PREF = "alertArray";
    private static final String LAT_KEY = "latkey", LOG_KEY = "logkey", STATE_KEY = "statekey", CITY_KEY = "citykey"; //is the location fragment already showed?

    private static final String API_KEY = "a1ab34e7fdfac19880f3782401882278";
    private double lat;
    private double lon;

    JSONObject apiResponse;
    ArrayList<AlertObject> alertsArray;

    ArrayList<String> dayOne, dayTwo, dayThree, dayFour;
    ArrayList<ForecastViewHelper> _myArrayForecast;

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
        lat = sharedPreferences.getFloat(LAT_KEY, (float) 0.0);
        lon = sharedPreferences.getFloat(LOG_KEY, (float) 0.0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(ALERT_ARRAY_PREF, "[{\"_operand\":\"gt\",\"_testValue\":100.0,\"_weatherCond\":\"temp\"}]"); //todo set default alert
        alertsArray = gson.fromJson(json, new TypeToken<List<AlertObject>>() {}.getType()); //very strange but works

        //addAlert("temp","gt", 20); //TODO sample add alert, should be removed
        Log.i("fuck", "shit");

        updateApiData();

        startService(new Intent(getApplicationContext(),ApiUpdates.class));

    }

    /**
     * creates a new alertObject and adds it to alertsArray
     * These read as "WeatherCond Operand TestValue", i.e. "Humidity less than 98[%]" or "temperature greater than 45F"
     *
     * @param weatherCond the weather condition you'd like to test whenever an api call is made. They can include:
     *                    "temp", "feelsLike", "humidity", & "windSpeed" (case sensitive)
     *                    should only be one of these, shouldn't be empty or null.
     * @param operand the way you'd like to compare the weather condition to your testing value(n). They include:
     *                "gt" -> ">"
     *                "lt" -> "<"
     *                "eq" -> "=="
     *                should only be one of these (case sensitive), shouldn't be empty or null.
     * @param value The value you'd like to test against.
     */
    private void addAlert(String weatherCond, String operand, double value) {
        alertsArray.add(new AlertObject(weatherCond,operand,value));
    }

    /**
     * removes the AlertObject at the given index from alertsArray
     * @param index the index of the object you'd like to remove
     */
    private void removeAlert(int index){
        alertsArray.remove(index);
    }

    /**
     * removes an AlertObject from the alertsArray given the object exists within it.
     * @param obj an example of the object you'd like to remove (doesn't have to be the exact one, I think).
     *            don't quote me on that last part tho
     */
    private void removeAlert(AlertObject obj){
        if (alertsArray.contains(obj)){
            alertsArray.remove(obj);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleEssentials();
    }

    /**
     * Store apiResponse in file and the alertsArray in shared prefs on app close.
     */
    @Override
    protected void onStop() {
        super.onStop();

        saveToFileSystem(apiResponse);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alertsArray);
        prefsEditor.putString(ALERT_ARRAY_PREF, json);
        prefsEditor.commit();

        /*** should automatically save lat + long to sharedPrefs ***/
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
     */
    public void updateApiData() {
        String url = "https://pro.openweathermap.org/data/2.5/forecast/hourly?lat=" + lat + "&lon=" + lon + "&cnt=36&units=imperial&appid=" + API_KEY; //TODO CHANGE TO 96

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("api", response.toString());
                saveToFileSystem(response);

                try {
                    checkTests();
                    updateForecastScreen(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("api", error.toString());
                JSONObject jsonObject = readFromFileSystem();
                try {
                    updateForecastScreen(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Access the RequestQueue through our singleton class.
        RequestSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * save an object to a file - will be stored in cachedApiResponse.bin
     * @param object the object you'd like to save, shouldn't be null, could be empty?
     * TODO Doesn't work right now, need to fix
     */
    public synchronized void saveToFileSystem(JSONObject object) {
        try {
            String tempPath = getApplicationContext().getFilesDir() + "/cachedApiResponse.bin";
            File file = new File(tempPath);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(object);
            oos.flush();
            oos.close();
            Log.i("caching", "saveToFileSystem: saved object to " + tempPath);
        } catch (Exception e) {
            Log.e("caching", "Couldn't save object to file: " + e.toString());
        }
    }

    /**
     * read cached api response from file and return as a JSONObject. Should work?
     * @return a cached JSONObject, will be null if no file exists (or error)
     * TODO Doesn't work right now, need to fix
     */
    public synchronized JSONObject readFromFileSystem() {
        JSONObject obj;
        try {
            String tempPath = getApplicationContext().getFilesDir() + "/cachedApiResponse.bin";
            File file = new File(tempPath);
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                obj = (JSONObject) ois.readObject();
                ois.close();
                Log.i("caching", "readFromFileSystem: read object from " + tempPath);
            } else {
                obj = null;
            }
        } catch (Exception e) {
            Log.e("caching", "Couldn't read object to file: " + e.toString());
            obj = null;
        }

        return obj;
    }


    /**
     * Check the current values against all of the user-set tests and create notifications when they're true
     * Only runs if the api successfully responds.
     */
    private void checkTests() throws JSONException {
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
     * update all of the textviews in ForecastFragment using the api response (or a cached version).
     *
     * @param response the api response. May be null, so will default to all '0's
     */
    public void updateForecastScreen(JSONObject response) throws JSONException {
        if (response == null) {
            //fill all slots with '0' - I'm unsure about how to actually get the strings into his textviews.
        } else {
            doSomeMagic();//set up recycler view
            JSONArray array = response.getJSONArray("list");

            /*** DAY ONE **/
            for (int i = 0; i <= 23; i += 2) { //format   current temp, date, highlow, temp, feelsLike, condition, wind, humidity  only first pos had HEADER!

                JSONArray weatherArray = array.getJSONObject(i).getJSONArray("weather");
                JSONObject windsArray = response.getJSONArray("list").getJSONObject(i).getJSONObject("wind");

                if (i == 0) {//do headers
                    //            getting all the headers stuff current temp, date, highlow
                    dayOne.add(array.getJSONObject(i).getJSONObject("main").getString("temp"));
                    dayOne.add(array.getJSONObject(i).getString("dt_txt"));
                    dayOne.add(array.getJSONObject(i).getJSONObject("main").getString("temp_max") + "/" + array.getJSONObject(i).getJSONObject("main").getString("temp_min"));
                }

                //content  temp, feelsLike, condiction, wind, humidity
                dayOne.add(array.getJSONObject(i).getJSONObject("main").getString("temp"));
                dayOne.add(array.getJSONObject(i).getJSONObject("main").getString("feels_like"));
                dayOne.add(weatherArray.getJSONObject(i).getString("main"));
                dayOne.add(windsArray.getString("speed") + "mph");
                dayOne.add(array.getJSONObject(i).getJSONObject("main").getString("humidity"));

            }

            /*** DAY TWO **/
            for (int i = 24; i <= 47; i += 2) { //format   current temp, date, highlow, temp, feelsLike, condition, wind, humidity  only first pos had HEADER!
                JSONArray weatherArray = array.getJSONObject(i).getJSONArray("weather");
                JSONObject windsArray = response.getJSONArray("list").getJSONObject(i).getJSONObject("wind");

                if (i == 24) {//do headers
                    //            getting all the headers stuff current temp, date, highlow
                    dayTwo.add(array.getJSONObject(i).getJSONObject("main").getString("temp"));
                    dayTwo.add(array.getJSONObject(i).getString("dt_txt"));
                    dayTwo.add(array.getJSONObject(i).getJSONObject("main").getString("temp_max") + "/" + array.getJSONObject(i).getJSONObject("main").getString("temp_min"));
                }

                //content  temp, feelsLike, condiction, wind, humidity
                dayTwo.add(array.getJSONObject(i).getJSONObject("main").getString("temp"));
                dayTwo.add(array.getJSONObject(i).getJSONObject("main").getString("feels_like"));
                dayTwo.add(weatherArray.getJSONObject(i).getString("main"));
                dayTwo.add(windsArray.getString("speed") + "mph");
                dayOne.add(array.getJSONObject(i).getJSONObject("main").getString("humidity"));

            }

            /*** DAY THREE **/
            for (int i = 48; i <= 71; i += 2) { //format   current temp, date, highlow, temp, feelsLike, condition, wind, humidity  only first pos had HEADER!
                JSONArray weatherArray = array.getJSONObject(i).getJSONArray("weather");
                JSONObject windsArray = response.getJSONArray("list").getJSONObject(i).getJSONObject("wind");

                if (i == 48) {//do headers
                    //            getting all the headers stuff current temp, date, highlow
                    dayThree.add(array.getJSONObject(i).getJSONObject("main").getString("temp"));
                    dayThree.add(array.getJSONObject(i).getString("dt_txt"));
                    dayThree.add(array.getJSONObject(i).getJSONObject("main").getString("temp_max") + "/" + array.getJSONObject(i).getJSONObject("main").getString("temp_min"));
                }

                //content  temp, feelsLike, condiction, wind, humidity
                dayThree.add(array.getJSONObject(i).getJSONObject("main").getString("temp"));
                dayThree.add(array.getJSONObject(i).getJSONObject("main").getString("feels_like"));
                dayThree.add(weatherArray.getJSONObject(i).getString("main"));
                dayThree.add(windsArray.getString("speed") + "mph");
                dayThree.add(array.getJSONObject(i).getJSONObject("main").getString("humidity"));

            }

            /*** DAY FOUR **/
            for (int i = 72; i <= 95; i += 2) { //format   current temp, date, highlow, temp, feelsLike, condition, wind, humidity  only first pos had HEADER!
                JSONArray weatherArray = array.getJSONObject(i).getJSONArray("weather");
                JSONObject windsArray = response.getJSONArray("list").getJSONObject(i).getJSONObject("wind");

                if (i == 72) {//do headers
                    //            getting all the headers stuff current temp, date, highlow
                    dayFour.add(array.getJSONObject(i).getJSONObject("main").getString("temp"));
                    dayFour.add(array.getJSONObject(i).getString("dt_txt"));
                    dayFour.add(array.getJSONObject(i).getJSONObject("main").getString("temp_max") + "/" + array.getJSONObject(i).getJSONObject("main").getString("temp_min"));
                }

                //content  temp, feelsLike, condiction, wind, humidity
                dayFour.add(array.getJSONObject(i).getJSONObject("main").getString("temp"));
                dayFour.add(array.getJSONObject(i).getJSONObject("main").getString("feels_like"));
                dayFour.add(weatherArray.getJSONObject(i).getString("main"));
                dayFour.add(windsArray.getString("speed") + "mph");
                dayFour.add(array.getJSONObject(i).getJSONObject("main").getString("humidity"));

            }
        }
    }

    private void doSomeMagic() {
        _myArrayForecast = new ArrayList<>();
        //grab each "day"
        ForecastViewHelper dayOne = new ForecastViewHelper();
        ForecastViewHelper dayTwo = new ForecastViewHelper();
        ForecastViewHelper dayThree = new ForecastViewHelper();
        ForecastViewHelper dayFour = new ForecastViewHelper();

        //add them to the array
        _myArrayForecast.add(dayOne);
        _myArrayForecast.add(dayTwo);
        _myArrayForecast.add(dayThree);
        _myArrayForecast.add(dayFour);

        /*** DAY 1 ***/

        ForecastViewHelper.Headers x = dayOne.get_myHeaders();//headers
        x.setCurrentTemp("56");
        x.setDate("Monday, April 24 2021");
        x.setHighLow("65/45");


        ForecastViewHelper.FirstRow one = dayOne.get_myFirstRow();
        one.setTemp("45");
        one.setFeelsLike("55");
        one.setCondition("Rainy");
        one.setWind("10 mph");
        one.setHumidity("55%");

        ForecastViewHelper.SecondRow two = dayOne.get_mySecondRow();
        two.setTemp("45");
        two.setFeelsLike("55");
        two.setCondition("Rainy");
        two.setWind("10 mph");
        two.setHumidity("55%");

        ForecastViewHelper.ThirdRow three = dayOne.get_myThirdRow();
        three.setTemp("45");
        three.setFeelsLike("55");
        three.setCondition("Rainy");
        three.setWind("10 mph");
        three.setHumidity("55%");

        ForecastViewHelper.FourthRow four = dayOne.get_myFourthRow();
        four.setTemp("45");
        four.setFeelsLike("55");
        four.setCondition("Rainy");
        four.setWind("10 mph");
        four.setHumidity("55%");

        ForecastViewHelper.FifthRow five = dayOne.get_myFifthRow();
        five.setTemp("45");
        five.setFeelsLike("55");
        five.setCondition("Rainy");
        five.setWind("10 mph");
        five.setHumidity("55%");

        ForecastViewHelper.SixthRow six = dayOne.get_mySixthRow();
        six.setTemp("45");
        six.setFeelsLike("55");
        six.setCondition("Rainy");
        six.setWind("10 mph");
        six.setHumidity("55%");

        ForecastViewHelper.SeventhRow seven = dayOne.get_mySeventhRow();
        seven.setTemp("45");
        seven.setFeelsLike("55");
        seven.setCondition("Rainy");
        seven.setWind("10 mph");
        seven.setHumidity("55%");

        ForecastViewHelper.EighthRow eight = dayOne.get_myEightRow();
        eight.setTemp("45");
        eight.setFeelsLike("55");
        eight.setCondition("Rainy");
        eight.setWind("10 mph");
        eight.setHumidity("55%");

        ForecastViewHelper.NinthRow nine = dayOne.get_myNinthRow();
        nine.setTemp("45");
        nine.setFeelsLike("55");
        nine.setCondition("Rainy");
        nine.setWind("10 mph");
        nine.setHumidity("55%");


        ForecastViewHelper.TenthRow ten = dayOne.get_myTenthRow();
        ten.setTemp("45");
        ten.setFeelsLike("55");
        ten.setCondition("Rainy");
        ten.setWind("10 mph");
        ten.setHumidity("55%");


        ForecastViewHelper.EleventhRow eleven = dayOne.get_myEleventhRow();
        eleven.setTemp("45");
        eleven.setFeelsLike("55");
        eleven.setCondition("Rainy");
        eleven.setWind("10 mph");
        eleven.setHumidity("55%");


        ForecastViewHelper.TwelfthRow twelve = dayOne.get_myTwelfthRow();
        twelve.setTemp("45");
        twelve.setFeelsLike("55");
        twelve.setCondition("Rainy");
        twelve.setWind("10 mph");
        twelve.setHumidity("55%");

        /*** DAY 2 ***/

        /*** DAY 3 ***/


        /*** DAY 4 ***/


    }




    /******************* THE CODE BELOW HANDLES LOCATION PERMISSIONS AND FINDING THE USER'S LAT AND LON ***********************/
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
                    .putFloat(LAT_KEY, (float) location.getLatitude())
                    .putFloat(LOG_KEY, (float) location.getLongitude())
                    .apply();
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