package pl.robertojavadev.workmanagementapp.dto;

import lombok.Data;
import pl.robertojavadev.workmanagementapp.model.Section;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Valid
    private List<Section> sections = new ArrayList<>();
}