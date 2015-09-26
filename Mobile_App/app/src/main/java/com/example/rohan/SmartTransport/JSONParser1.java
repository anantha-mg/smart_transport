package com.example.rohan.SmartTransport;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by rohan on 27/9/15.
 */
 class JSONParser1 {
    private static final String API_KEY = "AIzaSyCrhtfK-7AtsHzHUpm2njNke8TD4M3SpmM";
    private final String USER_AGENT = "Mozilla/5.0";

    private String[] loc_coords;

    public JSONParser1(String response)
    {

        loc_coords = new String[2];
    }

    public String[] process()
    {
        try
        {
            new makeGeoCodingRequest().execute("HSR Layout").get();
            return loc_coords;
        }
        catch (Exception e)
        {
            return null;

        }

    }
    class makeGeoCodingRequest extends AsyncTask<String,Void,String>
    {
        protected String doInBackground(String... urls) {
            try {

                Log.e("ASYNC","Executed");
                String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(urls[0], "UTF-8")+"&key="+API_KEY;
                System.out.println(url);
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                int responseCode = con.getResponseCode();
                if(responseCode == 200)
                    Log.e("GET REQUEST","Successful");
                System.out.println(responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());
                String Response = response.toString();
                JSONParser jsonParser = new JSONParser();
                Object main_obj = jsonParser.parse(Response);
                JSONObject obj1 = (JSONObject)main_obj;
                JSONArray results = (JSONArray)obj1.get("results");
                JSONObject results_object = (JSONObject)results.get(0);
                JSONObject geometry = (JSONObject)results_object.get("geometry");
                JSONObject location = (JSONObject)geometry.get("location");
                Double latitude = (Double)location.get("lat");
                Double longitude = (Double)location.get("lng");
                loc_coords = new String[2];
                loc_coords[0] = latitude.toString();
                loc_coords[1] = longitude.toString();
                Log.e("Latitude",latitude.toString());
                return null;
            }
            catch (Exception e) {
                return null;
            }

        }
    }

}
