package pl.robertojavadev.workmanagementapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
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