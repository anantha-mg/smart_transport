package smart_transport

class Ride {

    static constraints = {

        bookingDetails(blank:true, nullable:true)

    }

    static mapping = {
        subscribers lazy : false
        routeOfLocations lazy:false
    }

    static hasMany = [subscribers : Long, routeOfLocations: Long ]

    // ex: we can stop booking for a ride for 3 users in a mini and can take upto 10 bookings for transporting lunches
    // subscribers will contain user ids of subscribers who booked/shared the ride
    List<Long> subscribers;

    TransportType transportType; // PEOPLE OR GOODS/PARCEL/LUNCH
    RideStatus rideStatus;


    String bookingDetails

    /**
     *
     * Lets say we are pairing up 2 users User1 and User2. User1 route is location_id A->B and User2 path is location_id C->D

     SmartTransport will choose the best of below routes.

     A----(C----B)----D

     A----(C----D)----B

     C----(A----B)----D

     C----(A---D)----B

     If we have to store ACDB, it will be stored as List<A,C,D,B>

     */

    List<Long> routeOfLocations;

}
