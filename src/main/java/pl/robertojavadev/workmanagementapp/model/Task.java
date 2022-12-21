package pl.robertojavadev.workmanagementapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Description must be not empty")
    private String description;

    private boolean done;

    private Instant deadline;
}