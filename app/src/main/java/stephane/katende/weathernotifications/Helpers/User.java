package stephane.katende.weathernotifications.Helpers;
import java.util.HashSet;

public class User {
    private String _name;
    private UserLocation _currentLocation;
    private HashSet<Alerts> _listOfAlerts = new HashSet<>();

    /**
     * Create a user with a UserLocation
     * @param location the location of the user
     */
    public User(UserLocation location){
        _currentLocation = location;
    }

    /**
     * Create a User with a name
     * @param name the name of the user
     */
    public User(String name){
    _name = name;

    }

    /**
     * Create a User with a name, and a user location
     * @param name the name of the user
     * @param location the location of the user
     */
    public User(String name, UserLocation location){
    _name = name;
    _currentLocation = location;
    }

    /**
     * Get the name of the user
     * @return the name of the user, returns "No username" if none exists
     */
    public String getName() {
        if(_name.length() <= 0)
            return "No Username";
        return _name;
    }

    /**
     * Set the name of a user
     * @param _name the name
     */
    public void setName(String _name) {
        this._name = _name;
    }

    /**
     * Get the current location of a user
     * @return the UserLocation of the user
     */
    public UserLocation getCurrentLocation() {
        return _currentLocation;
    }

    /**
     * Set the current location of the user
     * @param _currentLocation the location
     */
    public void setCurrentLocation(UserLocation _currentLocation) {
        this._currentLocation = _currentLocation;
    }

    /**
     * Get a list of the alerts of the user
     * @return A Set of the alerts
     */
    public HashSet<Alerts> getListOfAlerts(){
        return _listOfAlerts;
    }

    /**
     * Add an alert for a user
     * @param alert the alert
     */
    public void addAlert(Alerts alert){
        _listOfAlerts.add(alert);
    }

    /**
     * Remove an alert for a user
     * @param alert the alert
     */
    public void removeAlert(Alerts alert){
        _listOfAlerts.remove(alert);
    }
}
