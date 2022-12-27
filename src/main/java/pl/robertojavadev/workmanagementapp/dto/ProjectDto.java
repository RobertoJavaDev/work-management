package pl.robertojavadev.workmanagementapp.dto;

import lombok.Data;
import pl.robertojavadev.workmanagementapp.model.Section;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ProjectDto {

    private UUID id;

    @NotBlank(message = "Project's description must not be empty")
    private String description;

    @Valid
    private List<Section> sections = new ArrayList<>();
}