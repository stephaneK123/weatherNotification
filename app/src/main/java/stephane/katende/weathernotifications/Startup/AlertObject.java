package stephane.katende.weathernotifications.Startup;

/**
 * AlertObject - an object to be used when describing a possible alert.
 * alert: n. a user-defined reason someone would get a notification about the weather
 *      things such as "the temperature is above 50F" or "the humidity is less than 30%"
 */
public class AlertObject {

    private String _weatherCond; //can only be "temp", "feelsLike", "humidity", or "windSpeed". Case-Sensitive. Never empty or null
    private String _operand; //can only be "gt", "lt, or "eq". Case Sensitive. Never empty or null
    private int _testValue; //can be whatever, including 0 and negatives


    /**
     * creates a new alertObject
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
     * @param testValue The value you'd like to test against.
     */
    public AlertObject (String weatherCond, String operand, int testValue){
        _weatherCond = weatherCond;
        _operand = operand;
        _testValue = testValue;

        repOK();
    }

    private void repOK(){
        assert (_weatherCond.equals("temp") || _weatherCond.equals("feelsLike") || _weatherCond.equals("humidity") || _weatherCond.equals("windSpeed"));
        assert (_operand.equals("gt") || _operand.equals("lt") || _operand.equals("eq"));
    }

    public int getTestValue() {
        return _testValue;
    }

    public String getOperand() {
        return _operand;
    }

    public String getWeatherCond() {
        return _weatherCond;
    }

    /**
     * checks an input value against the test value, returns a boolean based on the operand and the test value.
     * example:AlertObject.testValue=20, AlertObject.operand="gt" -> AlertObject.checkTestValues(42) == true
     *          reads as "42 > 20"
     * @param v the value you'd like to test your testValue against
     * @return a boolean based on how v tests against testValue using its operand
     */
    public boolean checkTestValues(int v){
        switch (_operand){
            case "gt" :
                return (v > _testValue);
            case "lt" :
                return (v < _testValue);
            case "eq" :
                return (v == _testValue);
            default:
                return (false);
        }
    }

}
