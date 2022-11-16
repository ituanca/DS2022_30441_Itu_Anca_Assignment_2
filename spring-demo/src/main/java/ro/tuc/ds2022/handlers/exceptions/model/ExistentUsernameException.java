package ro.tuc.ds2022.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

public class ExistentUsernameException extends CustomException{

    private static final String MESSAGE = "Username already exists!";
    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public ExistentUsernameException(String resource) {
        super(MESSAGE,httpStatus, resource, new ArrayList<>());
    }
}
