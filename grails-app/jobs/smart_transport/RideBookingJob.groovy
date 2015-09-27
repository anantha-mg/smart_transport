package smart_transport

import com.google.gson.Gson


class RideBookingJob {
    static triggers = {
      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    Gson gson = new Gson();

    public static String bookingEndPoint = "http://sandbox-t.olacabs.com/v1/bookings/create"

    def execute() {
        List<Ride> rides = Ride.findAllByRideStatus(RideStatus.PENDING_BOOKING)

        rides.each { ride->

            Long rideStartLocationId = ride.getRouteOfLocations().get(0)
            Location rideStartLocation = Location.findById(rideStartLocationId)

            String pickUpLat = rideStartLocation.latitude
            String pickUpLng = rideStartLocation.longitude

            String urlParams = "pickup_lat=" + pickUpLat + "&pickup_lng=" + pickUpLng + "&pickup_mode=NOW&category=sedan"
            String bookingJson = Util.sendRequest(bookingEndPoint, urlParams);

            ride.bookingDetails = bookingJson
            Booking booking = gson.fromJson(ride.bookingDetails, Booking.class);
            if(booking.status != "FAILURE") {
                ride.rideStatus = RideStatus.BOOKED
                //clear from temp map
                ride.subscribers.each { userId->
                    User user = User.findById(userId)
                    RideUtil.matchingRideMap.remove(user.mobile)
                }
            }

            ride.save(flush:true, failOnError: true)
        }
    }
}
