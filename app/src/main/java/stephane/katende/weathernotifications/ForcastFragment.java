package stephane.katende.weathernotifications;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ForcastFragment extends Fragment {
    androidx.appcompat.widget.Toolbar myToolBar;
    private static final String userLocatingTAG = "userLocation", ARG_PARAM2 = "param2";
    private String _userLocation, mParam2;
    BottomNavigationView _myBottomNav;
    FloatingActionButton _myFAB;
    NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myToolBar = view.findViewById(R.id._forcastToolbar);
        navController = Navigation.findNavController(view);
        _myFAB = getActivity().findViewById(R.id._btnFloating);
        _myBottomNav = getActivity().findViewById(R.id._bottomNav);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _userLocation = getArguments().getString(userLocatingTAG);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle("VT, Colchester");
        //bottom nav
        BottomNavigationView.OnNavigationItemSelectedListener bottomNavLister = item -> {
            switch (item.getItemId()) {
                case R.id.iconForcast:

                    break;
                case R.id.iconAlerts:
                    navController.navigate(R.id.from_Forcast_to_Alerts);

                    break;
            }
            return true;
        };
        _myBottomNav.setOnNavigationItemSelectedListener(bottomNavLister);

        //fab listener
        _myFAB.setOnClickListener(v -> {
            navController.navigate(R.id.from_Forcast_to_SetAlerts);
        });

        myToolBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.iconGame:
                    startActivity(new Intent(getContext(), GameActivity.class));
                    return true;
                case R.id.iconSettings:
                    navController.navigate(R.id.from_Forcast_to_Settings);
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    /**
     * Set a tool bar title, set a tool bar sub title as @userLocation
     *
     * @param userLocation the location of the user, must be <= 34 chars
     */
    private void setToolbarTitle(String userLocation) {
        myToolBar.setTitle("Forecasts");
        if (userLocation.length() <= 34)
            myToolBar.setSubtitle(userLocation);
    }

    public static ForcastFragment newInstance(String param1, String param2) {
        ForcastFragment fragment = new ForcastFragment();
        Bundle args = new Bundle();
        args.putString(userLocatingTAG, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forcast, container, false);
        return view;
    }

    public ForcastFragment() {
        // Required empty public constructor
    }


}