package pl.robertojavadev.workmanagementapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.dto.ProjectMapper;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.repository.ProjectRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public ProjectDto createProject(@Valid ProjectDto projectRequest) {

        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());

        return projectMapper.mapProjectEntityToProjectDto(projectRepository.save(project));
    }

    public Optional<Project> getProjectById(UUID id) {

        return projectRepository.findById(id);
    }

    public void deleteProject(final UUID id) {

        if (!projectRepository.existsById(id)) {
            throw new ResourceNotDeletedException("Project does not exist, please change your request");
        }

        projectRepository.deleteById(id);
    }
}
