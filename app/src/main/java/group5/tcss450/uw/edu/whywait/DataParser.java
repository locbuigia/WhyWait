package group5.tcss450.uw.edu.whywait;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Pareses the data of the gas station given from the Google maps api.
 */

public class DataParser implements Serializable {

    /*
     * Gas station name
     */

    private String mName;

    /*
     * The address the station.
     */

    private String mVicinity;

    /*
     * The latitude and longitude of the station.
     */

    private String mLat, mLng;

    /*
     * The rating given to the gas station.
     */

    private double mRating;

    /*
     * The price level of the gas station.
     */

    private String mOpenNow;
    String openNow;

    /*
     * Constructor of the for the data.
     * @param theName The name of the station.
     * @param theVicinity The address from the station.
     * @param theLat The latitude of the station.
     * @param theLng The longitude of the station.
     * @param theRating The rating of the station.
     * @param thePriceLevel The price level of the station.
     */

    public DataParser(String theName, String theVicinity, String theLat, String theLng, double theRating, String theOpenNow) {
        mName = theName;
        mVicinity = theVicinity;
        mLat = theLat;
        mLng = theLng;
        mRating = theRating;
        mOpenNow = theOpenNow;
    }

    /*
     * Empty public constructor that takes no fields.
     */

    public DataParser() {
    }

    /*
     * Pares the json string and returns a list of data fields.
     */

    public List<DataParser> parse(String jsonData, int caseToParse) {
        ArrayList<DataParser> list = new ArrayList<>();
        JSONArray jsonArray;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

            int length = jsonArray.length();
            Log.d("Length", Integer.toString(length));
            if (jsonArray != null) {
                for (int i = 0; i < length; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject geo = object.getJSONObject("geometry");
                    JSONObject location = geo.getJSONObject("location");

                    String name = object.getString("name");
                    String address = "";
                    if (caseToParse == 1) {
                        address = object.getString("vicinity");
                    } else if (caseToParse == 2){
                        address = object.getString("formatted_address");
                    }
                    String lat = location.getString("lat");
                    String lng = location.getString("lng");
                    if (object.has("opening_hours")) {
                        JSONObject openNowObject = object.getJSONObject("opening_hours");
                        openNow = openNowObject.getString("open_now");
                    } else {
                        openNow = "not Available";
                    }
                    double rating;
                    try {
                        rating = object.getDouble("rating");
                    } catch (JSONException e) {
                        rating = 0.0;
                    }


                    DataParser gas = new DataParser(name, address, lat, lng, rating, openNow);

                    list.add(gas);
                }
            }
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return list;
    }

    public String getName() {
        return mName;
    }
    public String getVicinity() {
        return mVicinity;
    }
    public String getLat() {
        return mLat;
    }
    public String getLng() {
        return mLng;
    }
    public double getRating() {
        return mRating;
    }
    public String getOpenNow() {
        return mOpenNow;
    }
}