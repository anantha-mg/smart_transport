package smart_transport;

import com.google.gson.Gson;

/**
 * Created by anantha on 27/9/15.
 */
public class TestUtil {

    public static String bookingEndPoint = "http://sandbox-t.olacabs.com/v1/bookings/create";

    public static String productsEndPoint = "http://sandbox-t.olacabs.com/v1/products";



    public static void main(String[] args) {

        Gson gson = new Gson();
        String pickUpLat = "12.950090";
        String pickUpLng = "77.642600";

        String urlParams = "pickup_lat=" + pickUpLat + "&pickup_lng=" + pickUpLng + "&pickup_mode=NOW&category=sedan";
        //urlParams = "pickup_lat=12.950072&pickup_lng=77.642684&category=sedan";

        try {
            String bookingJson = Util.sendRequest(bookingEndPoint, urlParams);
            Booking booking = gson.fromJson(bookingJson, Booking.class);
            System.out.println(booking.crn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
