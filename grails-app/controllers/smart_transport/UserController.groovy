package smart_transport

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class UserController {

    Gson gson = new GsonBuilder().setExclusionStrategies(new GrailsDomainExclusionStrategy()).create()

    /**
     * Sample user subscriptions:
     *
     USER1 : {"mobile":"9988776601","sourceLocation":{"latitude":12.950370,"longitude":77.644971,"address":"Embassy Golf Links"},"destinationLocation":{"latitude":12.971891,"longitude":77.641154,"address":"Indiranagar"},"transportType":"DELIVERY","sourceTime":{"hour":9,"minute":35}}
     USER2 : {"mobile":"9988776602","sourceLocation":{"latitude":12.960986,"longitude":77.638732,"address":"Domlur"},"destinationLocation":{"latitude":12.985454,"longitude":77.663925,"address":"C.V.Raman Nagar"},"transportType":"DELIVERY","sourceTime":{"hour":9,"minute":10}}
     * @return
     */

    def subscribe() {

        String requestJson = params.requestJson
        User user = gson.fromJson(requestJson, User.class)

        user.save(flush: true, failOnError: true)

        render "Success"
    }
}
