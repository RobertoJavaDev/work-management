package pl.robertojavadev.workmanagementapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Project's name must not be empty")
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 255, message = "Description's size must be between 0 and 255")
    private String description;

    @NotNull
    private Instant creationDate = Instant.now();

    @OneToMany(mappedBy = "project")
    private Set<Section> sections;

    public Project(String name, String description, Instant creationDate) {
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }
}
