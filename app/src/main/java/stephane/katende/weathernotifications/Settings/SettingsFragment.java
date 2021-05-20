package stephane.katende.weathernotifications.Settings;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import stephane.katende.weathernotifications.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    BottomNavigationView _myBottomNav;
    NavController navController;
    FloatingActionButton _myFAB;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        _myBottomNav = getActivity().findViewById(R.id._bottomNav);
        _myFAB = getActivity().findViewById(R.id._btnFloating);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().findViewById(R.id._tvBackground).setBackground(getResources().getDrawable(R.color.white));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().findViewById(R.id._tvBackground).setBackground(getResources().getDrawable(R.color.ic_launcher_background));
    }



    @Override
    public void onResume() {
        super.onResume();
        //bottom nav
        BottomNavigationView.OnNavigationItemSelectedListener bottomNavLister = item -> {
            switch (item.getItemId()) {
                case R.id.iconForcast:
                    navController.navigate(R.id.from_Settings_to_Forcast);
                    break;
                case R.id.iconAlerts:
                    navController.navigate(R.id.from_Settings_to_Alerts);

                    break;
            }
            return true;
        };
        _myBottomNav.setOnNavigationItemSelectedListener(bottomNavLister);

        //fab listener
        _myFAB.setOnClickListener(v -> {
            navController.navigate(R.id.from_Settings_to_SetAlerts);
        });

    }
}