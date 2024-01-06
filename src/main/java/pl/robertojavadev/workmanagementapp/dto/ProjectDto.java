package pl.robertojavadev.workmanagementapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.robertojavadev.workmanagementapp.model.Section;

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
    @Size(max = 255, message = "Description's size must be between 0 and 255")
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
