package stephane.katende.weathernotifications.Forcast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import stephane.katende.weathernotifications.R;


public class ForcastFragment extends Fragment {
    androidx.appcompat.widget.Toolbar myToolBar;
    private static final String userLocatingTAG = "userLocation", ARG_PARAM2 = "param2";
    private String _userLocation, mParam2;
    BottomNavigationView _myBottomNav;
    FloatingActionButton _myFAB;
    NavController navController;
    RecyclerView _myRecyclerView;
    ForecastViewHelper forecastViewHelper;
    ArrayList<ForecastViewHelper> _myArrayForecast;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myToolBar = view.findViewById(R.id._forcastToolbar);
        navController = Navigation.findNavController(view);
        _myFAB = getActivity().findViewById(R.id._btnFloating);
        _myBottomNav = getActivity().findViewById(R.id._bottomNav);
        _myRecyclerView = view.findViewById(R.id._myRecyclerView);

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
                    //tap to bring to front of view
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
                    //make
                    getActivity().findViewById(R.id.coordinatorLayout).setVisibility(View.GONE);
                    navController.navigate(R.id.from_Forcast_to_Game);
                    return true;
                case R.id.iconSettings:
                    navController.navigate(R.id.from_Forcast_to_Settings);
                    return true;
                default:
                    return false;
            }
        });


        /** RECYCLER VIEW  BINDING **/
        //data setup
        setUpDays();
      //make your adapter and give it the arraylist
        Adapter adapter = new Adapter(getActivity(), _myArrayForecast);


        //set the adapter
       _myRecyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        _myRecyclerView.setLayoutManager(linearLayoutManager);
    }

     void setUpDays() {
        //get the whole thing
        _myArrayForecast = new ArrayList<>();
        //grab each "day"
        ForecastViewHelper dayOne = new ForecastViewHelper();
        ForecastViewHelper dayTwo = new ForecastViewHelper();
        ForecastViewHelper dayThree = new ForecastViewHelper();
        ForecastViewHelper dayFour = new ForecastViewHelper();
        ForecastViewHelper dayFive = new ForecastViewHelper();
        ForecastViewHelper daySix = new ForecastViewHelper();
        ForecastViewHelper daySeven = new ForecastViewHelper();

        //add them to the array
        _myArrayForecast.add(dayOne);
        _myArrayForecast.add(dayTwo);
        _myArrayForecast.add(dayThree);
        _myArrayForecast.add(dayFour);
        _myArrayForecast.add(dayFive);
        _myArrayForecast.add(daySix);
        _myArrayForecast.add(daySeven);

        //set up each day

        //day one
        ForecastViewHelper.Headers x = dayOne.get_myHeaders();//headers
        x.setCurrentTemp("56");
        x.setHighLow("65/45");
        x.setLastUpdate("updated : 2 hours ago");
        x.setDate("Monday, April 24 2021");
        x.setContent("This is an example of content");

        ForecastViewHelper.FirstRow one = dayOne.get_myFirstRow();
        one.setTemp("45");
        one.setFeelsLike("55");
        one.setCondition("Rainy");
        one.setWind("10 mph");
        one.setHumidity("55%");
        one.setUv("2/10");

        ForecastViewHelper.SecondRow two = dayOne.get_mySecondRow();
        two.setTemp("45");
        two.setFeelsLike("55");
        two.setCondition("Rainy");
        two.setWind("10 mph");
        two.setHumidity("55%");
        two.setUv("2/10");

        ForecastViewHelper.ThirdRow three = dayOne.get_myThirdRow();
        three.setTemp("45");
        three.setFeelsLike("55");
        three.setCondition("Rainy");
        three.setWind("10 mph");
        three.setHumidity("55%");
        three.setUv("2/10");

        ForecastViewHelper.FourthRow four = dayOne.get_myFourthRow();
        four.setTemp("45");
        four.setFeelsLike("55");
        four.setCondition("Rainy");
        four.setWind("10 mph");
        four.setHumidity("55%");
        four.setUv("2/10");

        ForecastViewHelper.FifthRow five = dayOne.get_myFifthRow();
        five.setTemp("45");
        five.setFeelsLike("55");
        five.setCondition("Rainy");
        five.setWind("10 mph");
        five.setHumidity("55%");
        five.setUv("2/10");

        ForecastViewHelper.SixthRow six = dayOne.get_mySixthRow();
        six.setTemp("45");
        six.setFeelsLike("55");
        six.setCondition("Rainy");
        six.setWind("10 mph");
        six.setHumidity("55%");
        six.setUv("2/10");

        ForecastViewHelper.SeventhRow seven = dayOne.get_mySeventhRow();
        seven.setTemp("45");
        seven.setFeelsLike("55");
        seven.setCondition("Rainy");
        seven.setWind("10 mph");
        seven.setHumidity("55%");
        seven.setUv("2/10");

        ForecastViewHelper.EighthRow eight = dayOne.get_myEightRow();
        eight.setTemp("45");
        eight.setFeelsLike("55");
        eight.setCondition("Rainy");
        eight.setWind("10 mph");
        eight.setHumidity("55%");
        eight.setUv("2/10");

        ForecastViewHelper.NinthRow nine = dayOne.get_myNinthRow();
        nine.setTemp("45");
        nine.setFeelsLike("55");
        nine.setCondition("Rainy");
        nine.setWind("10 mph");
        nine.setHumidity("55%");
        nine.setUv("2/10");

        ForecastViewHelper.TenthRow ten = dayOne.get_myTenthRow();
        ten.setTemp("45");
        ten.setFeelsLike("55");
        ten.setCondition("Rainy");
        ten.setWind("10 mph");
        ten.setHumidity("55%");
        ten.setUv("2/10");

        ForecastViewHelper.EleventhRow eleven = dayOne.get_myEleventhRow();
        eleven.setTemp("45");
        eleven.setFeelsLike("55");
        eleven.setCondition("Rainy");
        eleven.setWind("10 mph");
        eleven.setHumidity("55%");
        eleven.setUv("2/10");


        ForecastViewHelper.TwelfthRow twelve = dayOne.get_myTwelfthRow();
        twelve.setTemp("45");
        twelve.setFeelsLike("55");
        twelve.setCondition("Rainy");
        twelve.setWind("10 mph");
        twelve.setHumidity("55%");
        twelve.setUv("2/10");


        //random data
        dayTwo.fillRandomData();
        dayThree.fillRandomData();
        dayFour.fillRandomData();
        dayFive.fillRandomData();
        daySix.fillRandomData();
        daySeven.fillRandomData();


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

    public ForecastViewHelper forecastViewHelper() {
        ForecastViewHelper x = new ForecastViewHelper();


        return x;
    }


}