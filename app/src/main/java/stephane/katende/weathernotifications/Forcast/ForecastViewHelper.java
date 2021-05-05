package stephane.katende.weathernotifications.Forcast;


public final class ForecastViewHelper {
    String[] _headers = new String[5], _row = new String[6], _row1 = new String[6],
            _row2 = new String[6], _row3 = new String[6], _row4 = new String[6],
            _row5 = new String[6], _row6 = new String[6], _row7 = new String[6],
            _row8 = new String[6], _row9 = new String[6], _row10 = new String[6],
            _row11 = new String[6], _row12 = new String[6];

    private ForecastViewHelper.Headers _myHeaders = new Headers();
    private ForecastViewHelper.Row _myRow = new Row();
    private ForecastViewHelper.FirstRow _myFirstRow = new FirstRow();
    private ForecastViewHelper.SecondRow _mySecondRow = new SecondRow();
    private ForecastViewHelper.ThirdRow _myThirdRow = new ThirdRow();
    private ForecastViewHelper.FourthRow _myFourthRow = new FourthRow();
    private ForecastViewHelper.FifthRow _myFifthRow = new FifthRow();
    private ForecastViewHelper.SixthRow _mySixthRow = new SixthRow();
    private ForecastViewHelper.SeventhRow _mySeventhRow = new SeventhRow();
    private ForecastViewHelper.EighthRow _myEightRow = new EighthRow();
    private ForecastViewHelper.NinthRow _myNinthRow = new NinthRow();
    private ForecastViewHelper.TenthRow _myTenthRow = new TenthRow();
    private ForecastViewHelper.EleventhRow _myEleventhRow = new EleventhRow();
    private ForecastViewHelper.TwelfthRow _myTwelfthRow = new TwelfthRow();


    public Headers get_myHeaders() {
        return _myHeaders;
    }

    public Row get_Row(){ return _myRow;}

    public FirstRow get_myFirstRow() {
        return _myFirstRow;
    }

    public SecondRow get_mySecondRow() {
        return _mySecondRow;
    }

    public ThirdRow get_myThirdRow() {
        return _myThirdRow;
    }

    public FourthRow get_myFourthRow() {
        return _myFourthRow;
    }

    public FifthRow get_myFifthRow() {
        return _myFifthRow;
    }

    public SixthRow get_mySixthRow() {
        return _mySixthRow;
    }

    public SeventhRow get_mySeventhRow() {
        return _mySeventhRow;
    }

    public EighthRow get_myEightRow() {
        return _myEightRow;
    }

    public NinthRow get_myNinthRow() {
        return _myNinthRow;
    }

    public TenthRow get_myTenthRow() {
        return _myTenthRow;
    }

    public EleventhRow get_myEleventhRow() {
        return _myEleventhRow;
    }

    public TwelfthRow get_myTwelfthRow() {
        return _myTwelfthRow;
    }

    public ForecastViewHelper() {
        //initialize
        //these don't change
        _row[0] = "    Time";
        _row[1] = "Temp";
        _row[2] = "FeelsLike";
        _row[3] = "Condition";
        _row[4] = "Wind";
        _row[5] = "Humidity";

        _row1[0] = "12am";
        _row2[0] = "2am";
        _row3[0] = "4am";
        _row4[0] = "6am";
        _row5[0] = "8am";
        _row6[0] = "10am";
        _row7[0] = "12pm";
        _row8[0] = "2pm";
        _row9[0] = "4pm";
        _row10[0] = "6pm";
        _row11[0] = "8pm";
        _row12[0] = "10pm";
    }


    public void fillRandomData() {
        //headers
        for (int i = 1; i < _headers.length; i++) {
            _headers[i] = "0";
        }
        //rows
        for (int i = 1; i < _row.length; i++) {
            _row1[i] = "test";
            _row2[i] = "test";
            _row3[i] = "test";
            _row4[i] = "test";
            _row5[i] = "test";
            _row6[i] = "test";
            _row7[i] = "test";
            _row8[i] = "test";
            _row9[i] = "test";
            _row10[i] = "test";
            _row11[i] = "test";
            _row12[i] = "test";
        }
    }

    public String[] getHeaders() {
        return  _headers;
    }

    public String[] getRow() {
        return  _row;
    }

    public String[] getRow1() {
        return _row1;
    }

    public String[] getRow2() {
        return _row2;
    }

    public String[] getRow3() {
        return _row3;
    }

    public String[] getRow4() {
        return _row4;
    }

    public String[] getRow5() {
        return _row5;
    }

    public String[] getRow6() {
        return _row6;
    }

    public String[] getRow7() {
        return _row7;
    }

    public String[] getRow8() {
        return _row8;
    }

    public String[] getRow9() {
        return _row9;
    }

    public String[] getRow10() {
        return _row10;
    }

    public String[] getRow11() {
        return _row11;
    }

    public String[] getRow12() {
        return _row12;
    }


    public abstract class Rows {
        public abstract void setTemp(String temp);

        public abstract void setFeelsLike(String feelsLike);

        public abstract void setCondition(String condition);

        public abstract void setWind(String windSpeed);

        public abstract void setHumidity(String humidity);

        public abstract String getTemp();

        public abstract String getFeelsLike();

        public abstract String getCondition();

        public abstract String getWind();

        public abstract String getHumidity();
    }

    public class Headers {
        public Headers() {
            _myHeaders = this;
        }

        public void setCurrentTemp(String currentTemp) {
            _headers[0] = currentTemp;
        }

        public void setDate(String date) {
            _headers[1] = date;
        }

        public void setHighLow(String highLow) {
            _headers[2] = highLow;
        }

        public void setContent(String content) {
            _headers[3] = content;
        }

        public void setLastUpdate(String lastUpdate) {
            _headers[4] = lastUpdate;
        }

        public String getCurrentTemp() {
            return _headers[0];
        }


        public String getDate() {
            return _headers[1];
        }


        public String getHighLow() {
            return _headers[2];
        }


        public String getContent() {
            return _headers[3];
        }

        public String getLastUpdate() {
            return _headers[4];
        }
    }

    public class Row extends Rows {
        public Row() {
            _myRow = this;
        }

        @Override
        public void setTemp(String temp) {
        }

        @Override
        public void setFeelsLike(String feelsLike) {
        }

        @Override
        public void setCondition(String condition) {
        }

        @Override
        public void setWind(String windSpeed) {
        }

        @Override
        public void setHumidity(String humidity) {
        }

        @Override
        public String getTemp() {
            return null;
        }

        @Override
        public String getFeelsLike() {
            return null;
        }

        @Override
        public String getCondition() {
            return null;
        }

        @Override
        public String getWind() {
            return null;
        }

        @Override
        public String getHumidity() {
            return null;
        }
    }

    public class FirstRow extends Rows {
        public FirstRow() {
            _myFirstRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row1[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row1[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row1[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row1[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row1[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row1[1];
        }

        @Override
        public String getFeelsLike() {
            return _row1[2];
        }

        @Override
        public String getCondition() {
            return _row1[3];
        }

        @Override
        public String getWind() {
            return _row1[4];
        }

        @Override
        public String getHumidity() {
            return _row1[5];
        }

    }

    public class SecondRow extends Rows {
        public SecondRow() {
            _mySecondRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row2[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row2[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row2[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row2[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row2[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row2[1];
        }

        @Override
        public String getFeelsLike() {
            return _row2[2];
        }

        @Override
        public String getCondition() {
            return _row2[3];
        }

        @Override
        public String getWind() {
            return _row2[4];
        }

        @Override
        public String getHumidity() {
            return _row2[5];
        }

    }

    public class ThirdRow extends Rows {
        public ThirdRow() {
            _myThirdRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row3[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row3[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row3[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row3[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row3[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row3[1];
        }

        @Override
        public String getFeelsLike() {
            return _row3[2];
        }

        @Override
        public String getCondition() {
            return _row3[3];
        }

        @Override
        public String getWind() {
            return _row3[4];
        }

        @Override
        public String getHumidity() {
            return _row3[5];
        }

    }


    public class FourthRow extends Rows {
        public FourthRow() {
            _myFourthRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row4[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row4[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row4[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row4[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row4[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row4[1];
        }

        @Override
        public String getFeelsLike() {
            return _row4[2];
        }

        @Override
        public String getCondition() {
            return _row4[3];
        }

        @Override
        public String getWind() {
            return _row4[4];
        }

        @Override
        public String getHumidity() {
            return _row4[5];
        }

    }

    public class FifthRow extends Rows {
        public FifthRow() {
            _myFifthRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row5[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row5[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row5[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row5[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row5[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row5[1];
        }

        @Override
        public String getFeelsLike() {
            return _row5[2];
        }

        @Override
        public String getCondition() {
            return _row5[3];
        }

        @Override
        public String getWind() {
            return _row5[4];
        }

        @Override
        public String getHumidity() {
            return _row5[5];
        }

    }

    public class SixthRow extends Rows {
        public SixthRow() {
            _mySixthRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row6[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row6[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row6[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row6[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row6[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row6[1];
        }

        @Override
        public String getFeelsLike() {
            return _row6[2];
        }

        @Override
        public String getCondition() {
            return _row6[3];
        }

        @Override
        public String getWind() {
            return _row6[4];
        }

        @Override
        public String getHumidity() {
            return _row6[5];
        }
    }

    public class SeventhRow extends Rows {
        public SeventhRow() {
            _mySeventhRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row7[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row7[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row7[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row7[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row7[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row7[1];
        }

        @Override
        public String getFeelsLike() {
            return _row7[2];
        }

        @Override
        public String getCondition() {
            return _row7[3];
        }

        @Override
        public String getWind() {
            return _row7[4];
        }

        @Override
        public String getHumidity() {
            return _row7[5];
        }

    }

    public class EighthRow extends Rows {
        public EighthRow() {
            _myEightRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row8[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row8[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row8[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row8[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row8[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row8[1];
        }

        @Override
        public String getFeelsLike() {
            return _row8[2];
        }

        @Override
        public String getCondition() {
            return _row8[3];
        }

        @Override
        public String getWind() {
            return _row8[4];
        }

        @Override
        public String getHumidity() {
            return _row8[5];
        }

    }

    public class NinthRow extends Rows {
        public NinthRow() {
            _myNinthRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row9[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row9[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row9[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row9[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row9[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row9[1];
        }

        @Override
        public String getFeelsLike() {
            return _row9[2];
        }

        @Override
        public String getCondition() {
            return _row9[3];
        }

        @Override
        public String getWind() {
            return _row9[4];
        }

        @Override
        public String getHumidity() {
            return _row9[5];
        }
    }

    public class TenthRow extends Rows {
        public TenthRow() {
            _myTenthRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row10[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row10[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row10[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row10[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row10[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row10[1];
        }

        @Override
        public String getFeelsLike() {
            return _row10[2];
        }

        @Override
        public String getCondition() {
            return _row10[3];
        }

        @Override
        public String getWind() {
            return _row10[4];
        }

        @Override
        public String getHumidity() {
            return _row10[5];
        }
    }

    public class EleventhRow extends Rows {
        public EleventhRow() {
            _myEleventhRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row11[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row11[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row11[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row11[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row11[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row11[1];
        }

        @Override
        public String getFeelsLike() {
            return _row11[2];
        }

        @Override
        public String getCondition() {
            return _row11[3];
        }

        @Override
        public String getWind() {
            return _row11[4];
        }

        @Override
        public String getHumidity() {
            return _row11[5];
        }
    }

    public class TwelfthRow extends Rows {
        public TwelfthRow() {
            _myTwelfthRow = this;
        }

        @Override
        public void setTemp(String temp) {
            _row12[1] = temp;
        }

        @Override
        public void setFeelsLike(String feelsLike) {
            _row12[2] = feelsLike;
        }

        @Override
        public void setCondition(String condition) {
            _row12[3] = condition;
        }

        @Override
        public void setWind(String windSpeed) {
            _row12[4] = windSpeed;
        }

        @Override
        public void setHumidity(String humidity) {
            _row12[5] = humidity;
        }

        @Override
        public String getTemp() {
            return _row12[1];
        }

        @Override
        public String getFeelsLike() {
            return _row12[2];
        }

        @Override
        public String getCondition() {
            return _row12[3];
        }

        @Override
        public String getWind() {
            return _row12[4];
        }

        @Override
        public String getHumidity() {
            return _row12[5];
        }
    }
}
