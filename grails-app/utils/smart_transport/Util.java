package smart_transport;

import java.util.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by anantha on 27/9/15.
 */
public class Util {

    public static boolean isRideSharable(Location existingRiderStart, Location existingRiderEnd, Location sharedRiderStart, Location sharedRiderEnd){

        //TODO plugin the algo to determine if the 2 rides/routes are compatible [nearby without much deviation + maximum overlap of kms]
        return true;
    }

    // HTTP request
    public static String sendRequest(String url, String paramString) throws Exception {

        String urlParameters = paramString;

        //String finalURL = url + "?" + URLEncoder.encode(paramString, "UTF-8");
        String finalURL = url + "?" + paramString;

        URL obj = new URL(finalURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("GET");
        //con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("X-APP-TOKEN", "74f5767f8e624b6389c38a683c11319e");
        con.setRequestProperty("Authorization", "Bearer c386ee17c4fa4c69a913daf690e192b1");



        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + finalURL);
        System.out.println("Response Code : " + responseCode);

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

        return response.toString();

    }


}
