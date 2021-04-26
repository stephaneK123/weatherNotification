package stephane.katende.weathernotifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {
    FragmentManager _myFragmentManager;
    FragmentContainerView _myFragmentView;
    private final String NOTIFICATION_CHANNELID = "stephane.katende.weathernotifications";
    private final int notificationId = 1;
    NotificationCompat.Builder _myNotificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WeatherNotifications);
        setContentView(R.layout.activity_main);
        _myNotificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNELID);


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