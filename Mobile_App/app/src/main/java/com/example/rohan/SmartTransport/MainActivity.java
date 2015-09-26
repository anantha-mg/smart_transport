package com.example.rohan.SmartTransport;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = "Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private TextView fullname;
    private TextView mob_no;
    private Button subscribe;
    private TimePicker start_time;
    private TimePicker end_time;
    private static final String API_KEY = "AIzaSyCrhtfK-7AtsHzHUpm2njNke8TD4M3SpmM";
    private final String USER_AGENT = "Mozilla/5.0";
    private String[] loc_coords;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final AutoCompleteTextView autoCompView_start = (AutoCompleteTextView) findViewById(R.id.autocomplete_start);
        final AutoCompleteTextView autoCompView_end = (AutoCompleteTextView) findViewById(R.id.autocomplete_end);

        autoCompView_start.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView_start.setOnItemClickListener(this);

        autoCompView_end.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView_end.setOnItemClickListener(this);
        fullname = (TextView)findViewById(R.id.input_name);
        mob_no = (TextView)findViewById(R.id.input_phno);
        subscribe = (Button)findViewById(R.id.btn_subscribe);
        start_time = (TimePicker)findViewById(R.id.start_timePicker);
        end_time = (TimePicker)findViewById(R.id.endtimePicker);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String full_name = fullname.getText().toString();
                String phno = mob_no.getText().toString();
                int hour_start = start_time.getCurrentHour();

                int minutes_start = start_time.getCurrentMinute();
                int hour_end = end_time.getCurrentHour();
                int minutes_end = end_time.getCurrentMinute();
                String home_location = autoCompView_start.getText().toString();
                String end_location = autoCompView_end.getText().toString();
                String[] home_coords = new String[2] ;
                String[] end_coords = new String[2];
                try
                {
                    JSONParser1 jsonParser1 = new JSONParser1(home_location);
                    loc_coords = jsonParser1.process();
                    home_coords[0]= loc_coords[0];
                    home_coords[1] = loc_coords[1];

                    jsonParser1 = new JSONParser1(end_location);
                    loc_coords = jsonParser1.process();
                    end_coords[0]= loc_coords[0];
                    end_coords[1] = loc_coords[1];
//                    for(String i: home_coords)
//                    {
//                        System.out.println(i);
//                    }
//                    for(String i: end_coords)
//                    {
//                        System.out.println(i);
//                    }
                }
                catch (Exception e)
                {
                    System.out.println("ERROR");
                }
                String office_location = autoCompView_end.getText().toString();
                String send_data = "{\"mobile\":\""+phno+"\",\"sourceLocation\":{\"latitude\":"+home_coords[0]+",\"longitude\":"+home_coords[1]+",\"address\":\""+home_location+"\"},\"destinationLocation\":{\"latitude\":"+end_coords[0]+",\"longitude\":"+end_coords[1]+",\"address\":\""+office_location+"\"},\"transportType\":\"COMMUTE\",\"sourceTime\":{\"hour\":"+hour_start+",\"minute\":"+minutes_start+"},\"destinationTime\":{\"hour\":"+hour_end+",\"minute\":"+minutes_end+"}}";
//              String send_data = "{\"mobile\":\"9988776654\",\"sourceLocation\":{\"latitude\":1,\"longitude\":2,\"address\":\"source address\"},\"destinationLocation\":{\"latitude\":3,\"longitude\":4,\"address\":\"destination address\"},\"transportType\":\"COMMUTE\",\"sourceTime\":{\"hour\":9,\"minute\":0},\"destinationTime\":

                try
                {
                    System.out.println(hour_end);
                    new makeSubscriptionRequest().execute(send_data);
                }
                catch(Exception e)
                {
                    System.out.println(e.fillInStackTrace());
                }

            }
        });
    }

    class makeSubscriptionRequest extends AsyncTask<String,Void,String>
    {
        protected String doInBackground(String... urls) {
            try {
                String send_data = urls[0];
                String url = "http://10.20.240.106:8080/smart_transport/user/subscribe?requestJson="+URLEncoder.encode(send_data,"UTF-8");
                Log.e("URL",url);

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

                //print result
                System.out.println(response.toString());
                return null;

            } catch (Exception e) {
                return null;
            }
        }


    }



//    public void makeGETRequest(String name, String phno, int hour, int minutes, String home_location, String end_location) throws Exception
//    {
//        String url = "http://10.20.240.106:8080/smart_transport/user/subscribe";
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        con.setRequestMethod("GET");
//        con.setRequestProperty("User-Agent", USER_AGENT);
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//
//        String send_data = "{\"mobile\":\"9988776655\",\"sourceLocation\":{\"latitude\":1,\"longitude\":2,\"address\":\"source address\"},\"destinationLocation\":{\"latitude\":3,\"longitude\":4,\"address\":\"destination address\"},\"transportType\":\"COMMUTE\",\"sourceTime\":{\"hour\":9,\"minute\":0},\"destinationTime\":{\"hour\":18,\"minute\":0}}";
//        DataOutputStream dataOutputStream = new DataOutputStream(con.getOutputStream());
//        dataOutputStream.writeBytes(send_data);
//        dataOutputStream.flush();
//        dataOutputStream.close();
//
//        int responseCode = con.getResponseCode();
//        if(responseCode == 200)
//            Log.e("GET REQUEST","Successful");
//        System.out.println(responseCode);
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //print result
//        System.out.println(response.toString());
//
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index).toString();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
}
