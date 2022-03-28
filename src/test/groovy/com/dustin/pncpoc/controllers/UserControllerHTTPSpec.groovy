package com.dustin.pncpoc.controllers


import com.dustin.pncpoc.models.User
import com.dustin.pncpoc.services.UserServiceImpl
import com.google.gson.Gson
import groovy.json.JsonSlurper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.hamcrest.Matchers.containsInAnyOrder

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

//@WebMvcTest(controllers = [UserController])
class UserControllerHTTPSpec extends Specification {

    private static String BASE_URL = '/api/v1/users'

    //@Autowired
    private MockMvc mvc
    private UserController userController;
    private UserServiceImpl userService

    def setup() {
        userService = Mock()
        userController = new UserController(userService)
        mvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    // Static toJSON() method available?
    def Gson = new Gson();

    def "should get a status code '201' when calling the HTTP POST '/users' route with a user"() {
        given:
        // My name, email, etc.
        Map request = [
                first_name  : "dustin",
                last_name   : "buschman",
                email       : "dustin.buschman@gmail.com",
                address     : "001 SomeRandom St. RandomCity, OH"
        ]

        when:
        // I use those given credentials to call HTTP POST '/users'
        def response = mvc.perform(post('/api/v1/users')
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson.toJson(request)))

        then:
        // I expect 201 status indicating that the resource was created
        1 * userService.createUser(_ as User)
        response.andExpect(status().isCreated())


    }

    def "should get back a status code of '200' and a JSON response body containing a list users when calling HTTP GET '/users' route"() {

        given:
        User user1 = new User("1", "bob", "ross", "bob.ross@gmail.com", "123 Elm St.", ["sk8boarding"])
        User user2 = new User("2", "stan", "theman", "stan.theman@gmail.com", "234 Elm St.", ["hockey"])

        List<User> expectedUsers = [user1, user2]
        //List<User> expectedUsers = [user1]

        when:
        def response = mvc.perform(get('/api/v1/users')).andDo(print());

        then:
        1 * userService.getAllUsers() >> expectedUsers
        response.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath('$[*].id', containsInAnyOrder(expectedUsers[0].getId(), expectedUsers[1].getId())),
                jsonPath('$[*].firstName', containsInAnyOrder(expectedUsers[0].getFirstName(), expectedUsers[1].getFirstName())),
                jsonPath('$[*].lastName', containsInAnyOrder(expectedUsers[0].getLastName(), expectedUsers[1].getLastName())),
                jsonPath('$[*].email', containsInAnyOrder(expectedUsers[0].getEmail(), expectedUsers[1].getEmail())),
                jsonPath('$[*].address', containsInAnyOrder(expectedUsers[0].getAddress(), expectedUsers[1].getAddress())),
                jsonPath('$[*].hobbies', containsInAnyOrder(expectedUsers[0].getHobbies(), expectedUsers[1].getHobbies()))
        )

    }

    def "should get back a status code of '200' and a JSON response body containing a single user when calling HTTP GET '/users/{id}' route"() {

        given:
        User expectedUser = new User("1234", "bob", "ross", "bob.ross@gmail.com", "123 Elm St.", ["sk8boarding"])

        when:
        def response = mvc.perform(get('/api/v1/users/' + expectedUser.getId())).andDo(print());
        print(response)

        then:
        1 * userService.findByUserId(expectedUser.getId()) >> Optional.of(expectedUser)

        response.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath('$.id', is(expectedUser.getId())),
                jsonPath('$.firstName', is(expectedUser.getFirstName())),
                jsonPath('$.lastName', is(expectedUser.getLastName())),
                jsonPath('$.email', is(expectedUser.getEmail())),
                jsonPath('$.address', is(expectedUser.getAddress())),
                jsonPath('$.hobbies', is(expectedUser.getHobbies()))
        )
    }

    def "should get back a status code of '200' when deleting a user by ID"() {
        given:
        String userId = '1234'

        when:
        def response = mvc.perform(delete(BASE_URL + "/" + userId))

        then:
        1 * userService.deleteUser(_ as String)

        response.andExpect(status().isOk())

    }

    def "should get back a status code of '200' and a JSON body of the updated user when calling HTTP POST '/users/{id}'"() {
        given: 'an existing user with updated attributes'
        Map request = [
                id          : "1234",
                firstName  : "bob",
                lastName   : "ross",
                email       : "bob.ross@gmail.com",
                address     : "123 Elm St.",
                hobbies     : ["sk8boarding", "frisbee golf"]
        ]
        User updatedUser = new User(request.id, request.firstName, request.lastName, request.email, request.address, request.hobbies)

        when: 'a request is made to update an existing user'
        def response = mvc.perform(post('/api/v1/users/' + request.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson.toJson(request)))
                .andDo(print())


        then: 'a response with the updated attributes is given back'
        1 * userService.updateUser(_ as User, _ as String) >> updatedUser

        response.andExpect(status().isOk())
    }


}
