package pl.robertojavadev.workmanagementapp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

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
}
