package pl.robertojavadev.workmanagementapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.service.ProjectService;

@Controller
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectViewController {

    private final ProjectService projectService;

    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", projectService.getAllProjects());

        return "project/index";
    }
}
