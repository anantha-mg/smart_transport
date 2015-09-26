package smart_transport

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class UserController {

    Gson gson = new GsonBuilder().setExclusionStrategies(new GrailsDomainExclusionStrategy()).create()
    def subscribe() {

        String requestJson = params.requestJson
        User user = gson.fromJson(requestJson, User.class)

        user.save(flush: true, failOnError: true)

        render "Thanks for subscribing to SmartTransport service"
    }
}
