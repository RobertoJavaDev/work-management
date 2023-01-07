package pl.robertojavadev.workmanagementapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value not found")
public class ResourceNotDeletedException extends Throwable {

    public ResourceNotDeletedException(String message) {
        super(message);
    }
}
