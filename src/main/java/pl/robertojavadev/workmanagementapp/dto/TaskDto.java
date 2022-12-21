package pl.robertojavadev.workmanagementapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
public class TaskDto {

    @NotBlank(message = "Description must be not empty")
    private String description;

    private Instant deadline;

}