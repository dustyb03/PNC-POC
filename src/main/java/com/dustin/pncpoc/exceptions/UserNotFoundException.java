package com.dustin.pncpoc.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String id) {
        super(String.format("Oh nose! A user with the ID of %s was not found", id));
    }
}
