package smart_transport

/**
 * Created by anantha on 26/9/15.
 */
enum RideStatus {

    INITIATED, // new ride initiated - for commute or transportation of goods
    WAITING_FOR_SHARE, // waiting for others to share ride
    PENDING_BOOKING, // waiting for Booking cab
    BOOKED, // Cab booked

}