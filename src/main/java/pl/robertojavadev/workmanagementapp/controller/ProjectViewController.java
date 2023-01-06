package pl.robertojavadev.workmanagementapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.service.ProjectService;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectViewController {

    private final ProjectService projectService;

    @GetMapping
    public String showProjects(Model model) {

        model.addAttribute("project", new ProjectDto());

        return "project/index";
    }

    @PostMapping
    public String addProject(
            @Valid
            @ModelAttribute("project") ProjectDto current,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "project/index";
        }
        projectService.createProject(current);
        model.addAttribute("project", new Project());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Added project!");

        return "project/index";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return projectService.getAllProjects();
    }
}
