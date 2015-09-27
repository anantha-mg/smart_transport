package smart_transport

/**
 * Created by anantha on 27/9/15.
 */
class RideUtil {

    private static final MAX_COMMUTERS_PER_RIDE = 2
    private static final MAX_DELIVERY_PER_RIDE = 2 // This needs to be more - like 10. But for demo, since we have 3 devices, we are fixing 3 bookings for shared delivery

    public static synchronized HashMap<String,RideIdAndRideType> matchingRideMap = new HashMap<String,RideIdAndRideType>()

    public static String productsEndPoint = "http://sandbox-t.olacabs.com/v1/products";

    public static synchronized joinRide(mobile, rideId, rideType) {

        User user = User.findByMobile(mobile)
        Ride ride = Ride.findById(rideId)
        List<Long> subscribers = ride.getSubscribers()
        subscribers.add(user.getId())
        ride.setSubscribers(subscribers)

        int maxCount = (ride.getTransportType() == TransportType.COMMUTE) ? MAX_COMMUTERS_PER_RIDE : MAX_DELIVERY_PER_RIDE

        if(ride.getSubscribers().size() == maxCount)
            ride.setRideStatus(RideStatus.PENDING_BOOKING) // we can book an Ola now
        else
            ride.setRideStatus(RideStatus.WAITING_FOR_SHARE) // wait for more shared users

        List<Long> locationIds = ride.getRouteOfLocations()
        long startLocationId = (rideType == RideType.ONWARD) ? user.getSourceLocation().getId() : user.getDestinationLocation().getId();
        long endLocationId = (rideType == RideType.ONWARD) ? user.getDestinationLocation().getId() : user.getSourceLocation().getId();
        locationIds.add(startLocationId)
        locationIds.add(endLocationId)
        ride.setRouteOfLocations(locationIds)

        ride.save(flush:true, failOnError: true)

    }

    public static int findSharingBenefits(Location originalStart, Location originalEnd, Location newStart, Location newEnd) {
        // assuming A->C->B->D case

        double dist = findDistance(originalStart.latitude, originalStart.longitude, originalEnd.latitude, originalEnd.longitude)
        double totalDist = findDistance(newStart.latitude, newStart.longitude, newEnd.latitude, newEnd.longitude)

        double originalPrice = dist * 10;
        double totalPrice = totalDist * 10; // assuming flat price of Rs 10 per km instead of slabs for demo

        return (dist*totalPrice)/totalDist - originalPrice;

    }

    private static double findDistance(double pickUpLat, double pickUpLng, double dropLat, double dropLng){

        String urlParams = "pickup_lat=" + pickUpLat  + "&pickup_lng=" + pickUpLng + "&category=sedan&drop_lat= " + dropLat + "&drop_lng=" + dropLng;

        try {
            String estimatesJson = Util.sendRequest(productsEndPoint, urlParams);
            System.out.println(estimatesJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1.0;//fixme

    }

}
