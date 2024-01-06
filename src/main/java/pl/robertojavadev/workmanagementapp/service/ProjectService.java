package pl.robertojavadev.workmanagementapp.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.dto.ProjectMapper;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotDeletedException;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotFoundException;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.repository.ProjectRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Validated
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::mapProjectEntityToProjectDto)
                .toList();
    }

    @Transactional
    public ProjectDto createProject(@Valid ProjectDto projectRequest) {

        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());

        return projectMapper.mapProjectEntityToProjectDto(project);
    }

    @Transactional(readOnly = true)
    public ProjectDto getProjectById(@NotNull UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Project with Id: " + id + " does not exist.");
        });
        return projectMapper.mapProjectEntityToProjectDto(project);
    }

    @Transactional
    public ProjectDto updateProject(@NotNull UUID id, @Valid ProjectDto projectRequest) {

        Project project = projectRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Project with Id: " + id + " does not exist.");
        });

        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());

        return projectMapper.mapProjectEntityToProjectDto(project);
    }

    @Transactional
    public void deleteProject(@NotNull UUID id) throws ResourceNotDeletedException {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new ResourceNotDeletedException("Project with Id: " + id + " does not exist.");
        }
    }
}
