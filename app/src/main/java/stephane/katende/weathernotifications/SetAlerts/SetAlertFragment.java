package stephane.katende.weathernotifications.SetAlerts;

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

import stephane.katende.weathernotifications.R;

public class SetAlertFragment extends Fragment {
    androidx.appcompat.widget.Toolbar myToolBar;
    private static final String userLocationTAG = "userLocation", ARG_PARAM2 = "param2";
    private String _userLocation, mParam2;
    BottomNavigationView _myBottomNav;
    FloatingActionButton _myFAB;
    NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myToolBar = view.findViewById(R.id._setAlertToolbar);
        navController = Navigation.findNavController(view);
        _myFAB = getActivity().findViewById(R.id._btnFloating);
        _myBottomNav = getActivity().findViewById(R.id._bottomNav);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _userLocation = getArguments().getString(userLocationTAG);
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
                    navController.navigate(R.id.from_SetAlert_to_Forcast);
                    break;
                case R.id.iconAlerts:
                    navController.navigate(R.id.from_SetAlert_to_Alerts);

                    break;
            }
            return true;
        };
        _myBottomNav.setOnNavigationItemSelectedListener(bottomNavLister);

        //fab listener
        _myFAB.setOnClickListener(v -> {

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
        myToolBar.setTitle("Set Alerts");
        if (userLocation.length() <= 34)
            myToolBar.setSubtitle(userLocation);
    }

    public static SetAlertFragment newInstance(String userLocation, String param2) {
        SetAlertFragment fragment = new SetAlertFragment();
        Bundle args = new Bundle();
        args.putString(userLocationTAG, userLocation);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_alert, container, false);
        return view;
    }

    public SetAlertFragment() {
        // Required empty public constructor
    }
}