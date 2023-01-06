package pl.robertojavadev.workmanagementapp.dto;

import lombok.Data;
import pl.robertojavadev.workmanagementapp.model.Section;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
public class ProjectDto {

    private UUID id;

    @NotBlank(message = "Project's name must not be empty")
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    private Instant creationDate = Instant.now();

    @Valid
    private List<Section> sections = new ArrayList<>();

    public ProjectDto(String name, String description) {
        this.name = name;
        this.description = description;
        this.creationDate = Instant.now();
    }

    public ProjectDto() {

    }
}