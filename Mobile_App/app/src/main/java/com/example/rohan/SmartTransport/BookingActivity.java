package com.example.rohan.SmartTransport;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by rohan on 27/9/15.
 */
public class BookingActivity extends Activity {

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        imageButton = (ImageButton)findViewById(R.id.imagebtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String mobile_number;
                    Bundle extras = getIntent().getExtras();
                    if(extras == null)
                        mobile_number="0000000000";
                    else
                    {
                        mobile_number = extras.getString("Mobile Number");

                    }
                    String url = "http://10.20.242.61:8080/smart_transport/ride/bookRide?mobile="+mobile_number;
                    Log.e("URL", url);
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                    int responseCode = con.getResponseCode();
                    if(responseCode == 200)
                        Log.e("GET REQUEST","Successful");

                }
                catch(Exception e)
                {
                    System.out.println(e.fillInStackTrace());
                }

            }
        });


    }
}
