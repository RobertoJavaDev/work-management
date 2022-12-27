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

    private boolean done;

    private Instant deadline;

    public TaskDto(final UUID id, final String description, final Instant deadline) {
        this.id = id;
        this.description = description;
        this.deadline = deadline;
    }

    public TaskDto(final UUID id, final String description, final boolean done, final Instant deadline) {
        this.id = id;
        this.description = description;
        this.done = done;
        this.deadline = deadline;
    }

    public TaskDto() {

    }
}