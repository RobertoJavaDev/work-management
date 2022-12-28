package pl.robertojavadev.workmanagementapp.dto;

import org.mapstruct.Mapper;
import pl.robertojavadev.workmanagementapp.model.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDto mapProjectEntityToProjectDto(Project project);
}
