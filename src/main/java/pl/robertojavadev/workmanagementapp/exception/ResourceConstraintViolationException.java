package pl.robertojavadev.workmanagementapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.BAD_REQUEST, reason = "Incorrect data input")
public class ResourceConstraintViolationException extends AppRuntimeException{

    public ResourceConstraintViolationException(String message) {
        super(message);
    }
}
