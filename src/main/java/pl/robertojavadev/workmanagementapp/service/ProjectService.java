package pl.robertojavadev.workmanagementapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.dto.ProjectMapper;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.repository.ProjectRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public ProjectDto createProject(ProjectDto projectRequest) {

        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());

        return projectMapper.mapProjectEntityToProjectDto(project);
    }
}
