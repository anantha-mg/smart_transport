package smart_transport


import grails.test.mixin.*
import spock.lang.*

@TestFor(RideController)
@Mock(Ride)
class RideControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.rideInstanceList
        model.rideInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.rideInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def ride = new Ride()
        ride.validate()
        controller.save(ride)

        then: "The create view is rendered again with the correct model"
        model.rideInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        ride = new Ride(params)

        controller.save(ride)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/ride/show/1'
        controller.flash.message != null
        Ride.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def ride = new Ride(params)
        controller.show(ride)

        then: "A model is populated containing the domain instance"
        model.rideInstance == ride
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def ride = new Ride(params)
        controller.edit(ride)

        then: "A model is populated containing the domain instance"
        model.rideInstance == ride
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/ride/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def ride = new Ride()
        ride.validate()
        controller.update(ride)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.rideInstance == ride

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        ride = new Ride(params).save(flush: true)
        controller.update(ride)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/ride/show/$ride.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/ride/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def ride = new Ride(params).save(flush: true)

        then: "It exists"
        Ride.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(ride)

        then: "The instance is deleted"
        Ride.count() == 0
        response.redirectedUrl == '/ride/index'
        flash.message != null
    }
}
