package com.dustin.pncpoc.services

import com.dustin.pncpoc.models.User
import com.dustin.pncpoc.repositories.UserRepository
import spock.lang.Specification

class UserServiceSpec extends Specification {

    private UserServiceImpl userService;
    private UserRepository userRepository;

    def setup() {
        userRepository = Mock();
        userService = new UserServiceImpl(userRepository);
    }

    def "should create a new user when given a User object"() {
        given:
        User userToCreate = new User(firstName: 'dustin', lastName: 'buschman', address: '123 SomePlace Drive', email: 'dustin.buschman', hobbies: ['pingpong', 'progr4mmin']);
        User createdUser = new User(id: '1', firstName: 'dustin', lastName: 'buschman', address: '123 SomePlace Drive', email: 'dustin.buschman', hobbies: ['pingpong', 'progr4mmin'])

        when:
        def response = userService.createUser(userToCreate);

        then:
        1 * userRepository.save(_ as User) >> createdUser
        response.id == '1'


    }

    def "should return a list of all users when requested"() {
        given:
        User user1 = new User("1", "bob", "ross", "bob.ross@gmail.com", "123 Elm St.", ["sk8boarding"])
        User user2 = new User("2", "stan", "theman", "stan.theman@gmail.com", "234 Elm St.", ["hockey"])

        List<User> users = [user1, user2]

        when:
        def retrievedUsers = userService.getAllUsers()

        then:
        1 * userRepository.findAll() >> users
        retrievedUsers.size() == 2
    }

    def "should update a user"() {
        given:
        User existingUser = new User("1", "bob", "ross", "bob.ross@gmail.com", "123 Elm St.", ["sk8boarding"])
        User userToUpdate = new User("1", "bob", "ross", "bob.ross@gmail.com", "123 Elm St.", ["sk8boarding", "piano"])

        when:
        def updatedUser = userService.updateUser(userToUpdate, userToUpdate.getId());

        then:
        1 * userRepository.findById(_ as String) >> Optional.of(userToUpdate)
        1 * userRepository.save(_ as User) >> userToUpdate

        updatedUser == userToUpdate
    }

    def "should delete a user"() {
        given: 'a user to delete'
        User userToDelete = new User("1", "bob", "ross", "bob.ross@gmail.com", "123 Elm St.", ["sk8boarding"])

        when:
        userService.deleteUser(userToDelete.getId());

        then:
        1 * userRepository.deleteUserById(_ as String)

    }
}
