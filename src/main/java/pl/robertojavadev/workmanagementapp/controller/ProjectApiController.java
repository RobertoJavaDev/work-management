package pl.robertojavadev.workmanagementapp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotDeletedException;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.service.ProjectService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/projects")
public class ProjectApiController {

    private final ProjectService projectService;

    ProjectApiController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @Transactional
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {

        List<Project> projects = projectService.getAllProjects();
        HttpHeaders headers = new HttpHeaders();

        if (projects.isEmpty()) {
            headers.add("message", "There are no available projects to view");
        } else {
            headers.add("message", "The projects has been successfully retrieved");
        }

        return new ResponseEntity<>(projects, headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectDto projectRequest) {

        ProjectDto project = projectService.createProject(projectRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The project has been successfully created");

        return new ResponseEntity<>(project, headers, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@Valid @PathVariable UUID id, @RequestBody ProjectDto projectRequest) {

        ProjectDto project = projectService.updateProject(id, projectRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The project has been successfully updated");

        return new ResponseEntity<>(project, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectDto> deleteProject(@Valid @PathVariable UUID id) throws ResourceNotDeletedException {

        projectService.deleteProject(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The project has been successfully deleted");

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
