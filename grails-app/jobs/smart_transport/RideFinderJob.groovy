package smart_transport



class RideFinderJob {
    static triggers = {
      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
        List<Ride> rides = Ride.findAllByRideStatus(RideStatus.WAITING_FOR_SHARE)

        rides.each { ride->
            Long rideInitiatorUserId = ride.getSubscribers().get(0)
            sendNotificaiontoOtherMatchingUsers(ride.id, rideInitiatorUserId)


        }
    }

    def sendNotificaiontoOtherMatchingUsers(rideId, rideInitiatorUserId){
        Date currentDate =  new Date()
        int currentHr = currentDate.getHours()
        int currentMinute = currentDate.getMinutes()

        User rideInitiator = User.findById(rideInitiatorUserId)

        def users = User.findAllByTransportType(rideInitiator.transportType)
        users.each { user->

            if(user.getId() != rideInitiatorUserId){ // we cannot share ride with same user and send notif to the user who started ride

                // first check if time matches as per subscriptions
                // ex: if user has subscribed for 10.35, we can send notif from 10.35 to 11.35 for sharing
                if((currentHr == user.getSourceTime().getHour() && currentMinute >= user.getSourceTime().getMinute())
                    || (currentHr == (( user.getSourceTime().getHour() + 1) % 24) && currentMinute <= user.getSourceTime().getMinute())){

                    boolean isRideSharable = Util.isRideSharable(rideInitiator.getSourceLocation(), rideInitiator.getDestinationLocation(),
                            user.getSourceLocation(), user.getDestinationLocation())

                    if(isRideSharable){
                        //RideUtil.joinRide(user.mobile, rideId, RideType.ONWARD)
                        RideUtil.matchingRideMap.put(user.mobile, new RideIdAndRideType(rideId: rideId, rideType: RideType.ONWARD))
                        //TODO: send PUSH notif here
                    }


                } else {

                    if(user.getTransportType() == TransportType.DELIVERY)
                        return; // 'continue' in grails inside a loop

                    if((currentHr == user.getDestinationTime().getHour() && currentMinute >= user.getDestinationTime().getMinute())
                            || (currentHr == (( user.getDestinationTime().getHour() + 1) % 24) && currentMinute <= user.getDestinationTime().getMinute())){
                        //match on reverse route
                        boolean isRideSharable = Util.isRideSharable(rideInitiator.getSourceLocation(), rideInitiator.getDestinationLocation(),
                                user.getDestinationLocation(), user.getSourceLocation())

                        if(isRideSharable){
                            //RideUtil.joinRide(user.mobile, rideId, RideType.RETURN)
                            RideUtil.matchingRideMap.put(user.mobile, new RideIdAndRideType(rideId: rideId, rideType: RideType.RETURN))
                        }
                    }
                }

            }

        }

    }
}
