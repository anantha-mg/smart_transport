package smart_transport

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    //Gson gson = new Gson();
    Gson gson = new GsonBuilder().setExclusionStrategies(new GrailsDomainExclusionStrategy()).create()
    def setup() {
    }

    def cleanup() {
    }

    void "test User to JSON Conversion"() {
        when:
        User user = new User()
        user.setMobile("9988776655")
        Location sourceLocation = new Location(latitude: 1, longitude: 2, address: "source address")
        Location destinationLocation = new Location(latitude: 3, longitude: 4, address: "destination address")
        user.setSourceLocation(sourceLocation)
        user.setDestinationLocation(destinationLocation)
        user.setTransportType(TransportType.COMMUTE)
        Time sourceTime = new Time(hour: 9, minute: 0)
        Time destinationTime = new Time(hour: 18, minute: 0)
        user.setSourceTime(sourceTime)
        user.setDestinationTime(destinationTime)


        then:
        System.out.println(gson.toJson(user))

    }

    void "test JSON to User Conversion"() {
        when:
        String userJson = """{"mobile":"9988776655","sourceLocation":{"latitude":1,"longitude":2}}""";

        then:
        User user = gson.fromJson(userJson, User.class)
        System.out.println(user)

    }
}
