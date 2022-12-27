package pl.robertojavadev.workmanagementapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sections")
@Getter
@Setter
@ToString
public class Section {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Description must be not empty")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section")
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
