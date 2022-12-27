package pl.robertojavadev.workmanagementapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Project's description must not be empty")
    @Size(max = 30)
    private String description;

    private Instant creationDate;

    @OneToMany(mappedBy = "project")
    private Set<Section> sections;
}
