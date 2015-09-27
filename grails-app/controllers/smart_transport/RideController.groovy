package smart_transport

import com.google.gson.Gson


class RideController {

    Gson gson = new Gson();

    def bookRide() {

        String mobile = params.mobile
        Ride ride = new Ride()

        User user = User.findByMobile(mobile)
        ride.setTransportType(user.transportType)
        ride.setRideStatus(RideStatus.WAITING_FOR_SHARE)
        List<Integer> subscribers = new ArrayList<Integer>()
        subscribers.add(user.getId())
        ride.setSubscribers(subscribers)

        ride.save(flush:true, failOnError: true)

        RideUtil.matchingRideMap.put(user.mobile, new RideIdAndRideType(rideId: ride.id, rideType: RideType.ONWARD))

        render "Your ride request has been accepted"

    }

    def joinRide() {

        String mobile = params.mobile

        RideIdAndRideType rideIdAndRideType = RideUtil.matchingRideMap.get(mobile)

        RideUtil.joinRide(mobile, rideIdAndRideType.rideId, rideIdAndRideType.rideType)
        render "Your ride request has been accepted"

    }

    def status() {

        String mobile = params.mobile

        User user = User.findByMobile(mobile)
        if(user == null)
            render "Subscribe"

        RideIdAndRideType rideIdAndRideType = RideUtil.matchingRideMap.get(mobile)
        if(rideIdAndRideType == null) {
            render "Book Cab"
        }

        long rideId = rideIdAndRideType.rideId

        Ride ride = Ride.findById(rideId)

        if(!ride.subscribers.contains(user.id))
            render "Join Cab"

        if(ride.rideStatus == RideStatus.WAITING_FOR_SHARE)
            render "Waiting for more pooling"
        else if(ride.rideStatus == RideStatus.PENDING_BOOKING)
            render "Your request has got fully pooled. We are processing your request and booking Ola"
        else if(ride.rideStatus == RideStatus.BOOKED){
            Booking booking = gson.fromJson(ride.bookingDetails, Booking.class);
            render "Ola booked!!! Booking id : " + booking.crn + " Driver name : " + booking.driver_name + " Driver number : " + booking.driver_number
        }

    }

}
