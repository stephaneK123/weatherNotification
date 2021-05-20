package stephane.katende.weathernotifications.Settings;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import stephane.katende.weathernotifications.MainActivity;
import stephane.katende.weathernotifications.R;


public class PermissionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2";;
    private String mParam1,  mParam2;
    androidx.appcompat.widget.Toolbar myToolBar;
    ConstraintLayout constraintLayout;
    private static final String SHOW_PERMS_KEY = "setlocationkey"; //is the location fragment already showed?
    SharedPreferences sharedPreferences;

    @Override
    public void onResume() {
        super.onResume();
        //are permissions enabled?
        constraintLayout.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//permissions not granted
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }

            //is location enabled?
            boolean gpsOn = false, networkOn = false;
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            try {
                gpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                networkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {

            }

            if (!gpsOn && !networkOn) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Please enable GPS")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }).setNegativeButton("Nah", null )
                        .show();
            }



        });


    }

    @Override
    public void onDetach() {
        sharedPreferences.edit().putBoolean(SHOW_PERMS_KEY, true).apply();
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myToolBar = view.findViewById(R.id._myToolbar);
        constraintLayout = view.findViewById(R.id.myLayout);

    }

    public static PermissionFragment newInstance(String param1, String param2) {
        PermissionFragment fragment = new PermissionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());



        return view;
    }

    public PermissionFragment() {
        // Required empty public constructor
    }

    /**
     * Set a tool bar title, set a tool bar sub title as @userLocation
     *
     * @param userLocation the location of the user, must be <= 34 chars
     */
    private void setToolbarTitle(String userLocation) {
        myToolBar.setTitle("Enable Permissions");
        if (userLocation.length() <= 34)
            myToolBar.setSubtitle(userLocation);
    }
}