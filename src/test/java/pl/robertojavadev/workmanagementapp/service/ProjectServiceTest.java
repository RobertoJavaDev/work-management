package pl.robertojavadev.workmanagementapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pl.robertojavadev.workmanagementapp.dto.ProjectDto;
import pl.robertojavadev.workmanagementapp.dto.ProjectMapper;
import pl.robertojavadev.workmanagementapp.model.Project;
import pl.robertojavadev.workmanagementapp.repository.ProjectRepository;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class ProjectServiceTest {

    private static final String projectName1 = "Project Java";
    private static final String projectName2 = "Project sport";
    private static final String projectName3 = "Project english";

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectMapper projectMapper;

    @Test
    void shouldReturnAllProjects() {
        //given
        List<Project> projects = new ArrayList<>(List.of(
                new Project(projectName1, "Learn programming", Instant.now()),
                new Project(projectName2, "", Instant.now()),
                new Project(projectName3, "Learn english vocabulary", Instant.now())
        ));

        //when
        when(projectRepository.findAll()).thenReturn(projects);
        List<Project> allProjects = projectService.getAllProjects();

        //then
        assertThat(allProjects, hasSize(projects.size()));
    }

    @Test
    void shouldCreatedProjectCorrectly() {
        //given
        Project project = new Project(projectName1, "Learn every day", Instant.now());
        ProjectDto createdProject = new ProjectDto(projectName1, "Learn every day");
        ProjectDto projectDto = new ProjectDto(projectName1, "Learn every day");

        //when
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.mapProjectEntityToProjectDto(project)).thenReturn(projectDto);
        ProjectDto returnProject = projectService.createProject(createdProject);

        //then
        assertEquals(project.getName(), returnProject.getName());
        assertEquals(project.getDescription(), returnProject.getDescription());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsEmpty() {
        //given
        Project project = new Project("", "Learn every day", Instant.now());
        ProjectDto createdProject = new ProjectDto("", "Learn every day");
        ProjectDto projectDto = new ProjectDto("", "Learn every day");

        //when
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.mapProjectEntityToProjectDto(project)).thenReturn(projectDto);

        //then
        assertThrows(ConstraintViolationException.class, () -> projectService.createProject(createdProject));
    }

    @Test
    void shouldThrowAnExceptionWhenDescriptionHasWhiteSpaces() {
        //given
        Project project = new Project("    ", "Learn every day", Instant.now());
        ProjectDto createdProject = new ProjectDto("    ", "Learn every day");
        ProjectDto projectDto = new ProjectDto("    ", "Learn every day");

        //when
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.mapProjectEntityToProjectDto(project)).thenReturn(projectDto);

        //then
        assertThrows(ConstraintViolationException.class, () -> projectService.createProject(createdProject));
    }
}
