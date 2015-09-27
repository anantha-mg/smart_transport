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


        List<Integer> locationIds = new ArrayList<Integer>()
        // assuming "book ride" is only onwards for now
        locationIds.add(user.getSourceLocation().getId())
        locationIds.add(user.getDestinationLocation().getId())
        ride.setRouteOfLocations(locationIds)

        ride.save(flush:true, failOnError: true)

        RideUtil.matchingRideMap.put(user.mobile, new RideIdAndRideType(rideId: ride.id, rideType: RideType.ONWARD))

        render "Success"

    }

    def joinRide() {

        String mobile = params.mobile

        RideIdAndRideType rideIdAndRideType = RideUtil.matchingRideMap.get(mobile)

        RideUtil.joinRide(mobile, rideIdAndRideType.rideId, rideIdAndRideType.rideType)
        render "Success"

    }

    def status() {

        String mobile = params.mobile

        User user = User.findByMobile(mobile)
        if(user == null){
            render "Subscribe"
            return
        }


        RideIdAndRideType rideIdAndRideType = RideUtil.matchingRideMap.get(mobile)
        if(rideIdAndRideType == null) {
            render "Book Cab"
            return
        }

        long rideId = rideIdAndRideType.rideId

        Ride ride = Ride.findById(rideId)

        if(!ride.subscribers.contains(user.id)){
            render "Join Cab"
            return
        }

        if(ride.rideStatus == RideStatus.WAITING_FOR_SHARE){
            render "Waiting for more pooling"
            return
        } else if(ride.rideStatus == RideStatus.PENDING_BOOKING){
            render "Pending Ola Booking"
            return
        } else if(ride.rideStatus == RideStatus.BOOKED){
            Booking booking = gson.fromJson(ride.bookingDetails, Booking.class);

            def rideInitiater = ride.getSubscribers().get(0)
            boolean isRideInitiater = rideInitiater == user.id

            //calculate savings
            def locationAId = ride.getRouteOfLocations().get(0)
            def locationA = Location.findById(locationAId)
            def locationBId = ride.getRouteOfLocations().get(0)
            def locationB = Location.findById(locationBId)
            def locationCId = ride.getRouteOfLocations().get(1)
            def locationC = Location.findById(locationCId)
            def locationDId = ride.getRouteOfLocations().get(1)
            def locationD = Location.findById(locationDId)

            // assuming the case where A-(C-B)- D is chosen when 2 users are combined (A-B) and (C-D), although
            // ACDB, CABD and CADB are other possible routes
            def startLocation = isRideInitiater ? locationA : locationC
            def endLocation = isRideInitiater ? locationB : locationD
            //int saving = RideUtil.findSharingBenefits(startLocation, endLocation, locationA, locationD)
            int saving = 0

            render "Booked!!! OLA Booking id : " + booking.crn + " Driver name : " + booking.driver_name + " Driver number : " + booking.driver_number + " \n"
                    + (saving > 0 ) ? " You saved Rs. " + saving + " due to sharing " : "";

            return
        }

    }

}
