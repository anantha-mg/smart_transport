package smart_transport

/**
 * Created by anantha on 26/9/15.
 */
class Location {

    static belongsTo = [user:User]

    long latitude;
    long longitude;
    String address; // Human readable address of lat/long
}
