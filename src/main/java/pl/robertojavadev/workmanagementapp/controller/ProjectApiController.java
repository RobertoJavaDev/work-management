package pl.robertojavadev.workmanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.exception.ResourceNotDeletedException;
import pl.robertojavadev.workmanagementapp.service.ProjectService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
public class ProjectApiController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {

        List<ProjectDto> projectsDtoList = projectService.getAllProjects();
        HttpHeaders headers = new HttpHeaders();

        if (projectsDtoList.isEmpty()) {
            headers.add("message", "There are no available projects to view");
        } else {
            headers.add("message", "The projects has been successfully retrieved");
        }

        return new ResponseEntity<>(projectsDtoList, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@NotNull @PathVariable UUID id) {

        ProjectDto projectDto = projectService.getProjectById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The project has been successfully retrieved");

        return new ResponseEntity<>(projectDto, headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectDto projectRequest) {

        ProjectDto projectDto = projectService.createProject(projectRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The project has been successfully created");

        return new ResponseEntity<>(projectDto, headers, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@NotNull @PathVariable UUID id, @Valid @RequestBody ProjectDto projectRequest) {

        ProjectDto projectDto = projectService.updateProject(id, projectRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The project has been successfully updated");

        return new ResponseEntity<>(projectDto, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectDto> deleteProject(@Valid @PathVariable UUID id) throws ResourceNotDeletedException {

        projectService.deleteProject(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The project has been successfully deleted");

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
