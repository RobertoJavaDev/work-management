package pl.robertojavadev.workmanagementapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Description must be not empty")
    private String description;

    private boolean done;

    private Instant deadline;

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private Section section;

    public Task(String description, Instant deadline) {
        this.description = description;
        this.done = false;
        this.deadline = deadline;
    }

    public Task(UUID id, String description, Instant deadline) {
        this.id = id;
        this.description = description;
        this.done = false;
        this.deadline = deadline;
    }
}