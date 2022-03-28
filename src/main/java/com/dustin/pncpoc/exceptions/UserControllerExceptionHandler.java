package com.dustin.pncpoc.exceptions;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseBody //- If you want to return without ResponseEntity, and just return the error message
public class UserControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorMessage handleUserNotFoundException(UserNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
