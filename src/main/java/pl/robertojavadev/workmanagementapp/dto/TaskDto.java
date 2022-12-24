package pl.robertojavadev.workmanagementapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Data
public class TaskDto {

    private UUID id;

    @NotBlank(message = "Description must be not empty")
    private String description;

    private Instant deadline;

}