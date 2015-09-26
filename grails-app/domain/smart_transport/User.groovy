package smart_transport

class User {

    static constraints = {
        mobile(unique: ['mobile', 'transportType'])
        destinationTime(blank:true, nullable:true) // for TransportType=DELIVERY, its a one-way trip ; no need to return back
    }

    static mapping = {
        sourceLocation lazy : false
        destinationLocation lazy:false

        sourceTime lazy : false
        destinationTime lazy:false

    }

    String mobile
    Location sourceLocation // daily starting point - like home/PG/Hostel etc
    Location destinationLocation // daily destination point - like office/college/place of work etc


    TransportType transportType // commute to office or delivery of food (lunch) or a parcel

    Time sourceTime; //pickup time from source
    Time destinationTime; //pickup time from destination
}
