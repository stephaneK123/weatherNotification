package stephane.katende.weathernotifications.Startup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import stephane.katende.weathernotifications.Forcast.ForcastFragment;
import stephane.katende.weathernotifications.Helpers.User;
import stephane.katende.weathernotifications.MainActivity;
import stephane.katende.weathernotifications.R;


public class SetLocationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2";
    private String mParam1, mParam2, mCity, mState;
    public static LocationManager locationManager;
    androidx.appcompat.widget.Toolbar myToolBar;
    FloatingActionButton floatingActionButton;
    SharedPreferences sharedPreferences;
    private static final String LAT_KEY = "latkey", LOG_KEY = "logkey", STATE_KEY = "statekey", CITY_KEY = "citykey"; //is the location fragment already showed?







    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle("Unknown location");
        floatingActionButton.setOnClickListener(v -> {
         // getMyLocation();
           startActivity(new Intent(getActivity(), MainActivity.class));

        });


    }




    public static SetLocationFragment newInstance(String param1, String param2) {
        SetLocationFragment fragment = new SetLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SetLocationFragment newInstance() {
        SetLocationFragment fragment = new SetLocationFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myToolBar = view.findViewById(R.id._toolbar);
        floatingActionButton = view.findViewById(R.id._btnUseMyLocation);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());;


        getActivity().findViewById(R.id.coordinatorLayout).setVisibility(View.INVISIBLE);
        TextView x = getActivity().findViewById(R.id._tvBackground);
        getActivity().findViewById(R.id._tvBackground).setBackground(getResources().getDrawable(R.color.white));
        x.setText("We will definitely not stalk you.");
        x.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setlocation, container, false);



        return view;
    }

    public SetLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Set a tool bar title, set a tool bar sub title as @userLocation
     *
     * @param userLocation the location of the user, must be <= 34 chars
     */
    private void setToolbarTitle(String userLocation) {
        myToolBar.setTitle("Set a Location");
        if (userLocation.length() <= 34)
            myToolBar.setSubtitle(userLocation);
    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().findViewById(R.id.coordinatorLayout).setVisibility(View.VISIBLE);
        TextView x = getActivity().findViewById(R.id._tvBackground);
        getActivity().findViewById(R.id._tvBackground).setBackground(getResources().getDrawable(R.color.ic_launcher_background));
        x.setText("");
    }
}