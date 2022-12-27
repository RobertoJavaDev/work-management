package pl.robertojavadev.workmanagementapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Task not found")
public class ResourceNotFoundException extends AppRuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
